package com.cgzz.job.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.SyncStateContract.Constants;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cgzz.job.BaseActivity;
import com.cgzz.job.R;
import com.cgzz.job.utils.ToastUtil;

/**
 */
public class MarkerActivity extends BaseActivity implements OnMarkerClickListener, OnInfoWindowClickListener,
		OnMarkerDragListener, OnMapLoadedListener, OnClickListener, InfoWindowAdapter {
	private MarkerOptions markerOption;
	// private TextView markerText;
	// private RadioGroup radioOption;
	private AMap aMap;
	private MapView mapView;
	private Marker marker2;// ������Ч����marker����
	private LatLng latlng = new LatLng(36.061, 103.834);
	double longitude, latitude;
	LatLng HotelLatLng;
	String hotelratingbar, hoteltitle, hotelsnippet;
	LinearLayout llLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_activity);
		setTitle("��ַ", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "ע��");
		Intent intent = getIntent();
		try {
			latitude = Double.valueOf(intent.getStringExtra("latitude"));
			longitude = Double.valueOf(intent.getStringExtra("longitude"));

		} catch (Exception e) {
			ToastUtil.makeShortText(MarkerActivity.this, "���ݳ����쳣���Ժ�����");
			finish();
		}

		HotelLatLng = new LatLng(latitude, longitude);

		hotelratingbar = intent.getStringExtra("hotelratingbar");
		hoteltitle = intent.getStringExtra("hoteltitle");
		hotelsnippet = intent.getStringExtra("hotelsnippet");
		/*
		 * 
		 * �������ߵ�ͼ�洢Ŀ¼�����������ߵ�ͼ���ʼ����ͼ����; ʹ�ù����п���������, ���������������ߵ�ͼ�洢��·����
		 * ����Ҫ�����ߵ�ͼ���غ�ʹ�õ�ͼҳ�涼����·������
		 */
		// Demo��Ϊ�������������ʹ�����ص����ߵ�ͼ��ʹ��Ĭ��λ�ô洢���������Զ�������
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // �˷���������д
		init();
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		// markerText = (TextView) findViewById(R.id.mark_listenter_text);
		// radioOption = (RadioGroup)
		// findViewById(R.id.custom_info_window_options);
		// Button clearMap = (Button) findViewById(R.id.clearMap);
		// clearMap.setOnClickListener(this);
		// Button resetMap = (Button) findViewById(R.id.resetMap);
		// resetMap.setOnClickListener(this);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		addMarkersToMap();// ����ͼ�����marker

		llLeft.setOnClickListener(this);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mapView != null)
			mapView.onDestroy();
	}

	/**
	 * �ڵ�ͼ�����marker
	 */
	private void addMarkersToMap() {
		// Marker marker = aMap.addMarker(new MarkerOptions()
		//
		// .title("�ú�ѧϰ")
		// .icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		// .perspective(true).draggable(true));
		// marker.setRotateAngle(90);// ����marker��ת90��
		// marker.showInfoWindow();// ����Ĭ����ʾһ��infowinfow
		// marker.setPositionByPixels(400, 400);

		markerOption = new MarkerOptions();
		markerOption.position(HotelLatLng);
		markerOption.title("1").snippet("");
		markerOption.perspective(true);
		markerOption.draggable(true);
		markerOption.icon(
				// BitmapDescriptorFactory
				// .fromResource(R.drawable.location_marker)
				BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)));
		// ��Marker����Ϊ������ʾ������˫ָ������Ч��
		markerOption.setFlat(true);

		// ArrayList<BitmapDescriptor> giflist = new
		// ArrayList<BitmapDescriptor>();
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		// MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
		// .position(Constants.CHENGDU).title("�ɶ���")
		// .snippet("�ɶ���:30.679879, 104.064855").icons(giflist)
		// .perspective(true).draggable(true).period(50);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		// markerOptionlst.add(markerOption1);
		List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
		marker2 = markerlst.get(0);
		marker2.showInfoWindow();// ����Ĭ����ʾһ��infowinfow
	}

	/**
	 * ��marker��ע������Ӧ�¼�
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.equals(marker2)) {
			if (aMap != null) {
				// jumpPoint(marker);
			}
		}
		// markerText.setText("��������" + marker.getTitle());
		return false;
	}

	/**
	 * marker���ʱ����һ��
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(HotelLatLng);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				double lng = t * HotelLatLng.longitude + (1 - t) * startLatLng.longitude;
				double lat = t * HotelLatLng.latitude + (1 - t) * startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	/**
	 * �������infowindow�����¼��ص�
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		ToastUtil.makeShortText(this, "������infoWindow����" + marker.getTitle());
		ToastUtil.makeShortText(MarkerActivity.this, "��ǰ��ͼ����������Marker����:" + aMap.getMapScreenMarkers().size());
	}

	/**
	 * �����϶�markerʱ�¼��ص�
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		String curDes = marker.getTitle() + "�϶�ʱ��ǰλ��:(lat,lng)\n(" + marker.getPosition().latitude + ","
				+ marker.getPosition().longitude + ")";
		// markerText.setText(curDes);
	}

	/**
	 * �����϶�marker�����¼��ص�
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {
		// markerText.setText(marker.getTitle() + "ֹͣ�϶�");
	}

	/**
	 * ������ʼ�϶�marker�¼��ص�
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
		// markerText.setText(marker.getTitle() + "��ʼ�϶�");
	}

	/**
	 * ����amap��ͼ���سɹ��¼��ص�
	 */
	@Override
	public void onMapLoaded() {
		// ��������maker��ʾ�ڵ�ǰ���������ͼ��
		// LatLngBounds bounds = new LatLngBounds.Builder()
		// .include(HotelLatLng).build();
		// aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HotelLatLng.latitude, HotelLatLng.longitude), 14));

	}

	/**
	 * �����Զ���infowindow���ڵ�infocontents�¼��ص�
	 */
	@Override
	public View getInfoContents(Marker marker) {
		// if (radioOption.getCheckedRadioButtonId() !=
		// R.id.custom_info_contents) {
		// return null;
		// }
		View infoContent = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	/**
	 * �����Զ���infowindow���ڵ�infowindow�¼��ص�
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		// if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window)
		// {
		// return null;
		// }
		View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * �Զ���infowinfow����
	 */
	public void render(Marker marker, View view) {
		// if (radioOption.getCheckedRadioButtonId() ==
		// R.id.custom_info_contents) {
		// ((ImageView) view.findViewById(R.id.badge))
		// .setImageResource(R.drawable.badge_sa);
		// } else if (radioOption.getCheckedRadioButtonId() ==
		// R.id.custom_info_window) {
		// ImageView imageView = (ImageView) view.findViewById(R.id.badge);
		// imageView.setImageResource(R.drawable.badge_wa);
		// }
		// String title = marker.getTitle();

		TextView titleUi = ((TextView) view.findViewById(R.id.win_hotel_title));
		TextView win_hotel_message = ((TextView) view.findViewById(R.id.win_hotel_message));
		titleUi.setText(hoteltitle);
		// if (title != null) {
		// SpannableString titleText = new SpannableString(title);
		// titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
		// titleText.length(), 0);
		// titleUi.setTextSize(15);
		// titleUi.setText(titleText);
		//
		// } else {
		// titleUi.setText("");
		// }
		// String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.win_hotel_snippet));

		snippetUi.setText(hotelsnippet);

		// if (snippet != null) {
		// SpannableString snippetText = new SpannableString(snippet);
		// snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
		// snippetText.length(), 0);
		// snippetUi.setTextSize(20);
		// snippetUi.setText(snippetText);
		// } else {
		// snippetUi.setText("");
		// }
		com.cgzz.job.view.CustomerRatingBar win_hotel_ratingbar = ((com.cgzz.job.view.CustomerRatingBar) view
				.findViewById(R.id.win_hotel_ratingbar));
		try {
			int star = Integer.parseInt(hotelratingbar);

			if (star == 0) {
				win_hotel_message.setVisibility(View.VISIBLE);
				win_hotel_message.setText("����");
				win_hotel_ratingbar.setVisibility(View.GONE);
			} else if (star == 1) {

				win_hotel_message.setVisibility(View.VISIBLE);
				win_hotel_message.setText("�����������Ƶ�");
				win_hotel_ratingbar.setVisibility(View.GONE);

			} else if (star >= 2) {

				win_hotel_message.setVisibility(View.GONE);
				win_hotel_ratingbar.setVisibility(View.VISIBLE);
				win_hotel_ratingbar.setProgress(star);

			}

		} catch (Exception e) {
			win_hotel_ratingbar.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_left:
			finish();
			break;
		default:
			break;
		}
	}
}
