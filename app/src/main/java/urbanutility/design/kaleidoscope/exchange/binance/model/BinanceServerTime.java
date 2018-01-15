package urbanutility.design.kaleidoscope.exchange.binance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceServerTime implements Parcelable
{

    @SerializedName("serverTime")
    @Expose
    private Long serverTime;
    public final static Parcelable.Creator<BinanceServerTime> CREATOR = new Creator<BinanceServerTime>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceServerTime createFromParcel(Parcel in) {
            return new BinanceServerTime(in);
        }

        public BinanceServerTime[] newArray(int size) {
            return (new BinanceServerTime[size]);
        }

    }
            ;

    protected BinanceServerTime(Parcel in) {
        this.serverTime = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public BinanceServerTime() {
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(serverTime);
    }

    public int describeContents() {
        return 0;
    }

}