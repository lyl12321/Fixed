package fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import context.MyApplication;
import fixed.MainActivity;
import fixed.OrderAdapter;
import fixed.OrderData;
import inf.ItemInnerCancelOrderListener;
import inf.ItemInnerFinishedOrderListener;
import liyulong.com.fixed.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.GetIMEI;
import util.ReCommit;
import util.changSolve;

public class OrderActivity extends Fragment implements ItemInnerFinishedOrderListener, ItemInnerCancelOrderListener {

    private List<OrderData> orderDataList = new ArrayList<>();

    MainActivity activity ;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView orderTextView;

    private Dialog dialog1;

    private RecyclerView recyclerView;

    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order,container,false);
        orderTextView = view.findViewById(R.id.order_textview);

        activity = (MainActivity) getActivity();


        recyclerView = view.findViewById(R.id.list_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

//        Toast.makeText(activity,GetIMEI.getIMEI(activity),Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //View view = getLayoutInflater().inflate(R.layout.progress);
        builder.setView(R.layout.progress_dialog);
        dialog1 = builder.create();




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
                        adapter = new OrderAdapter(orderDataList);
                        adapter.setItemInnerCancelOrderListener(OrderActivity.this::onItemInnerCancelOrderClick);
                        adapter.setItemInnerFinishedOrderListener(OrderActivity.this::onItemInnerFinishedOrderClick);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setAdapter(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SharedPreferences perf = activity.getSharedPreferences("returnid", activity.MODE_PRIVATE);
                        ReCommit.netReCommit(MyApplication.getContext(),perf.getInt("id",0));

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (perf.getInt("id",0) == 0){
                                    changState();
                                }


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
                                adapter = new OrderAdapter(orderDataList);
                                adapter.setItemInnerCancelOrderListener(OrderActivity.this::onItemInnerCancelOrderClick);
                                adapter.setItemInnerFinishedOrderListener(OrderActivity.this::onItemInnerFinishedOrderClick);
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
//        recyclerView.setAdapter(null);
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

    private void changState(){
        activity.closeToolbarState();
    }


    @Override
    public void onItemInnerCancelOrderClick(int position,String id) {
//        Toast.makeText(activity,"点击了"+id+"取消按钮",Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(activity)
                .setTitle("请确认")
                .setMessage("确认取消订单号为"+id+"的订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog1.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (changSolve.changSolve(id,"2")){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            Toast.makeText(activity,"取消成功",Toast.LENGTH_LONG).show();
                                            //通知APP进行刷新
                                            refreshData();
                                        }
                                    });

                                } else {

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            Toast.makeText(activity,"取消失败，请在软件关于内联系开发者",Toast.LENGTH_LONG).show();
                                            //通知APP进行刷新
                                            refreshData();
                                        }
                                    });

                                }
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消",null)
                .show();

    }

    @Override
    public void onItemInnerFinishedOrderClick(int position,String id) {
//        Toast.makeText(activity,"点击了"+id+"完成按钮",Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(activity)
                .setTitle("请确认")
                .setMessage("确认完成了订单号为"+id+"的订单了吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog1.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (changSolve.changSolve(id,"1")){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            Toast.makeText(activity,"感谢您选择我们的服务",Toast.LENGTH_LONG).show();
                                            //通知APP进行刷新
                                            refreshData();
                                        }
                                    });

                                } else {

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            Toast.makeText(activity,"完成失败，请联系开发者",Toast.LENGTH_LONG).show();
                                            //通知APP进行刷新
                                            refreshData();
                                        }
                                    });

                                }
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消",null)
                .show();

    }


    private void refreshData(){
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setAdapter(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SharedPreferences perf = activity.getSharedPreferences("returnid", activity.MODE_PRIVATE);
                ReCommit.netReCommit(MyApplication.getContext(),perf.getInt("id",0));

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (perf.getInt("id",0) == 0){
                            changState();
                        }


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
                        adapter.setItemInnerCancelOrderListener(OrderActivity.this::onItemInnerCancelOrderClick);
                        adapter.setItemInnerFinishedOrderListener(OrderActivity.this::onItemInnerFinishedOrderClick);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
