package com.cgzz.job.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.adapter.PromptAdapter;
import com.cgzz.job.application.GlobalVariables;
import com.cgzz.job.bean.PoiLocation;
import com.cgzz.job.http.AnsynHttpRequest;
import com.cgzz.job.http.HttpStaticApi;
import com.cgzz.job.http.ObserverCallBack;
import com.cgzz.job.http.ParserUtil;
import com.cgzz.job.http.UrlConfig;
import com.cgzz.job.utils.ImageTools;
import com.cgzz.job.utils.ToastUtil;
import com.cgzz.job.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 基本资料
 */
public class TheirProfileActivity extends BaseActivity
		implements OnClickListener, TextWatcher, OnPoiSearchListener, OnItemClickListener, OnCheckedChangeListener {
	// public ImageLoader mImageLoader;
	RelativeLayout rl_my_workcard;
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.login_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());

					// 保存到全局
					// 头
					if (!"".equals(faceurl))
						application.setFaceUrl(faceurl);
					// 姓名
					application.setRealname(name);
					// 年龄
					application.setAge(age);
					// 工龄
					application.setWorkage(gongling);
					// 地址
					if (!"".equals(address)) {
						application.setAddress(address);
						application.setHomeLatitude(latitude);
						application.setHomeLongitude(longitude);
					}

					if (cb_seting_training.isChecked()) {
						application.setIs_healthy("1");
					} else {
						application.setIs_healthy("0");
					}
					Intent mIntents = new Intent();
					mIntents.putExtra("ischenggong", "y");
					setResult(1, mIntents);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());
					Intent mIntent = new Intent();
					mIntent.putExtra("ischenggong", "n");
					setResult(1, mIntent);
					finish();
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theirprofile);
		setTitle("基本资料", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "完成");
		findView();
		initPopWindow();
		Assignment();
		init();
	}

	private TextView iv_their_credit, tv_their_tel, tv_their_passwords;
	private ImageView iv_their_pic;
	private LinearLayout llLeft, llright;
	private AutoCompleteTextView et_their_destination;
	private EditText et_their_age, et_their_gongling, iv_their_name, et_their_tel;
	private CheckBox cb_seting_training;

	private void findView() {

		iv_their_pic = (ImageView) findViewById(R.id.iv_their_pic);// 头像
		iv_their_credit = (TextView) findViewById(R.id.iv_their_credit);// 信用值
		cb_seting_training = (CheckBox) findViewById(R.id.cb_seting_training);// 健康证
		et_their_destination = (AutoCompleteTextView) findViewById(R.id.et_their_destination);// 家的位置
		tv_their_tel = (TextView) findViewById(R.id.tv_their_tel);// 电话
		et_their_gongling = (EditText) findViewById(R.id.et_their_gongling);// 工龄
		et_their_age = (EditText) findViewById(R.id.et_their_age);// 年龄
		iv_their_name = (EditText) findViewById(R.id.iv_their_name);// 姓名
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);

		tv_their_passwords = (TextView) findViewById(R.id.tv_their_passwords);// 修改密码

	}

	private void Assignment() {
		// mImageLoader = new ImageLoader(
		// GlobalVariables.getRequestQueue(TheirProfileActivity.this),
		// new BitmapCache());
		// 头像
		ImageListener listener = ImageLoader.getImageListener(iv_their_pic, R.drawable.icon_touxiangmoren,
				R.drawable.icon_touxiangmoren);
//		String url = application.getFaceUrl();
		try {
			mImageLoader.get(application.getFaceUrl(), listener);
		} catch (Exception e) {
			// TODO: handle exception
		}

		iv_their_credit.setText("信用分:" + application.getXinyong());
		et_their_destination.setText(application.getAddress());
		tv_their_tel.setText(application.getMobile());
		et_their_gongling.setText(application.getWorkage());
		et_their_age.setText(application.getAge());
		iv_their_name.setText(application.getRealname());
		if (application.getIs_healthy().equals("1")) {

			cb_seting_training.setChecked(true);
		} else {
			cb_seting_training.setChecked(false);
		}

	}

	private void init() {
		
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_their_passwords.setOnClickListener(this);
		iv_their_pic.setOnClickListener(this);
		et_their_destination.addTextChangedListener(this);
		et_their_destination.setOnItemClickListener(this);
		cb_seting_training.setOnCheckedChangeListener(this);
		iv_their_credit.setOnClickListener(this);
	}

	String name = "";
	String age = "";
	String gongling = "";

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {

		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 完成

			// if (Utils.isEmpty(address)) {
			// ToastUtil.makeShortText(this, "请填写家庭住址");
			// return;
			// }

			name = iv_their_name.getText().toString();
			if (Utils.isEmpty(name)) {
				ToastUtil.makeShortText(this, "姓名不能为空");
				return;
			} else {

				if (!checkNameChese(name)) {
					ToastUtil.makeShortText(this, "姓名请输入中文");
					return;
				}
			}

			age = et_their_age.getText().toString();
			if (Utils.isEmpty(age)) {
				ToastUtil.makeShortText(this, "年龄不能为空");
				return;
			} else {
				int i = Integer.parseInt(age);
				if (i > 60 || i < 18) {
					ToastUtil.makeShortText(this, "年龄请填写18~60岁之间");
					return;
				}
			}

			gongling = et_their_gongling.getText().toString();
			if (Utils.isEmpty(gongling)) {
				ToastUtil.makeShortText(this, "工龄不能为空");
				return;
			} else {
				int i = Integer.parseInt(gongling);
				if (i > 30) {
					ToastUtil.makeShortText(this, "工龄请填写0~30年之间");
					return;
				}
			}
			if (cb_seting_training.isChecked()) {
				isTrain = 1;
			} else {
				isTrain = 0;
			}

			updateUser(UrlConfig.updateUser_Http, application.getUserId(), application.getToken(), "2", name, age,
					gongling, faceurl, address, isTrain, latitude, longitude, true);
			break;

		case R.id.tv_their_passwords:// 修改密码

			intent = new Intent(TheirProfileActivity.this, ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_their_pic:// 头像
			popTheirProfile(true);
			break;
			
		case R.id.iv_their_credit:// 
			
			
			String url = "http://www.haoshoubang.com/bangke/html/credit.html";
			intent = new Intent(TheirProfileActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "信用值说明");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
			break;
			
		default:
			break;
		}

	}

	public boolean checkNameChese(String name) {

		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (!isChinese(cTemp[i])) {

				res = false;

				break;

			}

		}

		return res;

	}

	public boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	private void initPopWindow() {

		prompAdapter = new PromptAdapter(this);
		et_their_destination.setAdapter(prompAdapter);
		prompAdapter.refreshData(prompList);
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	String latitude = "", longitude = "", address = "";

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					List<PoiItem> poiItems = result.getPois();
					if (poiItems != null) {
						prompList.clear();
						for (int i = 0; i < poiItems.size(); i++) {
							PoiLocation ai = new PoiLocation();
							ai.setLat(poiItems.get(i).getLatLonPoint());
							ai.setTitle(poiItems.get(i).getTitle());

							prompList.add(ai);
						}
						// 刷新列表
						prompAdapter.refreshData(prompList);
						et_their_destination.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// 保存到配置文件，通过历史记录读取。并跳转页面
								PoiLocation poh2 = prompList.get(position);
								longitude = poh2.getLat().getLongitude() + "";
								latitude = poh2.getLat().getLatitude() + "";
								address = poh2.getTitle() + "";

							}
						});

					}

				}
			}
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		doSearchQuery(arg0.toString().trim());

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	private List<PoiLocation> prompList = new ArrayList<PoiLocation>();
	private PromptAdapter prompAdapter;
	private PoiSearch poiSearch;// POI搜索
	private PoiSearch.Query query;// Poi查询条件类

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(String searchText) {
		if (!Utils.isEmpty(searchText)) {
			query = new PoiSearch.Query(searchText, "", application.getCityCode());
			query.setPageSize(10);
			query.setPageNum(0);

			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		} else {
			// 显示历史记录
			// ToastUtil.makeShortText(DestinationActivity.this, "请输入搜索关键字");
		}

	}

	int isTrain = 0;

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (cb_seting_training == arg0) {
			if (arg1) {
				isTrain = 1;
			} else {
				isTrain = 0;
			}
		}

	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile(final boolean typePop) {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("请选择打开方式");
		up.setText("拍照");
		under.setText("相册");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

				Uri imageUri = null;
				String fileName = null;
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// if (crop) {
				// 删除上一次截图的临时文件
				SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
				ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(),
						sharedPreferences.getString("tempName1", ""));

				// 保存本次截图临时文件名字
				fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				Editor editor = sharedPreferences.edit();
				editor.putString("tempName1", fileName);
				editor.commit();
				// }else {
				// REQUEST_CODE = TAKE_PICTURE;
				// fileName = "image.jpg";
				// }
				imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, CROP);

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(openAlbumIntent, CROP);
			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					String fileName;
					fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName1", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 100, 100, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				// if (photoUri != null) {
				//// photo = BitmapFactory.decodeFile(photoUri.getPath());
				// photo = Bitmapyichu(photoUri.getPath());
				// }
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.PNG, 30, stream);
					}
				}
				iv_their_pic.setImageBitmap(photo);

				UpdateTextTask mTask = new UpdateTextTask(TheirProfileActivity.this);
				mTask.execute(photo);
				break;
			default:
				break;
			}
		}
	}

	public Bitmap Bitmapyichu(String photo) {

		Bitmap bmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photo, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(photo, opts);
		} catch (OutOfMemoryError err) {
		}

		// BitmapFactory.Options opt = new BitmapFactory.Options();
		//
		// opt.inPreferredConfig = Config.ARGB_8888;
		//
		// opt.inPurgeable = true;// 允许可清除
		//
		// opt.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
		//
		//
		//
		// // 这个isjustdecodebounds很重要
		// opt.inJustDecodeBounds = true;
		//// Bitmap bm = BitmapFactory.decodeFile(photo, opt);
		// Bitmap bm =null;
		// // 获取到这个图片的原始宽度和高度
		// int picWidth = opt.outWidth;
		// int picHeight = opt.outHeight;
		//
		// // 获取屏的宽度和高度
		// WindowManager windowManager = getWindowManager();
		// Display display = windowManager.getDefaultDisplay();
		// int screenWidth = display.getWidth();
		// int screenHeight = display.getHeight();
		//
		// // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		// opt.inSampleSize = 2;
		// // 根据屏的大小和图片大小计算出缩放比例
		// if (picWidth > picHeight) {
		// if (picWidth > screenWidth)
		// opt.inSampleSize = picWidth / screenWidth;
		// } else {
		// if (picHeight > screenHeight)
		//
		// opt.inSampleSize = picHeight / screenHeight;
		// }
		//
		// // 这次再真正地生成一个有像素的，经过缩放了的bitmap
		// opt.inJustDecodeBounds = false;
		// return bm = BitmapFactory.decodeFile(photo, opt);

		return bmp;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128
				: (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public void distoryBitmap(Bitmap bmb) {
		if (null != bmb && !bmb.isRecycled())
			bmb.recycle();
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(TheirProfileActivity.this, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}

		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 4);

		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			} else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	String faceurl = "";

	public void testUploadFace(String url, String name, String path) {

		// 设置请求参数的编码
		RequestParams params = new RequestParams(); // 默认编码UTF-8

		// 添加文件
		params.addBodyParameter("type", "2");
		params.addBodyParameter(name, new File(path));
		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				showWaitDialog2();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
				} else {
				}
			}

			// 成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dismissWaitDialog2();
				Bundle bundle = null;
				bundle = ParserUtil.ParserUpload(responseInfo.result);
				// application.setFaceUrl(bundle.get("url").toString());
				faceurl = bundle.get("url").toString();

			}

			// 失败
			@Override
			public void onFailure(HttpException error, String msg) {
				dismissWaitDialog2();
				ToastUtil.makeShortText(TheirProfileActivity.this, "上传失败，请重新上传图片");
				iv_their_pic.setImageDrawable(getResources().getDrawable(R.drawable.icon_nor_user));
			}
		});
	}

	private void updateUser(String url, String userid, String token, String apptype, String realname, String age,
			String workage, String portrait, String address, int isTrain2, String latitude, String longitude,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", token);
		map.put("apptype", apptype);
		map.put("realname", realname);
		map.put("age", age);
		map.put("workage", workage);
		if (!Utils.isEmpty(portrait)) 
			map.put("portrait", portrait);
		if (!Utils.isEmpty(address)) {
			map.put("address", address);
			map.put("latitude", latitude);
			map.put("longitude", longitude);
		}
		map.put("isHealthy", isTrain2 + "");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, TheirProfileActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(TheirProfileActivity.this), HttpStaticApi.login_Http, null, loadedtype);
	}

	class UpdateTextTask extends AsyncTask<Bitmap, Integer, String> {
		private Context context;

		UpdateTextTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Bitmap... params) {
			showWaitDialog2();
			String face = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "face");

			return face;
		}

		@Override
		protected void onPostExecute(String result) {
			testUploadFace(UrlConfig.uploadiv_Http, "file", result);
		}
	}

}
