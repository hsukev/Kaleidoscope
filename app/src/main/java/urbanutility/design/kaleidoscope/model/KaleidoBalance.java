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

    public KaleidoBalance(String id,BalanceType balance) {
        this.id = id;
        this.balanceType = balance;
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
