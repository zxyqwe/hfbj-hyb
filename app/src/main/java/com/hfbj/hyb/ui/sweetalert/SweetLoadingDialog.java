package com.hfbj.hyb.ui.sweetalert;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;


import com.hfbj.hyb.R;
import com.hfbj.hyb.ui.View.JumpingBeans;


public class SweetLoadingDialog extends Dialog {
    private View mDialogView;
    private AnimationSet mScaleInAnim;
    private AnimationSet mScaleOutAnim;
    private int resId;


    public SweetLoadingDialog(Context context, int resId) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        this.resId = resId;

        mScaleInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.dialog_scale_in);
        mScaleOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.dialog_scale_out);
        mScaleOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        SweetLoadingDialog.super.dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void onStart() {
        mDialogView.startAnimation(mScaleInAnim);
    }

    public void dismiss() {
        mDialogView.startAnimation(mScaleOutAnim);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        TextView textView = (TextView) mDialogView.findViewById(R.id.loading_text);
        textView.setText(Html.fromHtml(getContext().getResources().getString(resId)));
        JumpingBeans jumpingBeans = new JumpingBeans.Builder()
                .appendJumpingDots(textView)
                .build();
    }

}
