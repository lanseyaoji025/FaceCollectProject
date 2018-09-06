package com.shuding.fastnetworklibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuding.fastnetworklibrary.R;


/**
 * @ClassName : LoadingDialog
 * @Description : 自定义等待对话框
 */
public class LoadingDialog extends Dialog implements DialogInterface.OnDismissListener{
    Context context;
    ImageView image; // 圆型进度条
    Animation anim;// 动画
    TextView progress;
    TextView prompt;
    LoadindDialogDismiss loadindDialogDismiss;

    public  interface LoadindDialogDismiss{
        void HandDismissEvent();
    }

    public LoadingDialog(Context context, LoadindDialogDismiss loadindDialogDismiss) {
        super(context, R.style.dialog_trans);
        this.context = context;
        this.loadindDialogDismiss=loadindDialogDismiss;
    }


    public LoadingDialog(Context context){
        super(context, R.style.dialog_trans);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_toast);

        setAnimation();
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    protected void setAnimation() {
        anim = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading);
        anim.setInterpolator(new LinearInterpolator());

        image = (ImageView) this.findViewById(R.id.dialog_loading_animation);
        progress = (TextView) this.findViewById(R.id.dialog_loading_progress);
        prompt = (TextView) this.findViewById(R.id.dialog_loading_prompt);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按下了键盘上返回按钮
            this.dismiss();
            return true;
        }
        return false;
    }

    public void show(String content) {
        super.show();
        if (content == null || content.isEmpty()) {
            prompt.setText("加载中");
        } else {
            prompt.setText(content);
        }
        image.startAnimation(anim);
    }

    public void Dismiss(){
        if (isShowing()){
            dismiss();
            cancel();
        }
    }

    /**
     * 在这个地方处理进度条退出逻辑
     * @param dialog
     */

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (loadindDialogDismiss!=null){
            loadindDialogDismiss.HandDismissEvent();
        }
    }
}
