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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fixed.MainActivity;
import fixed.OrderAdapter;
import fixed.OrderData;
import liyulong.com.fixed.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.GetIMEI;

public class OrderActivity extends Fragment {

    private List<OrderData> orderDataList = new ArrayList<>();

    MainActivity activity ;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView orderTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order,container,false);
        orderTextView = view.findViewById(R.id.order_textview);

        activity = (MainActivity) getActivity();


        RecyclerView recyclerView = view.findViewById(R.id.list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

//        Toast.makeText(activity,GetIMEI.getIMEI(activity),Toast.LENGTH_LONG).show();



        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    initData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (orderDataList.size() == 0) {
                            orderTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            orderTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        if (GetIMEI.getIMEI(activity).equals("0")){
                            orderTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
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
                            initData();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (orderDataList.size() == 0) {
                                    orderTextView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    orderTextView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                if (GetIMEI.getIMEI(activity).equals("0")){
                                    orderTextView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                OrderAdapter adapter = new OrderAdapter(orderDataList);
                                recyclerView.setAdapter(adapter);
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
        orderDataList.clear();
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new FormBody.Builder()
                .add("imei", GetIMEI.getIMEI(activity))
                .build();
        Request request = new Request.Builder()
                .url("http://172.96.252.160/appdata/client/order.php")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String returnData = response.body().string();


            //Json的解析类对象
            JsonParser parser = new JsonParser();
            //将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(returnData).getAsJsonArray();

            Gson gson = new Gson();
            ArrayList<OrderData> userBeanList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成OrderData对象
                OrderData orderData = gson.fromJson(user, OrderData.class);
                orderDataList.add(orderData);
            }


//            List<OrderData> orderData = gson.fromJson(returnData, new TypeToken<List<OrderData>>(){}.getType());
//            orderDataList.add((OrderData) orderData);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
