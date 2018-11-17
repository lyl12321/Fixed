package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;

import liyulong.com.fixed.R;

public class AppointmentActivity extends Fragment {

    private EditText name;
    private EditText phone;
    private NiceSpinner buildNumber;
    private NiceSpinner floorNumber;
    private NiceSpinner sNumber;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment,container,false);

        name = view.findViewById(R.id.editText_Name);
        phone = view.findViewById(R.id.editText_Phone);
        buildNumber = view.findViewById(R.id.niceSpinner_BuildNumber);
        floorNumber = view.findViewById(R.id.niceSpinner_FloorNumber);
        sNumber = view.findViewById(R.id.niceSpinner_SNumber);












        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        buildNumber.attachDataSource(new LinkedList<>(Arrays.asList("1北","1南","2北","2南","3北","3南","5北","5南")));
        initSpinner();


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




    //    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        return MoveAnimation.create(MoveAnimation.LEFT,enter,500);
//
//    }
}
