package com.travel.driveassistant.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.travel.driveassistant.R;
import com.travel.driveassistant.events.PermissionGrantedEvent;
import com.travel.driveassistant.utils.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashActivity extends AppCompatActivity {
    private boolean isNextActivityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startApp();
            }
        }, 2000);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startApp();
    }

    public void startApp() {
        if (PermissionUtil.checkPermissionsGranted(this, PermissionUtil.CORE_APP_PERMISSIONS)) {
            startNextActivity();
        } else {
            PermissionUtil.requestAppCorePermissions(this);
        }
    }

    public void startNextActivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                finish();
                if (!isNextActivityStarted) {
                    isNextActivityStarted = true;
                    final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionUtil.onRequestCorePermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PermissionGrantedEvent event) {
        startApp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
