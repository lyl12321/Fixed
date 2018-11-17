package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import context.MyApplication;
import fixed.MainActivity;
import liyulong.com.fixed.R;

public class AppointmentActivity extends Fragment {

    private EditText name;
    private EditText phone;
    private NiceSpinner buildNumber;
    private NiceSpinner floorNumber;
    private NiceSpinner sNumber;
    private Button chooseTime;
    private String sTime = new String();


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











        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
        initSpinner();



        chooseTime.setOnClickListener(V -> {
            chooseTimeInit();
        });




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
                sNumber.setText((position+1)+"01");

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
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();


        startDate.set(selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE));
        endDate.set(selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH),selectedDate.get(Calendar.DATE)+7);



    }




    //    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.LEFT,enter,500);
//
//    }
}
