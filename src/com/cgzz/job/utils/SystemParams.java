package com.cgzz.job.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class SystemParams {
	private final String TAG = "SystemParams";
	private static SystemParams params;
	public float screenWidth;// ��Ļ��ȣ���λΪpx
	public float screenHeight;// ��Ļ�߶ȣ���λΪpx
	public int densityDpi;// ��Ļ�ܶȣ���λΪdpi
	public float scale;// ����ϵ����ֵΪ densityDpi/160
	public float fontScale;// ��������ϵ����ͬscale

	public final static int SCREEN_ORIENTATION_VERTICAL = 1; // ��Ļ״̬������
	public final static int SCREEN_ORIENTATION_HORIZONTAL = 2; // ��Ļ״̬������
	public int screenOrientation = SCREEN_ORIENTATION_VERTICAL;// ��ǰ��Ļ״̬��Ĭ��Ϊ����
	
	/**
	 * ˽�й��췽��
	 * 
	 * @param activity
	 */
	private SystemParams(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels - getStatusHeight(activity)-90-105-88-120;
		densityDpi = dm.densityDpi;
		scale = dm.density;
		fontScale = dm.scaledDensity;

		screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL
				: SCREEN_ORIENTATION_HORIZONTAL;
		
	}

	/**
	 * ��ȡʵ��
	 * 
	 * @param activity
	 * @return
	 */
	public static SystemParams getInstance(Activity activity) {
		if (params == null) {
			params = new SystemParams(activity);
		}
		return params;
	}

	/**
	 * ��ȡһ����ʵ��
	 * 
	 * @param activity
	 * @return
	 */
	public static SystemParams getNewInstance(Activity activity) {
		if (params != null) {
			params = null;
		}
		return getInstance(activity);
	}

	/**
	 * ������Ϣ
	 */
	public String toString() {
		return TAG
				+ ":[screenWidth: "
				+ screenWidth
				+ " screenHeight: "
				+ screenHeight
				+ " scale: "
				+ scale
				+ " fontScale: "
				+ fontScale
				+ " densityDpi: "
				+ densityDpi
				+ " screenOrientation: "
				+ (screenOrientation == SCREEN_ORIENTATION_VERTICAL ? "vertical"
						: "horizontal") + "]";
	}
	/**
	 * ��ȡ״̬���߶�
	 * @param activity
	 * @return
	 */
	public int getStatusHeight(Activity activity){  
	    int statusHeight = 0;  
	    Rect localRect = new Rect();  
	    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
	    statusHeight = localRect.top;  
	    if (0 == statusHeight){  
	        Class<?> localClass;  
	        try {  
	            localClass = Class.forName("com.android.internal.R$dimen");  
	            Object localObject = localClass.newInstance();  
	            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
	            statusHeight = activity.getResources().getDimensionPixelSize(i5);  
	        } catch (ClassNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        } catch (InstantiationException e) {  
	            e.printStackTrace();  
	        } catch (NumberFormatException e) {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (SecurityException e) {  
	            e.printStackTrace();  
	        } catch (NoSuchFieldException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return statusHeight;  
	} 
}

