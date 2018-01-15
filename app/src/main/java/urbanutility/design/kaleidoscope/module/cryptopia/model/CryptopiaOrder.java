package urbanutility.design.kaleidoscope.module.cryptopia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptopiaOrder implements Parcelable
{

    @SerializedName("TradePairId")
    @Expose
    private Long tradePairId;
    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("AskPrice")
    @Expose
    private Double askPrice;
    @SerializedName("BidPrice")
    @Expose
    private Double bidPrice;
    @SerializedName("Low")
    @Expose
    private Double low;
    @SerializedName("High")
    @Expose
    private Double high;
    @SerializedName("Volume")
    @Expose
    private Double volume;
    @SerializedName("LastPrice")
    @Expose
    private Double lastPrice;
    @SerializedName("BuyVolume")
    @Expose
    private Double buyVolume;
    @SerializedName("SellVolume")
    @Expose
    private Double sellVolume;
    @SerializedName("Change")
    @Expose
    private Double change;
    @SerializedName("Open")
    @Expose
    private Double open;
    @SerializedName("Close")
    @Expose
    private Double close;
    @SerializedName("BaseVolume")
    @Expose
    private Double baseVolume;
    @SerializedName("BaseBuyVolume")
    @Expose
    private Double baseBuyVolume;
    @SerializedName("BaseSellVolume")
    @Expose
    private Double baseSellVolume;
    public final static Parcelable.Creator<CryptopiaOrder> CREATOR = new Creator<CryptopiaOrder>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CryptopiaOrder createFromParcel(Parcel in) {
            return new CryptopiaOrder(in);
        }

        public CryptopiaOrder[] newArray(int size) {
            return (new CryptopiaOrder[size]);
        }

    }
            ;

    protected CryptopiaOrder(Parcel in) {
        this.tradePairId = ((Long) in.readValue((Long.class.getClassLoader())));
        this.label = ((String) in.readValue((String.class.getClassLoader())));
        this.askPrice = ((Double) in.readValue((Double.class.getClassLoader())));
        this.bidPrice = ((Double) in.readValue((Double.class.getClassLoader())));
        this.low = ((Double) in.readValue((Double.class.getClassLoader())));
        this.high = ((Double) in.readValue((Double.class.getClassLoader())));
        this.volume = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lastPrice = ((Double) in.readValue((Double.class.getClassLoader())));
        this.buyVolume = ((Double) in.readValue((Double.class.getClassLoader())));
        this.sellVolume = ((Double) in.readValue((Double.class.getClassLoader())));
        this.change = ((Double) in.readValue((Double.class.getClassLoader())));
        this.open = ((Double) in.readValue((Double.class.getClassLoader())));
        this.close = ((Double) in.readValue((Double.class.getClassLoader())));
        this.baseVolume = ((Double) in.readValue((Double.class.getClassLoader())));
        this.baseBuyVolume = ((Double) in.readValue((Double.class.getClassLoader())));
        this.baseSellVolume = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public CryptopiaOrder() {
    }

    public Long getTradePairId() {
        return tradePairId;
    }

    public void setTradePairId(Long tradePairId) {
        this.tradePairId = tradePairId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(Double buyVolume) {
        this.buyVolume = buyVolume;
    }

    public Double getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(Double sellVolume) {
        this.sellVolume = sellVolume;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(Double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public Double getBaseBuyVolume() {
        return baseBuyVolume;
    }

    public void setBaseBuyVolume(Double baseBuyVolume) {
        this.baseBuyVolume = baseBuyVolume;
    }

    public Double getBaseSellVolume() {
        return baseSellVolume;
    }

    public void setBaseSellVolume(Double baseSellVolume) {
        this.baseSellVolume = baseSellVolume;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(tradePairId);
        dest.writeValue(label);
        dest.writeValue(askPrice);
        dest.writeValue(bidPrice);
        dest.writeValue(low);
        dest.writeValue(high);
        dest.writeValue(volume);
        dest.writeValue(lastPrice);
        dest.writeValue(buyVolume);
        dest.writeValue(sellVolume);
        dest.writeValue(change);
        dest.writeValue(open);
        dest.writeValue(close);
        dest.writeValue(baseVolume);
        dest.writeValue(baseBuyVolume);
        dest.writeValue(baseSellVolume);
    }

    public int describeContents() {
        return 0;
    }

}