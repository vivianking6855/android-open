package com.learn.blockmonitor.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.widget.TextView;

import com.learn.blockmonitor.R;
import com.learn.blockmonitor.util.Const;

/**
 * Created by Venjee_Shen on 2018/3/27.
 *
 */

public class StackLogDetailActivity extends AppCompatActivity {

    private TextView content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stacklog_detail_page);
        content = findViewById(R.id.detail_content);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            content.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
        }
        showLogFromIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showLogFromIntent(getIntent());
    }

    private void showLogFromIntent(Intent intent){
        if(intent != null) {
            String log = intent.getStringExtra(Const.KEY_DETAIL_LOG);
            if(log!=null && !log.isEmpty()) {
                content.setText(log);
            }
        }
    }
}
