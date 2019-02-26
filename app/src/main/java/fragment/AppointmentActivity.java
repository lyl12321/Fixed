package fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.gson.Gson;


import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import fixed.MainActivity;
import liyulong.com.fixed.R;
import util.Commit;
import util.GetJsonDataUtil;
import util.JsonBean;
import util.NetState;
import util.PhoneNumberMatch;
import util.ReCommit;
import util.StringFilter;

public class AppointmentActivity extends MyFragment {

    private EditText name;
    private EditText phone;
    //    private NiceSpinner buildNumber;
//    private NiceSpinner floorNumber;
//    private NiceSpinner sNumber;
    private Button chooseTime;
    private Calendar date;
    private Calendar tDate;
    private Calendar tDate2;
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
    private boolean netState;
    private int servicePeopleNumber;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private int noption1, noption2, noption3 = 0;
    private boolean[] timepicktype = new boolean[]{false, false, true, true, true, false};
    private ImageView weixing;
    private ImageView zhifubao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment, container, false);

        name = view.findViewById(R.id.editText_Name);
        phone = view.findViewById(R.id.editText_Phone);
//        buildNumber = view.findViewById(R.id.niceSpinner_BuildNumber);
//        floorNumber = view.findViewById(R.id.niceSpinner_FloorNumber);
//        sNumber = view.findViewById(R.id.niceSpinner_SNumber);
        chooseTime = view.findViewById(R.id.button_Time);
        date = Calendar.getInstance();
        tDate = Calendar.getInstance();

        tDate.add(Calendar.MINUTE, 20);
        chooseTime.setText(getTime(tDate.getTime()));


        tDate2 = Calendar.getInstance();
//        tDate.set(2000, 4, 29, 0, 0);
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
        servicePeopleNumber = 111111;
//        phoneNumber = "13365591802";
        choosePeople = view.findViewById(R.id.button_People);
        servicePeople = new String[]{"lqwq    (李钰龙)", "cloverkit    (周广来)"};


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程中解析省市区数据
                initJsonData();
            }
        });
        thread.start();

        //进行时间的判断
        tDate2.add(Calendar.DATE, 7);
        if (!(date.get(Calendar.MONTH) == tDate2.get(Calendar.MONTH))) {
            timepicktype[1] = true;
        }
        if (!(date.get(Calendar.YEAR) == tDate2.get(Calendar.YEAR))) {
            timepicktype[0] = true;
        }

//        choosePosition.setText("点我选择位置");

//        progressBar.setIndeterminateDrawable(new DoubleBounce());





        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
//        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
//        initSpinner();
//        final int maxS = 20;
//        String[] bString = new String[]{"1北","1南","2北","2南","3北","3南","4北","4南","5北","5南","6北","6南"};
//        String[] fString = new String[6];
//        String[] sString = new String[maxS];


        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            try {
                String tmLine1Number = tm.getLine1Number();
                if (tmLine1Number.length() >= 11){
                    String mPhoneNumber = tmLine1Number.substring(tmLine1Number.length()-11);
//            StringFilter.StringFilter(tmLine1Number);
                    if (!mPhoneNumber.equals("00000000000") && mPhoneNumber.length() == 11){
                        phone.setText(mPhoneNumber);
                        Toast.makeText(activity,"电话号码已经自动填入，如有误请修改！",Toast.LENGTH_LONG).show();

                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }



        chooseTime.setOnClickListener(V -> {

            chooseTimeInit();

        });
        buttonCommit.setOnClickListener(V -> {
            buttonCommit.reset();
            String tempString = "";
            String issue = "";

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
//            String tempString1 = "";
//            for(int i = 0; i <=4; i++){
//                if (checkBoxes[i].isChecked()){
//                    tempString1 += checkBoxes[i].getText()+",";
//                    issue += checkBoxes[i].getText()+",";
//                }
//            }
//
//            if (tempString1 == ""){
//                tempString += "--您必须选择一项遇到的问题"+"\n";
//            }





            if (tempString.length() == 0 ) {

                String finalIssue = issue;
                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("核对您的信息!")
                        .setMessage(commit())
                        .setNegativeButton("返回修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK了，提交吧", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                buttonCommit.startLoading();

                                new Thread() {
                                    @Override
                                    public void run() {

                                        if (!NetState.isNetworkConnected(activity)){
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    buttonCommit.loadingFailed();
                                                    Toasty.error(activity,"当前无网络，无法提交！",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            return;
                                        }

                                        SharedPreferences perf = activity.getSharedPreferences("returnid", activity.MODE_PRIVATE);

                                        if (perf.getInt("id",0) == 0) {

                                            Date now = new Date(System.currentTimeMillis());

                                            if (Commit.netCommit(activity, name.getText().toString(), phone.getText().toString(), bResult, chooseTime.getText().toString(),
                                                    servicePeopleNumber, finalIssue, getTime(now))
                                                    ) {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.success(activity, "成功向技术人员提交消息！当技术人员收到您的消息时会马上联系您", Toast.LENGTH_LONG).show();
                                                        activity.changeToolbar(String.valueOf(perf.getInt("id",0)));
                                                        buttonCommit.loadingSuccessful();
                                                    }
                                                });

                                            } else {
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.error(activity,"错误码100",Toast.LENGTH_LONG).show();
                                                        buttonCommit.loadingFailed();
                                                    }
                                                });

                                            }

//                                    sendSMS("+86"+phoneNumber,commit());
//
                                        } else {

                                            if (ReCommit.netReCommit(activity,perf.getInt("id",0))){

                                                Date now = new Date(System.currentTimeMillis());
                                                if (Commit.netCommit(activity, name.getText().toString(), phone.getText().toString(), bResult, chooseTime.getText().toString(),
                                                        servicePeopleNumber, finalIssue, getTime(now))
                                                        ) {
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toasty.success(activity, "成功向技术人员提交消息！当技术人员收到您的消息时会马上联系您", Toast.LENGTH_LONG).show();
                                                            activity.changeToolbar(String.valueOf(perf.getInt("id",0)));
                                                            buttonCommit.loadingSuccessful();
                                                        }
                                                    });

                                                } else {
                                                    activity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toasty.error(activity,"错误码100",Toast.LENGTH_LONG).show();
                                                            buttonCommit.loadingFailed();
                                                        }
                                                    });

                                                }

                                            } else {

                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.info(activity,"还有未完成的订单哦!请联系开发者!",Toast.LENGTH_LONG).show();
                                                        buttonCommit.loadingFailed();
                                                    }
                                                });

                                            }

                                        }


                                    }
                                }.start();

                            }
                        })
                        .setCancelable(false)
                        .show();
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


            OptionsPickerView pvOptions = new OptionsPickerBuilder(activity, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    noption1 = options1;
                    noption2 = options2;
                    noption3 = options3;
//                    choosePosition.setText(options1Items.get(options1).getPickerViewText() +
//                            options3Items.get(options1).get(options2).get(options3));
                    bResult = options1Items.get(options1).getPickerViewText() +
                            options3Items.get(options1).get(options2).get(options3);
                    choosePosition.setText(bResult);
                }
            })
                    .setSelectOptions(noption1,noption2,noption3)

                    .setTitleText("选择位置")
//                    .setDividerColor(Color.BLACK)
//                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                    .setContentTextSize(20)
                    .isDialog(true)
                    .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            pvOptions.show();

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
                                servicePeopleNumber = 111111;
//                                phoneNumber = "13365591802";
                            }else {
                                servicePeopleNumber = 222222;
//                                phoneNumber = "13083001921";
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
                "预约时间:"+chooseTime.getText()+'\n';
//                "服务人员:"+choosePeople.getText()+'\n'+
//                "出现的问题:"+question;
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


        startDate.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE),date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE)+20);
        endDate.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE)+7);

        TimePickerView pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tDate.setTime(date);
                chooseTime.setText(getTime(date));
            }
        })
                .setTitleText("选择时间")
                .setType(timepicktype)
                .setLabel("年","月","日","点","分","秒")
                .isDialog(true)
                .setRangDate(startDate,endDate)
                .build();

        pvTime.show();



    }






    private void initJsonData() {//解析数据


        String JsonData = new GetJsonDataUtil().getJson(activity, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体


        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }


    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
        return format.format(date);
    }


}
