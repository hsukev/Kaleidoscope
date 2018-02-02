package urbanutility.design.kaleidoscope.model;

import android.arch.persistence.room.Embedded;
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
    public String id;

    @Embedded
    public BalanceType balanceType;

    public KaleidoBalance(BalanceType balance) {
        this.balanceType = balance;
        this.id = this.balanceType.exchange + "-" + this.balanceType.symbol;
    }

    public KaleidoBalance(String id){
        this.id = id;
    }

    public BalanceType getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(BalanceType balanceType) {
        this.balanceType = balanceType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
