package fixed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import context.MyApplication;
import liyulong.com.fixed.R;
import okhttp3.Call;
import okhttp3.Response;
import util.HttpUtil;

public class SplashActivity extends BaseActivity {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

//        getSupportActionBar().hide();
        setContentView(R.layout.splash_layout);

//        ImageView bingPicImg = findViewById(R.id.bing_pic_img);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String bingPic = prefs.getString("bing_pic",null);
//
//        if (bingPic != null) {
//            Glide.with(SplashActivity.this).load(bingPic).into(bingPicImg);
//        }

        new Thread(){
            @Override
            public void run() {

                try {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    sleep(1500);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();




    }



}
