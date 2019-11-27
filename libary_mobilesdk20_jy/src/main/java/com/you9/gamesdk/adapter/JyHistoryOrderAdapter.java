package com.you9.gamesdk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.you9.gamesdk.R;
import com.you9.gamesdk.bean.JyOrder;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;

import java.util.List;

public class JyHistoryOrderAdapter extends BaseAdapter {
    private List<JyOrder> orders;
    private Context context;
    private ViewHolder holder;

    public JyHistoryOrderAdapter(Context context) {
        this.context = context;

    }


    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<JyOrder> orders) {
        this.orders = orders;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_item_history_order"), null);
        holder = new ViewHolder();

        holder.tvOrderId = (TextView) convertView.findViewById(ResourceUtil.getId(context, "tvOrderId"));
        holder.tvCreateTime = (TextView) convertView.findViewById(ResourceUtil.getId(context, "tvCreateTime"));
        holder.tvHistoryOrderName = (TextView) convertView.findViewById(ResourceUtil.getId(context, "tvHistoryOrderName"));
        holder.tvPrice = (TextView) convertView.findViewById(ResourceUtil.getId(context, "tvPrice"));
        holder.tvPayStatus = (TextView) convertView.findViewById(ResourceUtil.getId(context, "tvPayStatus"));

        holder.tvOrderId.setText(orders.get(position).getOrder_id());
        holder.tvCreateTime.setText(orders.get(position).getCreate_time());
        holder.tvHistoryOrderName.setText(orders.get(position).getSubject());
        holder.tvPrice.setText(String.format(context.getResources().getString(ResourceUtil.getStringId(context, "jy_activity_history_order_price")), JyUtils.convertFenToYuan(orders.get(position).getPrice())));

        Log.d("eeeee", "listSize=" + orders.size());
        Log.d("eeeee", "order=" + orders.get(position).getOrder_id() + "======order_status=" + orders.get(position).getOrder_status());
        if (orders.get(position).getOrder_status().equals("1")) {
            //支付成功
            holder.tvPayStatus.setText(context.getResources().getString(ResourceUtil.getStringId(context, "jy_activity_history_order_pay_success")));
            holder.tvPayStatus.setTextColor(context.getResources().getColor(ResourceUtil.getColorId(context, "jy_bg_pay_success_green")));

        } else {
            //支付失败
            holder.tvPayStatus.setText(context.getResources().getString(ResourceUtil.getStringId(context, "jy_activity_history_order_pay_failed")));
            holder.tvPayStatus.setTextColor(context.getResources().getColor(ResourceUtil.getColorId(context, "jy_bg_pay_failed_red")));
        }


        convertView.setTag(holder);


        return convertView;
    }

    static class ViewHolder {
        private TextView tvOrderId;
        private TextView tvCreateTime;
        private TextView tvHistoryOrderName;
        private TextView tvPrice;
        private TextView tvPayStatus;

//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
    }
}
