package urbanutility.design.kaleidoscope.exchange.cryptopia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptopiaBalance implements Parcelable
{

    @SerializedName("Symbol")
    @Expose
    private String symbol;
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("Available")
    @Expose
    private String available;

    public final static Creator<CryptopiaBalance> CREATOR = new Creator<CryptopiaBalance>() {
        @SuppressWarnings({
                "unchecked"
        })
        public CryptopiaBalance createFromParcel(Parcel in) {
            return new CryptopiaBalance(in);
        }

        public CryptopiaBalance[] newArray(int size) {
            return (new CryptopiaBalance[size]);
        }
    };

    protected CryptopiaBalance(Parcel in) {
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
        this.total = ((String) in.readValue((String.class.getClassLoader())));
        this.available = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CryptopiaBalance() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(symbol);
        dest.writeValue(total);
        dest.writeValue(available);
    }

    public int describeContents() {
        return 0;
    }

}