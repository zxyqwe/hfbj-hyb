package com.hfbj.hyb.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.hfbj.hyb.R;
import com.hfbj.hyb.adapter.SimpleBaseAdapter;
import com.hfbj.hyb.adapter.ViewHolder;
import com.hfbj.hyb.app.AppConfig;
import com.hfbj.hyb.app.AppContext;
import com.hfbj.hyb.app.AppLogger;
import com.hfbj.hyb.bean.MemberBean;
import com.hfbj.hyb.bean.PayBean;
import com.hfbj.hyb.net.GsonRequest;
import com.hfbj.hyb.net.UrlManager;
import com.hfbj.hyb.ui.sweetalert.SweetAlertDialog;
import com.hfbj.hyb.ui.sweetalert.SweetDialog;
import com.hfbj.hyb.ui.sweetalert.SweetLoadingDialog;

import java.util.ArrayList;
import java.util.List;

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
    private TextView mHeaderView;

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
        AppLogger.e("wwhy  onResume  ***************");

        qrCodeReader.getCameraManager().startPreview();
       // qrCodeReader.setOnQRCodeReadListener(QRCodeScanActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLogger.e("wwhy  onPause  ***************");
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

        switch (v.getId()) {
        }
    }


    /**
     * 签到
     */
    public void signIn(final String content) {

        String numberId = AppConfig.getNumberId(content);

        //未查到对应信息，二维码错误
        if ("".equals(numberId)) {
            showErrorNotify(R.string.sign_faile, R.string.sign_faile_msg);
            return;
        }

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("WTF !!!!!!").setMessage("被气死了。。。");
//        builder.create().show();


//        Intent intent = new Intent(this,TestActivity.class);
//        startActivity(intent);

        loadMemberInfo(numberId,AppConfig.getNumber(content));
    }


    /**
     * 加载用户信息
     * @param id
     */
    public void loadMemberInfo(String id,final String memberNum) {
        final Dialog mLoading = new SweetLoadingDialog(this, R.string.signing);
        mLoading.show();
        executeRequest(new GsonRequest<MemberBean>(Request.Method.POST, UrlManager.formatUrl(UrlManager.INFO, id), MemberBean.class, null, new Response.Listener<MemberBean>() {
            @Override
            public void onResponse(final MemberBean value) {
                mLoading.dismiss();

                //签到成功，加载信息成功，可以缴纳会费
                final SweetAlertDialog dia = showSuccNotify(R.string.sign_succ, "");
                dia.findViewById(R.id.cancel_button).setVisibility(View.VISIBLE);
                dia.setConfirmText(getString(R.string.pay));
                dia.findViewById(R.id.user_info).setVisibility(View.VISIBLE);
                dia.findViewById(R.id.content_text).setVisibility(View.GONE);

                dia.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dia.dismiss();
                        qrCodeReader.setOnQRCodeReadListener(null);
                        showListViewDialog(value);
                    }
                });

              //  Button mConfirmBtn = ((Button) dia.findViewById(R.id.confirm_button));
              //  mConfirmBtn.setText(R.string.pay);
               // mConfirmBtn.setTag(value);

                ((TextView) dia.findViewById(R.id.tieba_id)).setText(value.getTieba_id());
                ((TextView) dia.findViewById(R.id.member_id)).setText(value.getUnique_name());
                ((TextView) dia.findViewById(R.id.member_fee)).setText(Html.fromHtml(getString(R.string.pay_line, value.getPayLine())));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoading.dismiss();

                //签到成功, 加载信息失败，不能缴纳会费
                final SweetAlertDialog dia = showSuccNotify(R.string.sign_succ, memberNum);
            }
        }));
    }


    /**
     * 缴纳会费
     * @param bean
     * @param year  缴纳年份
     */
    public void payment(final MemberBean bean,final int year){
        final Dialog mLoading = new SweetLoadingDialog(this, R.string.paying);
        mLoading.show();
        executeRequest(new GsonRequest<PayBean>(Request.Method.GET,UrlManager.getUrl(UrlManager.PAY, String.valueOf(year),bean.getId()),PayBean.class,null,new Response.Listener<PayBean>() {
            @Override
            public void onResponse(PayBean value) {
                mLoading.dismiss();

                if(value != null){
                    final SweetAlertDialog dia = showSuccNotify(R.string.pay_succ, getString(R.string.pay_succ_msg));
                }else{
                    payFailedHandler(bean,year);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoading.dismiss();

                payFailedHandler(bean,year);
            }
        }));
    }


    /**
     * 缴费失败处理
     * @param bean
     * @param year
     */
    public void payFailedHandler(final MemberBean bean,final int year){
        final SweetAlertDialog dialog = showErrorNotify(R.string.pay_failed, R.string.pay_fiale_msg);
        dialog.findViewById(R.id.cancel_button).setVisibility(View.VISIBLE);
        dialog.setConfirmText(getString(R.string.retry));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
                qrCodeReader.setOnQRCodeReadListener(null);

                bean.setId("270");
                //重试
                payment(bean,year);
            }
        });
    }


    /**
     * 显示会费缴纳年份选择框
     * @param bean
     * @return
     */
    public Dialog showListViewDialog(final MemberBean bean) {
        final SweetDialog dialog = new SweetDialog(this);
        dialog.setOnDismissListener(mDismissListener);
        dialog.setContentView(R.layout.pay_selected);
        final ListView lv = (ListView) dialog.findViewById(R.id.pay_lv);
        lv.addFooterView(View.inflate(this,R.layout.split_line,null));

        View view = View.inflate(this, R.layout.checked_text, null);
        mHeaderView = (TextView) view.findViewById(R.id.check_tv);
        mHeaderView.setGravity(Gravity.CENTER);
        lv.addHeaderView(view);
        mHeaderView.setText(Html.fromHtml(getString(R.string.pay_count, "0", "0")));

        int year = Integer.valueOf(bean.getPayLine());
        List<String> data = new ArrayList<String>();
        for (int i = 1; i < 6; i++) {
            data.add(String.valueOf(year + i));
        }

        final MyAdapter mAdapter = new MyAdapter(this, data);
        lv.setAdapter(mAdapter);
        lv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.checked(position);
                mHeaderView.setText(Html.fromHtml(getString(R.string.pay_count, String.valueOf(mAdapter.getPos()), String.valueOf(mAdapter.getPos() * AppConfig.FEE))));
            }
        });

        dialog.findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.confirm_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mAdapter.getPos();
                if(year < 1){
                    Toast.makeText(QRCodeScanActivity.this,"缴费年份不能为0",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                qrCodeReader.setOnQRCodeReadListener(null);

                bean.setId("-1");
                payment(bean,mAdapter.getPos());
            }
        });

        dialog.show();
        return dialog;
    }


    /**
     * 操作失败提示
     *
     * @param titleResId
     * @param contentResId
     */
    public SweetAlertDialog showErrorNotify(int titleResId, int contentResId) {
        return showNotify(SweetAlertDialog.ERROR_TYPE, titleResId, contentResId);
    }

    /**
     * 操作成功提示
     *
     * @param titleResId
     * @param content
     */
    public SweetAlertDialog showSuccNotify(int titleResId, String content) {
        return showNotify(SweetAlertDialog.SUCCESS_TYPE, getString(titleResId), content);
    }


    /**
     * 操作结果提示
     */
    public SweetAlertDialog showNotify(int type, int titleResId, int contentResId) {
        return showNotify(type,getString(titleResId),getString(contentResId));
    }

    /**
     * 操作结果提示
     */
    public SweetAlertDialog showNotify(int type, String title, String content) {
        SweetAlertDialog mDialog = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        mDialog.show();
        mDialog.setOnDismissListener(mDismissListener);
        return mDialog;
    }

    private OnDismissListener mDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            //qrCodeReader.getCameraManager().startPreview();
            qrCodeReader.setOnQRCodeReadListener(QRCodeScanActivity.this);
        }
    };


    /**
     * 缴纳会费年份选择适配
     */
    public class MyAdapter extends SimpleBaseAdapter<String> {

        private int pos = -1;

        public MyAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewHolder holder) {

            TextView ct = holder.getView(R.id.check_tv);
            ct.setText(data.get(position));

            ImageView imageView = holder.getView(R.id.icon);
            if(position < pos){
                imageView.setImageResource(R.drawable.check_on);
            }else{
                imageView.setImageResource(R.drawable.chek_off);
            }
            return convertView;
        }

        public void checked(int position) {
            if (pos < position) {
                pos = position;
            } else {
                pos = position - 1;
            }

            notifyDataSetChanged();
        }

        public int getPos() {
            return pos;
        }


        @Override
        public int getItemResource() {
            return R.layout.checked_text;
        }
    }


}
