package urbanutility.design.kaleidoscope.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceTrade implements Parcelable
{

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("side")
    @Expose
    private String side;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("baseAsset")
    @Expose
    private String baseAsset;
    @SerializedName("activeBuy")
    @Expose
    private Boolean activeBuy;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("realPnl")
    @Expose
    private Long realPnl;
    @SerializedName("feeAsset")
    @Expose
    private String feeAsset;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("totalQuota")
    @Expose
    private String totalQuota;
    @SerializedName("tradeId")
    @Expose
    private Long tradeId;
    @SerializedName("quoteAsset")
    @Expose
    private String quoteAsset;
    public final static Parcelable.Creator<BinanceTrade> CREATOR = new Creator<BinanceTrade>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceTrade createFromParcel(Parcel in) {
            return new BinanceTrade(in);
        }

        public BinanceTrade[] newArray(int size) {
            return (new BinanceTrade[size]);
        }

    }
            ;

    protected BinanceTrade(Parcel in) {
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
        this.side = ((String) in.readValue((String.class.getClassLoader())));
        this.fee = ((String) in.readValue((String.class.getClassLoader())));
        this.baseAsset = ((String) in.readValue((String.class.getClassLoader())));
        this.activeBuy = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.price = ((String) in.readValue((String.class.getClassLoader())));
        this.qty = ((String) in.readValue((String.class.getClassLoader())));
        this.realPnl = ((Long) in.readValue((Long.class.getClassLoader())));
        this.feeAsset = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.time = ((Long) in.readValue((Long.class.getClassLoader())));
        this.totalQuota = ((String) in.readValue((String.class.getClassLoader())));
        this.tradeId = ((Long) in.readValue((Long.class.getClassLoader())));
        this.quoteAsset = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BinanceTrade() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public Boolean getActiveBuy() {
        return activeBuy;
    }

    public void setActiveBuy(Boolean activeBuy) {
        this.activeBuy = activeBuy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Long getRealPnl() {
        return realPnl;
    }

    public void setRealPnl(Long realPnl) {
        this.realPnl = realPnl;
    }

    public String getFeeAsset() {
        return feeAsset;
    }

    public void setFeeAsset(String feeAsset) {
        this.feeAsset = feeAsset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTotalQuota() {
        return totalQuota;
    }

    public void setTotalQuota(String totalQuota) {
        this.totalQuota = totalQuota;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(symbol);
        dest.writeValue(side);
        dest.writeValue(fee);
        dest.writeValue(baseAsset);
        dest.writeValue(activeBuy);
        dest.writeValue(price);
        dest.writeValue(qty);
        dest.writeValue(realPnl);
        dest.writeValue(feeAsset);
        dest.writeValue(id);
        dest.writeValue(time);
        dest.writeValue(totalQuota);
        dest.writeValue(tradeId);
        dest.writeValue(quoteAsset);
    }

    public int describeContents() {
        return 0;
    }

}