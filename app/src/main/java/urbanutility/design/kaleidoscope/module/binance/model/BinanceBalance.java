package urbanutility.design.kaleidoscope.module.binance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceBalance implements Parcelable
{

    @SerializedName("asset")
    @Expose
    private String asset;
    @SerializedName("free")
    @Expose
    private String free;
    @SerializedName("locked")
    @Expose
    private String locked;
    public final static Parcelable.Creator<BinanceBalance> CREATOR = new Creator<BinanceBalance>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceBalance createFromParcel(Parcel in) {
            return new BinanceBalance(in);
        }

        public BinanceBalance[] newArray(int size) {
            return (new BinanceBalance[size]);
        }

    }
            ;

    protected BinanceBalance(Parcel in) {
        this.asset = ((String) in.readValue((String.class.getClassLoader())));
        this.free = ((String) in.readValue((String.class.getClassLoader())));
        this.locked = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BinanceBalance() {
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(asset);
        dest.writeValue(free);
        dest.writeValue(locked);
    }

    public int describeContents() {
        return 0;
    }

}