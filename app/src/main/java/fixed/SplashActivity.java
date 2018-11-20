package fixed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.splash_layout);

        ImageView bingPicImg = findViewById(R.id.bing_pic_img);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null) {
            Glide.with(SplashActivity.this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

        new Thread(){
            @Override
            public void run() {

                try {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    sleep(3000);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();




    }


    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new okhttp3.Callback() {

            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
//                mainActivityH.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(getContext()).load(bingPic).into(bingPicImg);
//                    }
//                });
            }


            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }
}
