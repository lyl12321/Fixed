package fixed;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import liyulong.com.fixed.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderData> orderDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView phoneNumber;
        TextView address;
        TextView time;
        TextView servicePeople;
        TextView issue;
        TextView commitTime;
        TextView solve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.list_item_id);
            name = itemView.findViewById(R.id.list_item_name);
            phoneNumber = itemView.findViewById(R.id.list_item_phoneNumber);
            address = itemView.findViewById(R.id.list_item_address);
            time = itemView.findViewById(R.id.list_item_time);
            servicePeople = itemView.findViewById(R.id.list_item_servicePeople);
            issue = itemView.findViewById(R.id.list_item_issue);
            commitTime = itemView.findViewById(R.id.list_item_commitTime);
            solve = itemView.findViewById(R.id.list_item_solve);
        }
    }

    public OrderAdapter(List<OrderData> orderData){
        orderDataList = orderData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OrderData order = orderDataList.get(i);
        viewHolder.id.setText("订单号:   "+order.getId());
        viewHolder.name.setText("姓名:   "+order.getName());
        viewHolder.phoneNumber.setText("电话号码:   "+order.getPhoneNumber());
        viewHolder.address.setText("地址:   "+order.getAddress());
        viewHolder.time.setText("预约时间:   "+order.getTime());
        if (order.getServicePeople().equals("111111")){
            viewHolder.servicePeople.setText("服务人员:   lqwq(李钰龙)");
        } else {
            viewHolder.servicePeople.setText("服务人员:   cloverkit(周广来)");
        }

        viewHolder.issue.setText("出现问题:   "+order.getIssue());
        viewHolder.commitTime.setText("下单时间:   "+order.getCommitTime());
        if (order.getSolve().equals("1")){
            viewHolder.solve.setText("已完成");
            viewHolder.solve.setTextColor(Color.rgb(140,255,140));
        } else {
            viewHolder.solve.setText("正在进行...");
            viewHolder.solve.setTextColor(Color.rgb(255,140,140));
        }
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }


}
