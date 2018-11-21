package fixed;

import android.Manifest;
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
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;


import java.io.IOException;

import liyulong.com.fixed.R;
import fragment.*;
import okhttp3.Call;
import okhttp3.Response;
import util.HttpUtil;


public class MainActivity extends BaseActivity {

    Fragment homeActivity = new HomeActivity();
    Fragment appointmentActivity = new AppointmentActivity();
    Fragment aboutActivity = new AboutActivity();
    AppointmentActivity appointmentActivityB;
    private Toolbar toolbar;
    HomeActivity homeF = new HomeActivity();


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";


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

        replaceFragment(homeActivity);
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.ic_home);
        toolbar.setTitle("    主页");
        setSupportActionBar(toolbar);
        appointmentActivityB = (AppointmentActivity) getSupportFragmentManager().findFragmentById(R.id.frameLyout_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
//                    != PackageManager.PERMISSION_GRANTED
                    ) {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setMessage("需要一些权限，点击确定开始授权")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })

                        .setCancelable(false)
                        .show();
            }


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
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                    showToast(this, "无法获取所需的权限, 请到系统设置开启");
                    return;
                }

                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        showToast(this, "无法获取所需的权限, 请到系统设置开启");
                        return;
                    }
                }

            }
        }
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setLogo(R.drawable.ic_home);
                    toolbar.setTitle("    主页");
                    replaceFragment(homeActivity);



                    return true;
                case R.id.navigation_appointment:
                    toolbar.setLogo(R.drawable.ic_time_circle);
                    toolbar.setTitle("    预约");
                    replaceFragment(appointmentActivity);


                    return true;
                case R.id.navigation_about:
                    toolbar.setLogo(R.drawable.ic_info_circle);
                    toolbar.setTitle("    关于");
                    replaceFragment(aboutActivity);

                    return true;
            }
            return false;
        }
    };








//    public void sendSMS(String phoneNumber,String message){
//
//
//
//
//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED){
//
//            Toast.makeText(this,"无法获取到短信权限，需要手动点发送",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
//            intent.putExtra("sms_body", message);
//
//            startActivity(intent);
//
//        } else {
//            //处理返回的发送状态
//            String SENT_SMS_ACTION = "SENT_SMS_ACTION";
//            Intent sentIntent = new Intent(SENT_SMS_ACTION);
//            PendingIntent sendIntent= PendingIntent.getBroadcast(this, 0, sentIntent,
//                    0);
//// register the Broadcast Receivers
//            this.registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context _context, Intent _intent) {
//                    switch (getResultCode()) {
//                        case Activity.RESULT_OK:
//
//                            showToast(MainActivity.this,"成功向技术人员提交消息！正在检测是否接收，不要关闭应用");
//                            break;
//                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        case SmsManager.RESULT_ERROR_NULL_PDU:
//
////                            buttonCommit.loadingFailed();
//
//                            new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
//                                    .setMessage("我也不知道啥情况，反正出错了，检查下吧")
//                                    .setNegativeButton("好的", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    })
//                                    .show();
//
////                            Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
//                            break;
//                    }
//                }
//            }, new IntentFilter(SENT_SMS_ACTION));
//            //处理返回的接收状态
//            String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
//// create the deilverIntent parameter
//            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
//            PendingIntent backIntent= PendingIntent.getBroadcast(this, 0,
//                    deliverIntent, 0);
//            this.registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context _context, Intent _intent) {
//
////                    buttonCommit.loadingSuccessful();
//
//
////                    Toast.makeText(MainActivity.this,
////                            "", Toast.LENGTH_SHORT)
////                            .show();
//
//                    new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
//                            .setMessage("技术人员会在两小时内联系您，请耐性等候")
//                            .setNegativeButton("好的", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .show();
//
//                }
//            }, new IntentFilter(DELIVERED_SMS_ACTION));
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber,null,message,sendIntent,backIntent);
//        }
//
//    }















//    public static boolean isMIUI() {
//        String device = Build.MANUFACTURER;
////        LogUtils.v("Build.MANUFACTURER = " + device);
//        if (device.equals("Xiaomi")) {
//            Properties prop = new Properties();
//            try {
//                prop.load(new FileInputStream(new File(Environment
//                        .getRootDirectory(), "build.prop")));
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
//                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
//                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        } else {
//            return false;
//        }
//    }




    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
        transaction.replace(R.id.frameLyout_main,fragment);
        transaction.commit();
    }
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
//    public static void showAlert(Context ctx, String info) {
//        showAlert(ctx, info, null);
//    }
//
//    public static void showAlert(Context ctx, String info, AdapterView.OnItemSelectedListener onItemSelectedListener) {
//        new AlertDialog.Builder(ctx)
//                .setMessage(info)
//                .setNegativeButton("取消",null)
//                .setPositiveButton("确定", null)
////                .setOnDismissListener(onDismiss)
//                .setOnItemSelectedListener(onItemSelectedListener)
//                .show();
//    }


}
