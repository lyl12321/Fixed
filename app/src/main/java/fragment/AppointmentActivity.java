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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;


import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import context.MyApplication;
import fixed.MainActivity;
import liyulong.com.fixed.R;
import util.PhoneNumberMatch;

public class AppointmentActivity extends Fragment {

    private EditText name;
    private EditText phone;
    private NiceSpinner buildNumber;
    private NiceSpinner floorNumber;
    private NiceSpinner sNumber;
    private Button chooseTime;
    private Calendar date;
    private Calendar tDate;
    private int aYear;
    private int aMonth;
    private int aDay;
    private int aHour;
    private int aMinute;
    private CheckBox[] checkBoxes;

    private LoadingButton buttonCommit;
    MainActivity activity;
    ProgressBar progressBar;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment,container,false);

        name = view.findViewById(R.id.editText_Name);
        phone = view.findViewById(R.id.editText_Phone);
        buildNumber = view.findViewById(R.id.niceSpinner_BuildNumber);
        floorNumber = view.findViewById(R.id.niceSpinner_FloorNumber);
        sNumber = view.findViewById(R.id.niceSpinner_SNumber);
        chooseTime = view.findViewById(R.id.button_Time);
        date = Calendar.getInstance();
        tDate = Calendar.getInstance();
        checkBoxes = new CheckBox[5];
        checkBoxes[0] = view.findViewById(R.id.checkBox1);
        checkBoxes[1] = view.findViewById(R.id.checkBox2);
        checkBoxes[2] = view.findViewById(R.id.checkBox3);
        checkBoxes[3] = view.findViewById(R.id.checkBox4);
        checkBoxes[4] = view.findViewById(R.id.checkBox5);
        buttonCommit = view.findViewById(R.id.button_Commit);
        activity = (MainActivity) getActivity();


        progressBar = view.findViewById(R.id.spin_kit);

//        progressBar.setIndeterminateDrawable(new DoubleBounce());




        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
        initSpinner();
        chooseTime.setOnClickListener(V -> {

            chooseTimeInit();

        });
        buttonCommit.setOnClickListener(V -> {
            buttonCommit.reset();
            Calendar tempDate = Calendar.getInstance();
            tempDate.set(aYear,aMonth,aDay,aHour,aMinute);

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
            if (phone.getText().length() != 11
                    && !PhoneNumberMatch.isMobileNO(phone.getText().toString())){
                tempString += "--联系方式暂时只支持13位大陆电话" + "\n";
            }
            if (!date.before(tempDate)){
                tempString += "--预约时间要大于现在的时间"+"\n";
            }


            if (tempString.length() == 0) {



                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setMessage("核对您的信息!" + "\n" +commit())
                        .setNegativeButton("返回修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("ojbk了，提交吧", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                buttonCommit.startLoading();

                                sendSMS("+8613365591802",commit());


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
                        .setMessage("您的输入有问题，请检查" +"\n"+
                                tempString)
                        .setNegativeButton("返回修改",null)
                        .show();



            }





//           Toast.makeText(getContext(),commit(),Toast.LENGTH_SHORT).show();


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
                "地址:"+buildNumber.getText()+sNumber.getText()+'\n'+
                "预约时间:"+chooseTime.getText()+'\n'+
                "出现的问题:"+question;
    }

    private void initSpinner(){
        LinkedList<String> fAdapter = new LinkedList<>();
        LinkedList<String> sAdapter = new LinkedList<>();
        int maxSNumber = 20;
        for (int i = 1; i <= 6; i++){
            fAdapter.add(i+"层");
        }
        floorNumber.attachDataSource(fAdapter);
        for (int j = 1; j <= maxSNumber; j++){
            if (j < 10){
                sAdapter.add(1+"0"+j);
            } else {
                sAdapter.add("1"+j);
            }
        }
        sNumber.attachDataSource(sAdapter);
        floorNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAdapter.clear();


                for (int j = 1; j <= maxSNumber; j++){
                    if (j < 10){
                        sAdapter.add((position+1)+"0"+j);
                    } else {
                        sAdapter.add((position+1)+""+j+"");
                    }
                }
                sNumber.attachDataSource(sAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void chooseTimeInit(){

//        Calendar startDate = Calendar.getInstance();
//        Calendar endDate = Calendar.getInstance();
//
//
//        startDate.set(selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));
//        endDate.set(selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE)+7);



        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tDate.set(year,month,dayOfMonth,hourOfDay,minute);
                        aYear = year;
                        aMonth = month;
                        aDay = dayOfMonth;
                        aHour = hourOfDay;
                        aMinute = minute;
                        chooseTime.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日"+"  "+hourOfDay+":"+minute);
                    }
                },date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),true).show();

//                Toast.makeText(getContext(),"changed",Toast.LENGTH_SHORT).show();
            }
        },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH)).show();


    }





    public void sendSMS(String phoneNumber,String message){




        if (ContextCompat.checkSelfPermission(activity,Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){

            Toast.makeText(activity,"无法获取到短信权限，需要手动点发送",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            buttonCommit.loadingSuccessful();
            startActivity(intent);


        } else {
            //处理返回的发送状态
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

                            activity.showToast(getContext(),"成功向技术人员提交消息！正在检测是否接收，不要关闭应用");
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                        case SmsManager.RESULT_ERROR_NULL_PDU:

//                            buttonCommit.loadingFailed();

                            new android.support.v7.app.AlertDialog.Builder(getContext())
                                    .setMessage("我也不知道啥情况，反正出错了，检查下吧")
                                    .setNegativeButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            buttonCommit.loadingFailed();
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
                            .setMessage("技术人员会在两小时内联系您，请耐性等候")
                            .setNegativeButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    buttonCommit.loadingSuccessful();
                                }
                            })
                            .show();

                }
            }, new IntentFilter(DELIVERED_SMS_ACTION));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null,message,sendIntent,backIntent);
        }

    }







    //    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.LEFT,enter,500);
//
//    }
}
