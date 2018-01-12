package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jerye on 1/12/2018.
 */

@Entity(tableName = "kaleido_balances")
public class KaleidoBalance {
    @PrimaryKey
    @NonNull
    private String symbol;
    private String totalQty;
    private String averagePrice;

    public KaleidoBalance(String symbol, String totalQty, String averagePrice) {
        this.symbol = symbol;
        this.totalQty = totalQty;
        this.averagePrice = averagePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }
}
