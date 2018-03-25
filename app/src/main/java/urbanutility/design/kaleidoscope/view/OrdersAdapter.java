package urbanutility.design.kaleidoscope.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.R;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 1/10/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    List<KaleidoOrder> list = new ArrayList<>();
    Context mContext;
    public OrdersAdapter(Context context){
        mContext = context;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_order_item, parent, false);
        Log.d("Viewpager", "orders create view holder");
        return new OrdersViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        holder.orderSymbol.setText(list.get(position).getSymbol());
        holder.orderAmount.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getAmount()));
        holder.orderExchange.setText(list.get(position).getExchange());
        holder.orderPrice.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getPrice()));
        holder.orderSide.setText(list.get(position).getSide());
        holder.orderTime.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_symbol)
        TextView orderSymbol;
        @BindView(R.id.order_amount)
        TextView orderAmount;
        @BindView(R.id.order_exchange)
        TextView orderExchange;
        @BindView(R.id.order_price)
        TextView orderPrice;
        @BindView(R.id.order_side)
        TextView orderSide;
        @BindView(R.id.order_time)
        TextView orderTime;


         OrdersViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public void addOrder(List<KaleidoOrder> orders){
        list = orders;
        notifyDataSetChanged();
    }
}
