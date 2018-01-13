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
import urbanutility.design.kaleidoscope.model.KaleidoBalance;

/**
 * Created by jerye on 1/12/2018.
 */

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceViewHolder> {
    private List<KaleidoBalance> list = new ArrayList<>();
    private Context mContext;

    public BalanceAdapter(Context context){
        mContext = context;
    }

    @Override
    public BalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.balance_item, parent, false);
        return new BalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BalanceViewHolder holder, int position) {
        holder.balanceSymbol.setText(list.get(position).getSymbol());
        holder.balanceAveragePrice.setText(list.get(position).getAveragePrice());
        holder.balanceQuantity.setText(list.get(position).getTotalQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BalanceViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.balance_symbol)
        TextView balanceSymbol;
        @BindView(R.id.balance_average_price)
        TextView balanceAveragePrice;
        @BindView(R.id.balance_quantity)
        TextView balanceQuantity;

        public BalanceViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}
