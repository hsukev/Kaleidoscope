package urbanutility.design.kaleidoscope.exchange.binance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BinanceDeposit {

    @SerializedName("depositList")
    @Expose
    private List<BinanceDepositList> binanceDepositList = new ArrayList<BinanceDepositList>();
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<BinanceDepositList> getBinanceDepositList() {
        return binanceDepositList;
    }

    public void setBinanceDepositList(List<BinanceDepositList> binanceDepositList) {
        this.binanceDepositList = binanceDepositList;
    }

    public BinanceDeposit getBinanceDepositList(List<BinanceDepositList> binanceDepositList) {
        this.binanceDepositList = binanceDepositList;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BinanceDeposit withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

}