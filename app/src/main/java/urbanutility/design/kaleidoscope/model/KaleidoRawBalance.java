package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import urbanutility.design.kaleidoscope.datatypes.BalanceType;

/**
 * Created by jerye on 1/12/2018.
 */

@Entity(tableName = "kaleido_balances")
public class KaleidoRawBalance {
    @PrimaryKey
    @NonNull
    private String symbol;
    private long totalQty;
    private long averagePrice;

    public KaleidoRawBalance(BalanceType balance) {
        this.symbol = balance.symbol;
        this.totalQty = balance.totalQty;
        this.averagePrice = balance.averagePrice;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public long getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(long totalQty) {
        this.totalQty = totalQty;
    }

    public long getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(long averagePrice) {
        this.averagePrice = averagePrice;
    }
}
