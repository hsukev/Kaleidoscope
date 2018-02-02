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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.R;
import urbanutility.design.kaleidoscope.model.KaleidoBaseCurrency;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 1/12/2018.
 */

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.BalanceViewHolder> {
    private List<KaleidoBaseCurrency> list = new ArrayList<>();
    private Context mContext;

    public CurrentAdapter(Context context){
        mContext = context;
    }

    @Override
    public BalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.current_item, parent, false);
        return new BalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BalanceViewHolder holder, int position) {
        holder.symbol.setText(list.get(1).getBaseCurrencyType().positions.get(position).symbol);
        Log.d("currentadapter", list.get(1).getBaseCurrencyType().positions.get(position).symbol);
        holder.cost.setText(String.format(Locale.ENGLISH,"$%e", list.get(1).getBaseCurrencyType().positions.get(position).cost));
        holder.costPerUnit.setText(KaleidoFunctions.DoubleToFormatedString(list.get(1).getBaseCurrencyType().positions.get(position).costPerUnit));
        holder.currentValue.setText(String.format(Locale.ENGLISH,"$%e", list.get(1).getBaseCurrencyType().positions.get(position).currentVal));
        holder.realizedGain.setText(String.format(Locale.ENGLISH,"$%.3f", list.get(1).getBaseCurrencyType().positions.get(position).realizedGain));
        holder.unrealizedGain.setText(String.format(Locale.ENGLISH,"$%.3f", list.get(1).getBaseCurrencyType().positions.get(position).unrealizedGain));
        holder.amount.setText(KaleidoFunctions.DoubleToFormatedString(list.get(1).getBaseCurrencyType().positions.get(position).amount));
        holder.changedPercent.setText(String.format(Locale.ENGLISH,"%.2f", list.get(1).getBaseCurrencyType().positions.get(position).changePercent));

    }

    public void refresh(List<KaleidoBaseCurrency> baseCurrencies){
        list = baseCurrencies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(list.isEmpty()){
            return 0;
        }else{
            return list.get(1).getBaseCurrencyType().positions.size();
        }

    }

    public class BalanceViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.symbol)
        TextView symbol;
        @BindView(R.id.cost)
        TextView cost;
        @BindView(R.id.cost_per_unit)
        TextView costPerUnit;
        @BindView(R.id.current_val)
        TextView currentValue;
        @BindView(R.id.realized_gain)
        TextView realizedGain;
        @BindView(R.id.unrealized_gain)
        TextView unrealizedGain;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.changed_percent)
        TextView changedPercent;

        public BalanceViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}
