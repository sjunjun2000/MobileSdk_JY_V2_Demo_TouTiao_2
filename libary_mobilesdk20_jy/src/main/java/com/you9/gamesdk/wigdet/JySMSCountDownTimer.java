package com.you9.gamesdk.wigdet;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.you9.gamesdk.R;
import com.you9.gamesdk.util.ResourceUtil;


public class JySMSCountDownTimer extends CountDownTimer {

    private TextView sendTextView;
    private Context context;

    public JySMSCountDownTimer(long millisInFuture, long countDownInterval,
                               Context context, TextView sendTextView) {
        super(millisInFuture, countDownInterval);
        // TODO Auto-generated constructor stub
        this.sendTextView = sendTextView;
        this.context = context;
    }

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub
        sendTextView.setEnabled(true);
        sendTextView.setText(context
                .getString(ResourceUtil.getStringId(context, "jy_activity_sms_verify_resend")));
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // TODO Auto-generated method stub
        sendTextView.setEnabled(false);
        sendTextView.setText(millisUntilFinished / 1000 + "S");
    }

}
