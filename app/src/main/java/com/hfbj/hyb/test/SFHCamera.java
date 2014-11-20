//package com.hfbj.hyb.test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.hardware.Camera;
//import android.util.Log;
//import android.view.SurfaceHolder;
//
//public class SFHCamera implements SurfaceHolder.Callback {
//	private SurfaceHolder holder = null;
//	Camera mCamera;
//	private int width, height;
//	private Camera.PreviewCallback previewCallback;
//	private Point screenResolution;
//	private Point cameraResolution;
//	private static final String TAG = "SFHCamera";
//	public boolean isOK = true;
//
//	// This is bigger than the size of a small screen, which is still supported.
//	// The routine
//	// below will still select the default (presumably 320x240) size for these.
//	// This prevents
//	// accidental selection of very low resolution on some devices.
//	private static final int MIN_PREVIEW_PIXELS = 470 * 320; // normal screen
//	private static final int MAX_PREVIEW_PIXELS = 1280 * 720;
//
//	@SuppressWarnings("deprecation")
//	public SFHCamera(SurfaceHolder holder, int w, int h,
//			Camera.PreviewCallback previewCallback) {
//		this.holder = holder;
//		this.holder.addCallback(this);
//		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		width = w;
//		height = h;
//		this.previewCallback = previewCallback;
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
//		Camera.Parameters parameters = mCamera.getParameters();
//		// parameters.setPreviewSize(width, height);// 设置尺寸
//		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
//		parameters.setPictureFormat(PixelFormat.JPEG);
//		mCamera.setParameters(parameters);
//		// mCamera.setDisplayOrientation(90);
//		mCamera.startPreview();// 开始预览
//		Log.e("Camera", "surfaceChanged");
//	}
//
//	Point getCameraResolution() {
//		return cameraResolution;
//	}
//
//	Point getScreenResolution() {
//		return screenResolution;
//	}
//
//	/**
//	 * Reads, one time, values from the camera that are needed by the app.
//	 */
//	void initFromCameraParameters(Camera camera) {
//		Camera.Parameters parameters = camera.getParameters();
//		// We're landscape-only, and have apparently seen issues with display
//		// thinking it's portrait
//		// when waking from sleep. If it's not landscape, assume it's mistaken
//		// and reverse them:
//		if (width < height) {
//			Log.i(TAG,
//					"Display reports portrait orientation; assuming this is incorrect");
//			int temp = width;
//			width = height;
//			height = temp;
//		}
//		screenResolution = new Point(width, height);
//		Log.i(TAG, "Screen resolution: " + screenResolution);
//		cameraResolution = findBestPreviewSizeValue(parameters,
//				screenResolution);
//		Log.i(TAG, "Camera resolution: " + cameraResolution);
//	}
//
//	private Point findBestPreviewSizeValue(Camera.Parameters parameters,
//			Point screenResolution) {
//
//		List<Camera.Size> rawSupportedSizes = parameters
//				.getSupportedPreviewSizes();
//		if (rawSupportedSizes == null) {
//			Log.w(TAG,
//					"Device returned no supported preview sizes; using default");
//			Camera.Size defaultSize = parameters.getPreviewSize();
//			return new Point(defaultSize.width, defaultSize.height);
//		}
//
//		// Sort by size, descending
//		List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(
//				rawSupportedSizes);
//		Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
//			@Override
//			public int compare(Camera.Size a, Camera.Size b) {
//				int aPixels = a.height * a.width;
//				int bPixels = b.height * b.width;
//				if (bPixels < aPixels) {
//					return -1;
//				}
//				if (bPixels > aPixels) {
//					return 1;
//				}
//				return 0;
//			}
//		});
//
//		if (Log.isLoggable(TAG, Log.INFO)) {
//			StringBuilder previewSizesString = new StringBuilder();
//			for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
//				previewSizesString.append(supportedPreviewSize.width)
//						.append('x').append(supportedPreviewSize.height)
//						.append(' ');
//			}
//			Log.i(TAG, "Supported preview sizes: " + previewSizesString);
//		}
//
//		Point bestSize = null;
//		float screenAspectRatio = (float) screenResolution.x
//				/ (float) screenResolution.y;
//
//		float diff = Float.POSITIVE_INFINITY;
//		for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
//			int realWidth = supportedPreviewSize.width;
//			int realHeight = supportedPreviewSize.height;
//			int pixels = realWidth * realHeight;
//			if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
//				continue;
//			}
//			boolean isCandidatePortrait = realWidth < realHeight;
//			int maybeFlippedWidth = isCandidatePortrait ? realHeight
//					: realWidth;
//			int maybeFlippedHeight = isCandidatePortrait ? realWidth
//					: realHeight;
//			if (maybeFlippedWidth == screenResolution.x
//					&& maybeFlippedHeight == screenResolution.y) {
//				Point exactPoint = new Point(realWidth, realHeight);
//				Log.i(TAG, "Found preview size exactly matching screen size: "
//						+ exactPoint);
//				return exactPoint;
//			}
//			float aspectRatio = (float) maybeFlippedWidth
//					/ (float) maybeFlippedHeight;
//			float newDiff = Math.abs(aspectRatio - screenAspectRatio);
//			if (newDiff < diff) {
//				bestSize = new Point(realWidth, realHeight);
//				diff = newDiff;
//			}
//		}
//
//		if (bestSize == null) {
//			Camera.Size defaultSize = parameters.getPreviewSize();
//			bestSize = new Point(defaultSize.width, defaultSize.height);
//			Log.i(TAG, "No suitable preview sizes, using default: " + bestSize);
//		}
//
//		Log.i(TAG, "Found best approximate preview size: " + bestSize);
//		return bestSize;
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder arg0) {
//		mCamera = Camera.open();// 启动服务
//		try {
//			initFromCameraParameters(mCamera);
//			mCamera.setPreviewDisplay(holder);// 设置预览
//			isOK = true;
//			Log.e("Camera", "surfaceCreated");
//		} catch (IOException e) {
//			isOK = false;
//			Camera tmp = mCamera;
//			mCamera = null;
//			tmp.release();
//		}
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder arg0) {
//		isOK = false;
//		mCamera.setPreviewCallback(null);
//		mCamera.autoFocus(null);
//		mCamera.stopPreview();// 停止预览
//		Camera tmp = mCamera;
//		mCamera = null;
//		tmp.release();
//		Log.e("Camera", "surfaceDestroyed");
//	}
//
//	public void justshot() {
//		try {
//			if (!isOK)
//				return;
//			if (mCamera != null) {
//				mCamera.setOneShotPreviewCallback(previewCallback);
//			}
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * 自动对焦并回调Camera.PreviewCallback
//	 */
//	public void AutoFocusAndPreviewCallback() {
//		try {
//			if (!isOK)
//				return;
//			if (mCamera != null) {
//				mCamera.autoFocus(mAutoFocusCallBack);
//			}
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * 自动对焦
//	 */
//	private Camera.AutoFocusCallback mAutoFocusCallBack = new Camera.AutoFocusCallback() {
//
//		@Override
//		public void onAutoFocus(boolean success, Camera camera) {
//			try {
//				if (!isOK)
//					return;
//				if (success) { // 对焦成功，回调Camera.PreviewCallback
//					mCamera.setOneShotPreviewCallback(previewCallback);
//				}
//			} catch (Exception e) {
//			}
//		}
//	};
//
//}
