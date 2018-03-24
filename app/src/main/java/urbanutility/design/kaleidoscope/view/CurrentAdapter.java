package urbanutility.design.kaleidoscope.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import urbanutility.design.kaleidoscope.model.KaleidoPosition;
import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 1/12/2018.
 */

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.BalanceViewHolder> {
    private List<KaleidoPosition> list = new ArrayList<>();
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
        holder.symbol.setText(list.get(position).getSymbol());
        holder.totalGain.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getTotalGain()));
        holder.costPerUnit.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getAvgUnitPrice()));
        holder.currentValue.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getCurrentUnitPrice()));
        holder.realizedGain.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getRealizedGain()));
        holder.unrealizedGain.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getUnrealizedGain()));
        holder.amount.setText(KaleidoFunctions.doubleToFormatedString(list.get(position).getAmount()));
        holder.changedPercent.setText(String.format(Locale.ENGLISH,"%.2f", list.get(position).getPercentChange()));

    }

    public void refresh(List<KaleidoPosition> positionList){
        list = positionList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(list.isEmpty()){
            return 0;
        }else{
            return list.size();
        }

    }

    public class BalanceViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.symbol)
        TextView symbol;
        @BindView(R.id.totalGain)
        TextView totalGain;
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
