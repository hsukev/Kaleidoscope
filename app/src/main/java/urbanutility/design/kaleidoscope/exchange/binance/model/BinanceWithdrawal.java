package urbanutility.design.kaleidoscope.exchange.binance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BinanceWithdrawal {

    @SerializedName("withdrawList")
    @Expose
    private List<BinanceWithdrawList> withdrawList = new ArrayList<BinanceWithdrawList>();
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<BinanceWithdrawList> getWithdrawList() {
        return withdrawList;
    }

    public void setWithdrawList(List<BinanceWithdrawList> withdrawList) {
        this.withdrawList = withdrawList;
    }

    public BinanceWithdrawal withWithdrawList(List<BinanceWithdrawList> withdrawList) {
        this.withdrawList = withdrawList;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BinanceWithdrawal withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

}