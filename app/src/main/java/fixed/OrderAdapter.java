package fixed;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.list_item_id);
            name = itemView.findViewById(R.id.list_item_name);
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

    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }


}
