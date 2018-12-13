package fixed;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import liyulong.com.fixed.R;
import fragment.*;
import okhttp3.Call;
import okhttp3.Response;
import util.HttpUtil;
import util.SharedPreferencesUtil;


public class MainActivity extends BaseActivity {


    Fragment appointmentActivity = new AppointmentActivity();


    private Toolbar toolbar;

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("退出应用程序")
                .setMessage("确定要退出吗？我会想你的qwq")
                .setNegativeButton("不退出",null)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StatConfig.setDebugEnable(false);
        StatService.registerActivityLifecycleCallbacks(this.getApplication());


        replaceFragment(appointmentActivity);
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.ic_time_circle);
        toolbar.setTitle("    预约");
        setSupportActionBar(toolbar);



            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED
                    ) {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setMessage("程序需要一些必要的权限才能正常工作，请同意")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })

                        .setCancelable(false)
                        .show();

                } else {

                UpdateBuilder.create().check();

            }














    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_about:
                Intent aboutIntent = new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    //    private void loadBingPic(){
//        String requestBingPic = "http://guolin.tech/api/bing_pic";
//        HttpUtil.sendOkHttpRequest(requestBingPic, new okhttp3.Callback() {
//
//            public void onResponse(Call call, Response response) throws IOException {
//                final String bingPic = response.body().string();
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
//                editor.putString("bing_pic", bingPic);
//                editor.apply();
////                mainActivityH.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Glide.with(getContext()).load(bingPic).into(bingPicImg);
////                    }
////                });
//            }
//
//
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//    }
    /*
    申请所需权限
     */
    public static final int PERMISSIONS_REQUEST_CODE = 1002;

    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
    private void requestPermission() {
        // Here, thisActivity is the current activity


            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            Manifest.permission.READ_PHONE_STATE,
//                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSIONS_REQUEST_CODE);

        }

    /**
     * 权限获取回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {

                // 用户取消了权限弹窗
                if (grantResults.length == 0) {
//                    showToast(this, "无法获取所需的权限, 请到系统设置开启");
                    Toasty.error(this,"无法获取所需的权限, 请到系统设置开启",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
//                        showToast(this, "无法获取所需的权限, 请到系统设置开启");
                        Toasty.error(this,"无法获取所需的权限, 请到系统设置开启",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        }
    }







    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
        transaction.replace(R.id.frameLyout_main,fragment);
        transaction.commit();
    }


}
