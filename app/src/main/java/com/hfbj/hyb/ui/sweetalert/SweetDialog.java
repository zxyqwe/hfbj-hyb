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


public class SweetDialog extends Dialog {
    private View mDialogView;
    private AnimationSet mScaleInAnim;
    private AnimationSet mScaleOutAnim;
    private int resId;
    private OnSweetDismissed mDismissed;



    public SweetDialog(Context context) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        mScaleInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.dialog_scale_in);
        mScaleOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.dialog_scale_out);
        mScaleOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(mDismissed != null)
                    mDismissed.onDismissed();

                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        SweetDialog.super.dismiss();
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


    public void setOnSweetDismissed(OnSweetDismissed mDismissed){
        this.mDismissed = mDismissed;
    }


    public static interface OnSweetDismissed {
        public void onDismissed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
    }


}
