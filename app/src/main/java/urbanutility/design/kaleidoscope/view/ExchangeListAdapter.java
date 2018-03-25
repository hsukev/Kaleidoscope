package urbanutility.design.kaleidoscope.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import urbanutility.design.kaleidoscope.R;

/**
 * Created by jerye on 2/18/2018.
 */

public class ExchangeListAdapter extends RecyclerView.Adapter<ExchangeListAdapter.ExchangeListViewHolder> {
    private List<String> exchangeList = new ArrayList<>();
    private ExchangeListClickHandler clickHandler;

    public ExchangeListAdapter(ExchangeListClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public ExchangeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_exchange_item, parent, false);
        return new ExchangeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExchangeListViewHolder holder, int position) {
        holder.exchangeName.setText(exchangeList.get(position));
    }

    @Override
    public int getItemCount() {
        return exchangeList.size();
    }

    public void addExchange(String newExchange) {
        exchangeList.add(newExchange);
    }

    class ExchangeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView exchangeName;
        @BindView(R.id.update_button)
        ImageView updateButton;
        @BindView(R.id.delete_button)
        ImageView deleteButton;

        ExchangeListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.update(exchangeList.get(getAdapterPosition()));
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.delete(exchangeList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ExchangeListClickHandler {
        void update(String exchangeName);

        void delete(String exchangeName);
    }
}
