package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import urbanutility.design.kaleidoscope.datatypes.BalanceType;

/**
 * Created by jerye on 1/12/2018.
 */

@Entity(tableName = "kaleido_balances")
public class KaleidoBalance {
    @PrimaryKey
    @NonNull
    private BalanceType balanceType;

    public KaleidoBalance(BalanceType balance) {
        this.balanceType = balance;
    }

    @NonNull
    public BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(@NonNull BalanceType balanceType) {
        this.balanceType = balanceType;
    }
}
