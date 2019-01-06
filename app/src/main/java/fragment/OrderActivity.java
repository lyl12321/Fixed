package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fixed.MainActivity;
import fixed.OrderAdapter;
import fixed.OrderData;
import liyulong.com.fixed.R;

public class OrderActivity extends Fragment {

    private List<OrderData> orderDataList = new ArrayList<>();

    MainActivity activity ;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order,container,false);

        activity = (MainActivity) getActivity();


        RecyclerView recyclerView = view.findViewById(R.id.list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);




        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                initData();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OrderAdapter adapter = new OrderAdapter(orderDataList);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });




        return view;
    }




    private void initData(){
        for (int i = 1;i <=2; i++){
            OrderData orderData = new OrderData(i+"","test");
            orderDataList.add(orderData);
        }
    }

}
