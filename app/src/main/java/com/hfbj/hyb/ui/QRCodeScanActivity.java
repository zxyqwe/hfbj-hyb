package com.hfbj.hyb.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.hfbj.hyb.R;
import com.hfbj.hyb.app.AppLogger;
import com.hfbj.hyb.ui.sweetalert.SweetAlertDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 二维码扫描界面
 * PS： 扫描完成后使用stopPreview停止， 用startPreview有很大的几率开启不了，不能重新扫描，原因待查，
 * 所以采用在扫描完成后去除扫描监听，不过后台依然在扫描，性能欠佳
 *
 * @author LiYanZhao
 * @date 2014-9-28 下午3:29:19
 */
public class QRCodeScanActivity extends BaseActivity implements
        QRCodeReaderView.OnQRCodeReadListener, OnClickListener {

    @InjectView(R.id.qrcode_reader_view)
    QRCodeReaderView qrCodeReader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_scan_lay);
        ButterKnife.inject(this);
        initView();
    }

    /**
     * 初始化视图
     */
    public void initView() {
        qrCodeReader.setOnQRCodeReadListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReader.getCameraManager().startPreview();
        qrCodeReader.setOnQRCodeReadListener(QRCodeScanActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReader.getCameraManager().stopPreview(); // 暂停扫描
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        //AppLogger.d("停止 二维码扫描 ");
        //qrCodeReader.getCameraManager().stopPreview(); // 暂停扫描
        qrCodeReader.setOnQRCodeReadListener(null);

        AppLogger.e(text);
        //loadUserInfo(text);
        signIn(text);
    }

    @Override
    public void cameraNotFound() {
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 签到
     */
    public void signIn(final String reqId) {
//        final Dialog mLoadingDialog = DialogHelper.showDialog(AppContext.getInstance(), R.string.signing);
//        mLoadingDialog.show();
//
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("access_token", AppConfig.getLoginInfo().getAccessToken());
//        executeRequest(new GsonRequest<SignBean>(UrlHelper.getSignInUrl(reqId), Method.GET, SignBean.class, params, new Listener<SignBean>() {
//            @Override
//            public void onResponse(SignBean bean) {
//                mLoadingDialog.dismiss();
//
//                if (bean.isConfirmed()) {
//                    Toast.makeText(getBaseContext(), R.string.sign_succ, Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(QRCodeScanActivity.this, UserInfoActivity.class);
//                    intent.putExtra("reqId", reqId);
//                    startActivity(intent);
//                }
//            }
//        }, new ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error != null && error.networkResponse.statusCode == HttpStatus.SC_FORBIDDEN)
//                    showNotify(R.string.user_no);
//                else
//                    showNotify(R.string.sign_faile_msg);
//
//                mLoadingDialog.dismiss();
//            }
//        }));
    }

    public void showNotify(int StringID) {
        SweetAlertDialog mDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(R.string.sign_faile)
                .setContentText(StringID);
        mDialog.show();

      //  Dialog dia = DialogHelper.showSureDialog(this, StringID);
        mDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //AppLogger.d("重新开启 二维码扫描 ");
                qrCodeReader.setOnQRCodeReadListener(QRCodeScanActivity.this);
            }
        });
    }

}
