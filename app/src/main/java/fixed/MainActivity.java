package fixed;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;


import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import context.MyApplication;
import es.dmoral.toasty.Toasty;
import liyulong.com.fixed.R;
import fragment.*;
import update.ClickUpdateToastCallback;
import util.getVersion;


public class MainActivity extends BaseActivity {


    Fragment appointmentActivity = new AppointmentActivity();
    Fragment about = new AboutActivity();
    Fragment orderActivity = new OrderActivity();
    private Fragment currentFragment ;


    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    UpdateBuilder autoUpdate = UpdateBuilder.create();

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
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);

//
//        //开启应用统计功能
        if (!getVersion.isApkInDebug(MyApplication.getContext())){
            StatConfig.setDebugEnable(false);
            StatService.registerActivityLifecycleCallbacks(this.getApplication());
        }


//        replaceFragment(appointmentActivity);
        initFragment();
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.ic_time_circle);
        toolbar.setTitle("    预约");
        setSupportActionBar(toolbar);

        //**的设置。。。
//        SharedPreferences perf1 = getSharedPreferences("returnid",MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_appointment:
                        replaceFragment(appointmentActivity);
                        toolbar.setLogo(R.drawable.ic_time_circle);
                        toolbar.setTitle("    预约");
//                        if (perf1.getInt("id",0) != 0){
//                            changeToolbar(String.valueOf(perf1.getInt("id",0)));
//                        }
                        break;
                    case R.id.navigation_about:
                        replaceFragment(about);
                        toolbar.setTitle("    关于");
                        toolbar.setLogo(R.drawable.ic_info_circle);
                        break;
                    case R.id.navigation_order:
                        replaceFragment(orderActivity);
                        toolbar.setTitle("    订单");
                        toolbar.setLogo(R.drawable.ic_order);
                        break;
                }
                return true;
            }
        });




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
            autoUpdate.checkWithDaemon(30);
        }








    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoUpdate.stopDaemon();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences perf = getSharedPreferences("returnid", MODE_PRIVATE);
        if (perf.getInt("id",0) != 0){
            changeToolbar(String.valueOf(perf.getInt("id",0)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.auto_update:
                UpdateBuilder.create().setCheckCallback(new ClickUpdateToastCallback()).check();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
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
//                            Manifest.permission.SEND_SMS,
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



    private void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLyout_main,appointmentActivity).commit();
//        transaction.add(R.id.frameLyout_main,orderActivity).hide(orderActivity).commit();
//        transaction.add(R.id.frameLyout_main,about).hide(about).commit();
        currentFragment = appointmentActivity;
    }





    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.addToBackStack(null);

        if (!fragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frameLyout_main, fragment)
                    .commit();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragment)
                    .commit();
        }
        currentFragment = fragment;
    }

    public void changeToolbar(String id){
        if (Integer.valueOf(id) == 0){
            toolbar.setSubtitle("");
            return;
        }
        toolbar.setSubtitle("    已预约,订单号: "+id);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.red));
    }

    public void closeToolbarState(){
        toolbar.setSubtitle("");
        toolbar.refreshDrawableState();
    }


}
