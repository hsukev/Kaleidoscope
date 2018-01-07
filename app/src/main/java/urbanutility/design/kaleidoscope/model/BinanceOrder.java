
package urbanutility.design.kaleidoscope.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceOrder implements Parcelable {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("clientOrderId")
    @Expose
    private String clientOrderId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("origQty")
    @Expose
    private String origQty;
    @SerializedName("executedQty")
    @Expose
    private String executedQty;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timeInForce")
    @Expose
    private String timeInForce;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("side")
    @Expose
    private String side;
    @SerializedName("stopPrice")
    @Expose
    private String stopPrice;
    @SerializedName("icebergQty")
    @Expose
    private String icebergQty;
    @SerializedName("time")
    @Expose
    private Long time;
    public final static Parcelable.Creator<BinanceOrder> CREATOR = new Creator<BinanceOrder>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceOrder createFromParcel(Parcel in) {
            return new BinanceOrder(in);
        }

        public BinanceOrder[] newArray(int size) {
            return (new BinanceOrder[size]);
        }

    };

    protected BinanceOrder(Parcel in) {
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
        this.orderId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.clientOrderId = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((String) in.readValue((String.class.getClassLoader())));
        this.origQty = ((String) in.readValue((String.class.getClassLoader())));
        this.executedQty = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.timeInForce = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.side = ((String) in.readValue((String.class.getClassLoader())));
        this.stopPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.icebergQty = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public BinanceOrder() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrigQty() {
        return origQty;
    }

    public void setOrigQty(String origQty) {
        this.origQty = origQty;
    }

    public String getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(String executedQty) {
        this.executedQty = executedQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getIcebergQty() {
        return icebergQty;
    }

    public void setIcebergQty(String icebergQty) {
        this.icebergQty = icebergQty;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(symbol);
        dest.writeValue(orderId);
        dest.writeValue(clientOrderId);
        dest.writeValue(price);
        dest.writeValue(origQty);
        dest.writeValue(executedQty);
        dest.writeValue(status);
        dest.writeValue(timeInForce);
        dest.writeValue(type);
        dest.writeValue(side);
        dest.writeValue(stopPrice);
        dest.writeValue(icebergQty);
        dest.writeValue(time);
    }

    public int describeContents() {
        return 0;
    }

}