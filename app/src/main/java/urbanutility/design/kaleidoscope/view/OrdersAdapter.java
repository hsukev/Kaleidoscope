package urbanutility.design.kaleidoscope.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.R;
import urbanutility.design.kaleidoscope.model.BinanceOrder;

/**
 * Created by jerye on 1/10/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    List<BinanceOrder> list = new ArrayList<>();
    Context mContext;
    public OrdersAdapter(Context context){
        mContext = context;
    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_history_item, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrdersViewHolder holder, int position) {
        holder.orderSymbol.setText(list.get(position).getSymbol());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_symbol)
        TextView orderSymbol;

        public OrdersViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public void addOrder(List<BinanceOrder> orders){
        list = orders;
        notifyDataSetChanged();
    }
}
