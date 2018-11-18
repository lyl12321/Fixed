package fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import context.MyApplication;
import liyulong.com.fixed.R;

public class AppointmentActivity extends Fragment {

    private EditText name;
    private EditText phone;
    private NiceSpinner buildNumber;
    private NiceSpinner floorNumber;
    private NiceSpinner sNumber;
    private Button chooseTime;
    private Calendar date;
    private int aYear;
    private int aMonth;
    private int aDay;
    private int aHour;
    private int aMinute;
    private CheckBox[] checkBoxes;

    private Button buttonCommit;



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
        checkBoxes = new CheckBox[5];
        checkBoxes[0] = view.findViewById(R.id.checkBox1);
        checkBoxes[1] = view.findViewById(R.id.checkBox2);
        checkBoxes[2] = view.findViewById(R.id.checkBox3);
        checkBoxes[3] = view.findViewById(R.id.checkBox4);
        checkBoxes[4] = view.findViewById(R.id.checkBox5);
        buttonCommit = view.findViewById(R.id.button_Commit);


        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
        initSpinner();



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        chooseTime.setOnClickListener(V -> {

            chooseTimeInit();

        });


       buttonCommit.setOnClickListener(V -> {
           Toast.makeText(getContext(),commit(),Toast.LENGTH_SHORT).show();

       });




    }



    private String commit(){
        String question = new String();

        for(int i = 0; i <=4; i++){
            if (checkBoxes[i].isChecked()){
                question += checkBoxes[i].getText()+",";
            }
        }
        return "姓名:"+name.getText()+";"+
                "电话:"+phone.getText()+";"+
                "地址:"+buildNumber.getText()+sNumber.getText()+";"+
                "预约时间:"+chooseTime.getText()+";"+
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
                        date.set(year,month,dayOfMonth,hourOfDay,minute);
                        aYear = year;
                        aMonth = month;
                        aDay = dayOfMonth;
                        aHour = hourOfDay;
                        aMinute = minute;
                        chooseTime.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日"+"  "+hourOfDay+":"+minute);
                    }
                },date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),false).show();

//                Toast.makeText(getContext(),"changed",Toast.LENGTH_SHORT).show();
            }
        },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH)).show();


    }





    //    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.LEFT,enter,500);
//
//    }
}
