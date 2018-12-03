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
import com.google.gson.Gson;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.IOException;

import context.MyApplication;
import liyulong.com.fixed.R;
import okhttp3.Call;
import okhttp3.Response;
import update.*;
import util.HttpUtil;

public class SplashActivity extends BaseActivity {
    String url = "http://lqwqb.ml/update.json";
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
        UpdateConfig.getConfig()
                .setUrl(url)// 配置检查更新的API接口
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        Gson gson = new Gson();
                        UpdateInfo updateInfo = gson.fromJson(response,UpdateInfo.class);
                        Update update = new Update();
                        // 此apk包的下载地址
                        update.setUpdateUrl(updateInfo.getUpdate_url());
                        // 此apk包的版本号
                        update.setVersionCode(updateInfo.getUpdate_ver_code());
                        // 此apk包的更新内容
                        update.setUpdateContent(updateInfo.getUpdate_content());
                        // 此apk包是否为强制更新
                        update.setVersionName(updateInfo.getUpdate_ver_name());
                        update.setMd5(updateInfo.getMd5());
                        update.setForced(updateInfo.isIgnore_able());
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(false);
                        return update;
                    }
                })
                .setFileCreator(new CustomApkFileCreator())
                .setUpdateStrategy(new AllDialogShowStrategy())
                .setDownloadWorker(OkhttpDownloadWorker.class)
                .setCheckCallback(new ToastCallback(this));

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
