package urbanutility.design.kaleidoscope.exchange.gdax.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GdaxTicker {

    @SerializedName("trade_id")
    @Expose
    private Long tradeId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("ask")
    @Expose
    private String ask;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("time")
    @Expose
    private String time;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public GdaxTicker withTradeId(Long tradeId) {
        this.tradeId = tradeId;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public GdaxTicker withPrice(String price) {
        this.price = price;
        return this;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public GdaxTicker withSize(String size) {
        this.size = size;
        return this;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public GdaxTicker withBid(String bid) {
        this.bid = bid;
        return this;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public GdaxTicker withAsk(String ask) {
        this.ask = ask;
        return this;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public GdaxTicker withVolume(String volume) {
        this.volume = volume;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GdaxTicker withTime(String time) {
        this.time = time;
        return this;
    }

}