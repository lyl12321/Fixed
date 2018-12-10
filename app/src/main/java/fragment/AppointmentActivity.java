package fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dx.dxloadingbutton.lib.LoadingButton;


import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import context.MyApplication;
import es.dmoral.toasty.Toasty;
import fixed.MainActivity;
import liyulong.com.fixed.R;
import util.PhoneNumberMatch;

public class AppointmentActivity extends Fragment {

    private EditText name;
    private EditText phone;
//    private NiceSpinner buildNumber;
//    private NiceSpinner floorNumber;
//    private NiceSpinner sNumber;
    private Button chooseTime;
    private Calendar date;
    private Calendar tDate;
//    private int aYear;
//    private int aMonth;
//    private int aDay;
//    private int aHour;
//    private int aMinute;
    private CheckBox[] checkBoxes;
    private Button choosePosition;

    private LoadingButton buttonCommit;
    MainActivity activity;
    ProgressBar progressBar;
    private String bResult;
    private String phoneNumber;
    private Button choosePeople;
    private String[] servicePeople;
    private int checkedItem = 0;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment,container,false);

        name = view.findViewById(R.id.editText_Name);
        phone = view.findViewById(R.id.editText_Phone);
//        buildNumber = view.findViewById(R.id.niceSpinner_BuildNumber);
//        floorNumber = view.findViewById(R.id.niceSpinner_FloorNumber);
//        sNumber = view.findViewById(R.id.niceSpinner_SNumber);
        chooseTime = view.findViewById(R.id.button_Time);
        date = Calendar.getInstance();
        tDate = Calendar.getInstance();
        tDate.set(2000,4,29,0,0);
        checkBoxes = new CheckBox[5];
        checkBoxes[0] = view.findViewById(R.id.checkBox1);
        checkBoxes[1] = view.findViewById(R.id.checkBox2);
        checkBoxes[2] = view.findViewById(R.id.checkBox3);
        checkBoxes[3] = view.findViewById(R.id.checkBox4);
        checkBoxes[4] = view.findViewById(R.id.checkBox5);
        buttonCommit = view.findViewById(R.id.button_Commit);
        activity = (MainActivity) getActivity();
        choosePosition = view.findViewById(R.id.button_position);
        bResult = "";
        phoneNumber = "13365591802";
        choosePeople = view.findViewById(R.id.button_People);
        servicePeople = new String[]{"lqwq    (李钰龙)","cloverkit    (周广来)"};
//        choosePosition.setText("点我选择位置");

//        progressBar.setIndeterminateDrawable(new DoubleBounce());




        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
//        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
//        initSpinner();
        final int maxS = 20;
        String[] bString = new String[]{"1北","1南","2北","2南","3北","3南","4北","4南","5北","5南"};
        String[] fString = new String[6];
        String[] sString = new String[maxS];

        chooseTime.setOnClickListener(V -> {

            chooseTimeInit();

        });
        buttonCommit.setOnClickListener(V -> {
            buttonCommit.reset();
//            Calendar tempDate = Calendar.getInstance();
//            tempDate.set(aYear,aMonth,aDay,aHour,aMinute);

////           if (ContextCompat.checkSelfPermission(activity,Manifest.permission.SEND_SMS)
////                   != PackageManager.PERMISSION_GRANTED) {
////               ActivityCompat.requestPermissions(activity, new String[]{
////                       Manifest.permission.SEND_SMS,
//////                Manifest.permission.READ_PHONE_STATE,
//////                Manifest.permission.WRITE_EXTERNAL_STORAGE
////               }, MainActivity.PERMISSIONS_REQUEST_CODE);
//
//
//
//           }


            String tempString = "";

            //处理name,phone等等为空的情况
            if (name.getText().length() == 0){
                tempString += "--姓名不能为空" + "\n" ;
            }
            if (!PhoneNumberMatch.isMobileNO(phone.getText().toString())){
                tempString += "--联系方式暂时只支持11位大陆电话" + "\n";
            }

            if (bResult == ""){
                tempString += "--位置不能为空"+"\n";
            }
            if (!(date.before(tDate))){
                tempString += "--预约时间不能小于现在时间"+"\n";
            }
            String tempString1 = "";
            for(int i = 0; i <=4; i++){
                if (checkBoxes[i].isChecked()){
                    tempString1 += checkBoxes[i].getText()+",";
                }
            }
            if (tempString1 == ""){
                tempString += "--您必须选择一项遇到的问题"+"\n";
            }





            if (tempString.length() == 0) {



                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("核对您的信息!")
                        .setMessage(commit())
                        .setNegativeButton("返回修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("ojbk了，提交吧", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                buttonCommit.startLoading();

                                sendSMS("+86"+phoneNumber,commit());


                            }
                        })
                        .setCancelable(false)
                        .show();


//
//               activity.showAlert(getContext(), "核对您的信息:" + "\n" + commit() + "\n" + "是否确认提交", new AdapterView.OnItemSelectedListener() {
//                   @Override
//                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                       if (position == 0){
//
//                       } else {
//
//                       }
//                   }
//
//                   @Override
//                   public void onNothingSelected(AdapterView<?> parent) {
//
//                   }
//               });
            } else {

                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("您的输入有问题，请检查:")
                        .setMessage(tempString)
                        .setNegativeButton("返回修改",null)
                        .show();



            }





//           Toast.makeText(getContext(),commit(),Toast.LENGTH_SHORT).show();


        });
        choosePosition.setOnClickListener(V -> {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()){
                imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
            }
            bResult = "";
            new AlertDialog.Builder(activity)
                    .setTitle("选择楼号")
                    .setItems(bString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bResult += bString[which];
                    for (int i = 1; i <= 6; i++){
                        fString[i-1] = i+"楼";
                    }

                    new AlertDialog.Builder(activity)
                            .setTitle("选择楼层")
                            .setItems(fString, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    bResult += fString[which];
                                    for (int j = 1; j <= maxS; j++){
                                        if (j < 10){
                                            sString[j-1] = (which+1)+"0"+j;
                                        } else {
                                            sString[j-1] = (which+1)+""+j;
                                        }
                                    }
                                    new AlertDialog.Builder(activity)
                                            .setItems(sString, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    bResult += sString[which];
                                                    choosePosition.setText(bResult);
                                                }
                                            })
                                            .setTitle("选择宿舍号")
                                            .show();
                                }
                            }).show();
                }
            })
                    .show();
        });
        choosePeople.setOnClickListener(V -> {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()){
                imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
            }

            new AlertDialog.Builder(activity)
                    .setTitle("选择服务人员")
                    .setItems(servicePeople, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            choosePeople.setText(servicePeople[which]);
                            if (which == 0){
                                phoneNumber = "13365591802";
                            }else {
                                phoneNumber = "13083001921";
                            }
                        }
                    })
                    .show();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        buttonCommit.reset();









    }



    private String commit(){
        String question = new String();

        for(int i = 0; i <=4; i++){
            if (checkBoxes[i].isChecked()){
                question += checkBoxes[i].getText()+",";
            }
        }

        return "姓名:"+name.getText()+'\n'+
                "电话:"+phone.getText()+'\n'+
                "地址:"+choosePosition.getText()+'\n'+
                "预约时间:"+chooseTime.getText()+'\n'+
//                "服务人员:"+choosePeople.getText()+'\n'+
                "出现的问题:"+question;
    }



//    private void initSpinner(){
//        LinkedList<String> fAdapter = new LinkedList<>();
//        LinkedList<String> sAdapter = new LinkedList<>();
//        int maxSNumber = 20;
//        for (int i = 1; i <= 6; i++){
//            fAdapter.add(i+"层");
//        }
//        floorNumber.attachDataSource(fAdapter);
//        for (int j = 1; j <= maxSNumber; j++){
//            if (j < 10){
//                sAdapter.add(1+"0"+j);
//            } else {
//                sAdapter.add("1"+j);
//            }
//        }
//        sNumber.attachDataSource(sAdapter);
//        floorNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sAdapter.clear();
//
//
//                for (int j = 1; j <= maxSNumber; j++){
//                    if (j < 10){
//                        sAdapter.add((position+1)+"0"+j);
//                    } else {
//                        sAdapter.add((position+1)+""+j+"");
//                    }
//                }
//                sNumber.attachDataSource(sAdapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//    }

    private void chooseTimeInit(){

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();


        startDate.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE),date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE)+10);
        endDate.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE)+7);

        TimePickerView pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tDate.setTime(date);
                chooseTime.setText(getTime(date));
            }
        })

                .setType(new boolean[]{false, true, true, true, true, false})
                .setLabel("年","月","日","点","分","秒")
                .isDialog(true)
                .setRangDate(startDate,endDate)
                .build();

        pvTime.show();



//        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        tDate.set(year,month,dayOfMonth,hourOfDay,minute);
//                        aYear = year;
//                        aMonth = month;
//                        aDay = dayOfMonth;
//                        aHour = hourOfDay;
//                        aMinute = minute;
//                        chooseTime.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日"+"  "+hourOfDay+":"+minute);
//                    }
//                },date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),true).show();
//
////                Toast.makeText(getContext(),"changed",Toast.LENGTH_SHORT).show();
//            }
//        },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH)).show();


    }


    public void sendSMS(String phoneNumber,String message){

        try{
//            处理返回的发送状态
            String SENT_SMS_ACTION = "SENT_SMS_ACTION";
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sendIntent= PendingIntent.getBroadcast(activity, 0, sentIntent,
                    0);
// register the Broadcast Receivers
            activity.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context _context, Intent _intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:

//                            activity.showToast(getContext(),"成功向技术人员提交消息！正在检测是否接收，不要关闭应用");
                            Toasty.success(getContext(),"成功向技术人员提交消息！当技术人员收到您的消息时会马上联系您",Toast.LENGTH_LONG).show();
                            buttonCommit.loadingSuccessful();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                        case SmsManager.RESULT_ERROR_NULL_PDU:

//                            buttonCommit.loadingFailed();
                            new android.support.v7.app.AlertDialog.Builder(getContext())
                                    .setTitle("出错啦")
                                    .setMessage("提交失败了，是不是要手动提交")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            buttonCommit.loadingFailed();
                                        }
                                    })
                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
                                            intent.putExtra("sms_body", message);
                                            buttonCommit.loadingSuccessful();
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
//                            Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT_SMS_ACTION));


            //处理返回的接收状态
            String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
// create the deilverIntent parameter
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            PendingIntent backIntent= PendingIntent.getBroadcast(activity, 0,
                    deliverIntent, 0);
            activity.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context _context, Intent _intent) {

//                    buttonCommit.loadingSuccessful();


//                    Toast.makeText(MainActivity.this,
//                            "", Toast.LENGTH_SHORT)
//                            .show();

                    new android.support.v7.app.AlertDialog.Builder(getContext())
                            .setTitle("成功啦")
                            .setMessage("技术人员会在10分钟内联系您，请耐性等候")
                            .setNegativeButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                }
            }, new IntentFilter(DELIVERED_SMS_ACTION));

            SmsManager smsManager = SmsManager.getDefault();
            List<String> divideContents = smsManager.divideMessage(message);
            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, sendIntent, backIntent);
            }
//            smsManager.sendTextMessage(phoneNumber,null,message,sendIntent,backIntent);
//
        } catch (Exception e){

            Toasty.error(activity,"无法获取到短信权限，需要手动点发送",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            buttonCommit.loadingSuccessful();
            startActivity(intent);


        }

    }



//    public void sendSMS(String phoneNumber,String message){
//
//
//
//
//        if (ContextCompat.checkSelfPermission(activity,Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED){
//
//            Toasty.error(activity,"无法获取到短信权限，需要手动点发送",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
//            intent.putExtra("sms_body", message);
//            buttonCommit.loadingSuccessful();
//            startActivity(intent);
//
//
//        } else {
//            //处理返回的发送状态
//            String SENT_SMS_ACTION = "SENT_SMS_ACTION";
//            Intent sentIntent = new Intent(SENT_SMS_ACTION);
//            PendingIntent sendIntent= PendingIntent.getBroadcast(activity, 0, sentIntent,
//                    0);
//// register the Broadcast Receivers
//            activity.registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context _context, Intent _intent) {
//                    switch (getResultCode()) {
//                        case Activity.RESULT_OK:
//
////                            activity.showToast(getContext(),"成功向技术人员提交消息！正在检测是否接收，不要关闭应用");
//                            Toasty.success(getContext(),"成功向技术人员提交消息！正在检测是否接收，不要关闭应用",Toast.LENGTH_LONG).show();
//                            buttonCommit.loadingSuccessful();
//                            break;
//                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        case SmsManager.RESULT_ERROR_NULL_PDU:
////                            buttonCommit.loadingFailed();
//                            new android.support.v7.app.AlertDialog.Builder(getContext())
//                                    .setTitle("出错啦")
//                                    .setMessage("提交失败了，是不是要手动提交")
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            buttonCommit.loadingFailed();
//                                        }
//                                    })
//                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
//                                            intent.putExtra("sms_body", message);
//                                            buttonCommit.loadingSuccessful();
//                                            startActivity(intent);
//                                        }
//                                    })
//                                    .show();
////                            Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
//                            break;
//                    }
//                }
//            }, new IntentFilter(SENT_SMS_ACTION));
//            //处理返回的接收状态
//            String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
//// create the deilverIntent parameter
//            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
//            PendingIntent backIntent= PendingIntent.getBroadcast(activity, 0,
//                    deliverIntent, 0);
//            activity.registerReceiver(new BroadcastReceiver() {
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
//                    new android.support.v7.app.AlertDialog.Builder(getContext())
//                            .setTitle("成功啦")
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





    private String getTime(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
        return format.format(date);
    }


}
