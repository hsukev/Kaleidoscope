package urbanutility.design.kaleidoscope.exchange.cryptopia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CryptopiaMarkets implements Parcelable
{

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Data")
    @Expose
    private List<CryptopiaOrder> data = null;
    public final static Parcelable.Creator<CryptopiaMarkets> CREATOR = new Creator<CryptopiaMarkets>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CryptopiaMarkets createFromParcel(Parcel in) {
            return new CryptopiaMarkets(in);
        }

        public CryptopiaMarkets[] newArray(int size) {
            return (new CryptopiaMarkets[size]);
        }

    }
            ;

    protected CryptopiaMarkets(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((Object) in.readValue((Object.class.getClassLoader())));
        in.readList(this.data, (CryptopiaOrder.class.getClassLoader()));
    }

    public CryptopiaMarkets() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<CryptopiaOrder> getData() {
        return data;
    }

    public void setData(List<CryptopiaOrder> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}