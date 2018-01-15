
package urbanutility.design.kaleidoscope.exchange.binance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BinanceAccountInfo implements Parcelable {

    @SerializedName("makerCommission")
    @Expose
    private Long makerCommission;
    @SerializedName("takerCommission")
    @Expose
    private Long takerCommission;
    @SerializedName("buyerCommission")
    @Expose
    private Long buyerCommission;
    @SerializedName("sellerCommission")
    @Expose
    private Long sellerCommission;
    @SerializedName("canTrade")
    @Expose
    private Boolean canTrade;
    @SerializedName("canWithdraw")
    @Expose
    private Boolean canWithdraw;
    @SerializedName("canDeposit")
    @Expose
    private Boolean canDeposit;
    @SerializedName("balances")
    @Expose
    private List<BinanceBalance> balances = null;
    public final static Parcelable.Creator<BinanceAccountInfo> CREATOR = new Creator<BinanceAccountInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceAccountInfo createFromParcel(Parcel in) {
            return new BinanceAccountInfo(in);
        }

        public BinanceAccountInfo[] newArray(int size) {
            return (new BinanceAccountInfo[size]);
        }

    };

    protected BinanceAccountInfo(Parcel in) {
        this.makerCommission = ((Long) in.readValue((Long.class.getClassLoader())));
        this.takerCommission = ((Long) in.readValue((Long.class.getClassLoader())));
        this.buyerCommission = ((Long) in.readValue((Long.class.getClassLoader())));
        this.sellerCommission = ((Long) in.readValue((Long.class.getClassLoader())));
        this.canTrade = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.canWithdraw = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.canDeposit = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.balances, (BinanceBalance.class.getClassLoader()));
    }

    public BinanceAccountInfo() {
    }

    public Long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(Long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public Long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(Long takerCommission) {
        this.takerCommission = takerCommission;
    }

    public Long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public Long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(Long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public Boolean getCanTrade() {
        return canTrade;
    }

    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    public Boolean getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(Boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public Boolean getCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(Boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public List<BinanceBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<BinanceBalance> balances) {
        this.balances = balances;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(makerCommission);
        dest.writeValue(takerCommission);
        dest.writeValue(buyerCommission);
        dest.writeValue(sellerCommission);
        dest.writeValue(canTrade);
        dest.writeValue(canWithdraw);
        dest.writeValue(canDeposit);
        dest.writeList(balances);
    }

    public int describeContents() {
        return 0;
    }

}


