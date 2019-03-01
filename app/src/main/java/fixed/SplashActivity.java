package fixed;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

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
import util.ReCommit;
import util.SharedPreferencesUtil;
import welcome.IntroActivity;

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
        setContentView(R.layout.splash_layout);

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
                        update.setForced(updateInfo.isForced());
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(updateInfo.isIgnore_able());
                        return update;
                    }
                })
                .setFileCreator(new CustomApkFileCreator())
                .setUpdateStrategy(new AllDialogShowStrategy())
                .setDownloadWorker(OkhttpDownloadWorker.class)
                .setCheckCallback(new ToastCallback(this));




        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);

        if (isFirstOpen) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
            return;
        }



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//
//            }, 500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences perf = getSharedPreferences("returnid", MODE_PRIVATE);
                ReCommit.netReCommit(MyApplication.getContext(),perf.getInt("id",0));
                Intent intent = new Intent(MyApplication.getContext(),MainActivity.class);
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                //finish();
                try {
                    Thread.sleep(1025);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }).start();












    }



}
