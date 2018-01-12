package urbanutility.design.kaleidoscope.module.binance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinancePriceTicker implements Parcelable
{

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("price")
    @Expose
    private String price;
    public final static Parcelable.Creator<BinancePriceTicker> CREATOR = new Creator<BinancePriceTicker>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinancePriceTicker createFromParcel(Parcel in) {
            return new BinancePriceTicker(in);
        }

        public BinancePriceTicker[] newArray(int size) {
            return (new BinancePriceTicker[size]);
        }

    }
            ;

    protected BinancePriceTicker(Parcel in) {
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BinancePriceTicker() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(symbol);
        dest.writeValue(price);
    }

    public int describeContents() {
        return 0;
    }

}