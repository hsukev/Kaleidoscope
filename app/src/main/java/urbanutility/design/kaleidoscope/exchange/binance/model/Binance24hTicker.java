package urbanutility.design.kaleidoscope.exchange.binance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Binance24hTicker implements Parcelable
{

    @SerializedName("priceChange")
    @Expose
    private String priceChange;
    @SerializedName("priceChangePercent")
    @Expose
    private String priceChangePercent;
    @SerializedName("weightedAvgPrice")
    @Expose
    private String weightedAvgPrice;
    @SerializedName("prevClosePrice")
    @Expose
    private String prevClosePrice;
    @SerializedName("lastPrice")
    @Expose
    private String lastPrice;
    @SerializedName("bidPrice")
    @Expose
    private String bidPrice;
    @SerializedName("askPrice")
    @Expose
    private String askPrice;
    @SerializedName("openPrice")
    @Expose
    private String openPrice;
    @SerializedName("highPrice")
    @Expose
    private String highPrice;
    @SerializedName("lowPrice")
    @Expose
    private String lowPrice;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("openTime")
    @Expose
    private Long openTime;
    @SerializedName("closeTime")
    @Expose
    private Long closeTime;
    @SerializedName("fristId")
    @Expose
    private Long fristId;
    @SerializedName("lastId")
    @Expose
    private Long lastId;
    @SerializedName("count")
    @Expose
    private Long count;
    public final static Parcelable.Creator<Binance24hTicker> CREATOR = new Creator<Binance24hTicker>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Binance24hTicker createFromParcel(Parcel in) {
            return new Binance24hTicker(in);
        }

        public Binance24hTicker[] newArray(int size) {
            return (new Binance24hTicker[size]);
        }

    }
            ;

    protected Binance24hTicker(Parcel in) {
        this.priceChange = ((String) in.readValue((String.class.getClassLoader())));
        this.priceChangePercent = ((String) in.readValue((String.class.getClassLoader())));
        this.weightedAvgPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.prevClosePrice = ((String) in.readValue((String.class.getClassLoader())));
        this.lastPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.bidPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.askPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.openPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.highPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.lowPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.volume = ((String) in.readValue((String.class.getClassLoader())));
        this.openTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.closeTime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.fristId = ((Long) in.readValue((Long.class.getClassLoader())));
        this.lastId = ((Long) in.readValue((Long.class.getClassLoader())));
        this.count = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public Binance24hTicker() {
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public Long getFristId() {
        return fristId;
    }

    public void setFristId(Long fristId) {
        this.fristId = fristId;
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(priceChange);
        dest.writeValue(priceChangePercent);
        dest.writeValue(weightedAvgPrice);
        dest.writeValue(prevClosePrice);
        dest.writeValue(lastPrice);
        dest.writeValue(bidPrice);
        dest.writeValue(askPrice);
        dest.writeValue(openPrice);
        dest.writeValue(highPrice);
        dest.writeValue(lowPrice);
        dest.writeValue(volume);
        dest.writeValue(openTime);
        dest.writeValue(closeTime);
        dest.writeValue(fristId);
        dest.writeValue(lastId);
        dest.writeValue(count);
    }

    public int describeContents() {
        return 0;
    }

}