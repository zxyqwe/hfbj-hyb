//package com.hfbj.hyb.test;
//
//import java.util.EnumMap;
//import java.util.EnumSet;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.Vector;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.LuminanceSource;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.PlanarYUVLuminanceSource;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//import com.hfbj.hyb.R;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class testCamera extends Activity {
//	/** Called when the activity is first created. */
//	private static final String TAG = "testCamera";
//	private SurfaceView sfvCamera;
//	private SFHCamera sfhCamera;
//	private View centerView;
//	private TextView txtScanResult;
//	private ImageView imgView;
//	private Timer mTimer;
//	private MyTimerTask mTimerTask;
//	private SharedPreferences sp;
//	private Toast toast = null;
//	// 按照标准HVGA
//	final static int width = 480;
//	final static int height = 320;
//	int dstLeft, dstTop, dstWidth, dstHeight;
//	private MultiFormatReader reader;
//	private Map<DecodeHintType, Object> hints;
//	private Rect framingRect;
//	private Rect framingRectInPreview;
//
//	private static final int MIN_FRAME_WIDTH = 240;
//	private static final int MIN_FRAME_HEIGHT = 240;
//	private static final int MAX_FRAME_WIDTH = 600;
//	private static final int MAX_FRAME_HEIGHT = 400;
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		Window window = getWindow();
//		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		setContentView(R.layout.main);
//		centerView = (View) this.findViewById(R.id.centerView);
//		sfvCamera = (SurfaceView) this.findViewById(R.id.sfvCamera);
//		// sfhCamera = new SFHCamera(sfvCamera.getHolder(), width, height,
//		sfhCamera = new SFHCamera(sfvCamera.getHolder(), getWindowManager()
//				.getDefaultDisplay().getWidth(), getWindowManager()
//				.getDefaultDisplay().getHeight(), previewCallback);
//		txtScanResult = (TextView) this.findViewById(R.id.txtScanResult);
//		imgView = (ImageView) this.findViewById(R.id.ImageView01);
//		sp = getSharedPreferences("ZXY_MDB", 0);
//		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
//		hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
//		decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
//		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
//		reader = new MultiFormatReader();
//		reader.setHints(hints);
//		// 初始化定时器
//		mTimer = new Timer();
//		mTimerTask = new MyTimerTask();
//		mTimer.schedule(mTimerTask, 0, 500);
//	}
//
//	class MyTimerTask extends TimerTask {
//		private int acu = 0;
//
//		@SuppressWarnings("deprecation")
//		@Override
//		public void run() {
//			if(!sfhCamera.isOK)return;
//			acu++;
//			if (dstLeft == 0) {// 只赋值一次
//				dstLeft = centerView.getLeft() * width
//						/ getWindowManager().getDefaultDisplay().getWidth();
//				dstTop = centerView.getTop() * height
//						/ getWindowManager().getDefaultDisplay().getHeight();
//				dstWidth = (centerView.getRight() - centerView.getLeft())
//						* width
//						/ getWindowManager().getDefaultDisplay().getWidth();
//				dstHeight = (centerView.getBottom() - centerView.getTop())
//						* height
//						/ getWindowManager().getDefaultDisplay().getHeight();
//			}
//			sfhCamera.justshot();
//			if (acu % 6 == 0)
//				sfhCamera.AutoFocusAndPreviewCallback();
//		}
//	}
//
//	private static Bitmap toBitmap(LuminanceSource source, int[] pixels) {
//		int width = source.getWidth();
//		int height = source.getHeight();
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
//
//	/**
//	 * 自动对焦后输出图片
//	 */
//	private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
//		@Override
//		public void onPreviewFrame(byte[] data, Camera arg1) {
//			// 取得指定范围的帧的数据
//			// PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
//			// data, width, height, dstLeft, dstTop, dstWidth, dstHeight,
//			// false);
//			Point cameraResolution = sfhCamera.getCameraResolution();
//			PlanarYUVLuminanceSource source = buildLuminanceSource(data,
//					cameraResolution.x, cameraResolution.y);
//			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//			Bitmap grayscaleBitmap = toBitmap(source,
//					source.renderCroppedGreyscaleBitmap());
//			imgView.setImageBitmap(grayscaleBitmap);
//			try {
//				Result result = reader.decodeWithState(bitmap);
//				String sss = result.getText();
//				reader.reset();
//				String s2 = sp.getString(sss, null);
//				if (s2 == null) {
//					txtScanResult.setText(sss);
//					showAlert(sss);
//				} else {
//					txtScanResult.setText(s2);
//					SharedPreferences.Editor editor = sp.edit();
//					editor.putString("str", sss);
//					editor.putInt("state", 0);
//					// editor.putString("time"+sss,
//					// DateFormat.getDateTimeInstance().format(new Date()));
//					// editor.putInt("state"+sss, 1);
//					editor.commit();
//					// arg1.setPreviewCallback(null);
//					// arg1.autoFocus(null);
//					// arg1.stopPreview();
//					mTimer.cancel();
//					showAlert(s2);
//					finish();
//				}
//			} catch (Exception e) {
//				// txtScanResult.setText("Scanning");
//				showAlert(e.toString());
//			}
//		}
//	};
//
//	/**
//	 * Calculates the framing rect which the UI should draw to show the user
//	 * where to place the barcode. This target helps with alignment as well as
//	 * forces the user to hold the device far enough away to ensure the image
//	 * will be in focus.
//	 *
//	 * @return The rectangle to draw on screen in window coordinates.
//	 */
//	public Rect getFramingRect() {
//		if (framingRect == null) {
//			if (sfhCamera.mCamera == null) {
//				return null;
//			}
//			Point screenResolution = sfhCamera.getScreenResolution();
//			if (screenResolution == null) {
//				// Called early, before init even finished
//				return null;
//			}
//			int width = screenResolution.x * 3 / 4;
//			if (width < MIN_FRAME_WIDTH) {
//				width = MIN_FRAME_WIDTH;
//			} else if (width > MAX_FRAME_WIDTH) {
//				width = MAX_FRAME_WIDTH;
//			}
//			int height = screenResolution.y * 3 / 4;
//			if (height < MIN_FRAME_HEIGHT) {
//				height = MIN_FRAME_HEIGHT;
//			} else if (height > MAX_FRAME_HEIGHT) {
//				height = MAX_FRAME_HEIGHT;
//			}
//			int leftOffset = (screenResolution.x - width) / 2;
//			int topOffset = (screenResolution.y - height) / 2;
//			framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
//					topOffset + height);
//			Log.d(TAG, "Calculated framing rect: " + framingRect);
//		}
//		return framingRect;
//	}
//
//	/**
//	 * Like {@link #getFramingRect} but coordinates are in terms of the preview
//	 * frame, not UI / screen.
//	 */
//	public Rect getFramingRectInPreview() {
//		if (framingRectInPreview == null) {
//			Rect framingRect = getFramingRect();
//			if (framingRect == null) {
//				return null;
//			}
//			Rect rect = new Rect(framingRect);
//			Point cameraResolution = sfhCamera.getCameraResolution();
//			Point screenResolution = sfhCamera.getScreenResolution();
//			if (cameraResolution == null || screenResolution == null) {
//				// Called early, before init even finished
//				return null;
//			}
//			rect.left = rect.left * cameraResolution.x / screenResolution.x;
//			rect.right = rect.right * cameraResolution.x / screenResolution.x;
//			rect.top = rect.top * cameraResolution.y / screenResolution.y;
//			rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
//			framingRectInPreview = rect;
//		}
//		return framingRectInPreview;
//	}
//
//	/**
//	 * A factory method to build the appropriate LuminanceSource object based on
//	 * the format of the preview buffers, as described by Camera.Parameters.
//	 *
//	 * @param data
//	 *            A preview frame.
//	 * @param width
//	 *            The width of the image.
//	 * @param height
//	 *            The height of the image.
//	 * @return A PlanarYUVLuminanceSource instance.
//	 */
//	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
//			int width, int height) {
//		Rect rect = getFramingRectInPreview();
//		if (rect == null) {
//			return null;
//		}
//		// Go ahead and assume it's YUV rather than die.
//		return new PlanarYUVLuminanceSource(data, width, height, rect.left,
//				rect.top, rect.width(), rect.height(), false);
//	}
//
//	protected void showAlert(String s2) {
//		if (toast == null) {
//			toast = Toast.makeText(this, s2, Toast.LENGTH_SHORT);
//		} else {
//			toast.setText(s2);
//		}
//		toast.show();
//
//	}
//}