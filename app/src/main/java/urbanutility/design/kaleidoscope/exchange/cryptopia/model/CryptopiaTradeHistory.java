package urbanutility.design.kaleidoscope.exchange.cryptopia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptopiaTradeHistory implements Parcelable
{

    @SerializedName("Market")
    @Expose
    private String market;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Rate")
    @Expose
    private String rate;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("Fee")
    @Expose
    private String fee;
    @SerializedName("TimeStamp")
    @Expose
    private String time;

    public final static Creator<CryptopiaTradeHistory> CREATOR = new Creator<CryptopiaTradeHistory>() {
        @SuppressWarnings({
                "unchecked"
        })
        public CryptopiaTradeHistory createFromParcel(Parcel in) {
            return new CryptopiaTradeHistory(in);
        }

        public CryptopiaTradeHistory[] newArray(int size) {
            return (new CryptopiaTradeHistory[size]);
        }
    };

    protected CryptopiaTradeHistory(Parcel in) {
        this.market = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.rate = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
        this.total = ((String) in.readValue((String.class.getClassLoader())));
        this.fee = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CryptopiaTradeHistory() {
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(market);
        dest.writeValue(type);
        dest.writeValue(rate);
        dest.writeValue(amount);
        dest.writeValue(total);
        dest.writeValue(fee);
        dest.writeValue(time);
    }

    public int describeContents() {
        return 0;
    }

}