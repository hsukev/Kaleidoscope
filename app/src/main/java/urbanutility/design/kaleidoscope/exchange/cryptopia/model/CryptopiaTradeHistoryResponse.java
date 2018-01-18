package urbanutility.design.kaleidoscope.exchange.cryptopia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CryptopiaTradeHistoryResponse implements Parcelable
{

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Data")
    @Expose
    private List<CryptopiaTradeHistory> data = null;
    public final static Creator<CryptopiaTradeHistoryResponse> CREATOR = new Creator<CryptopiaTradeHistoryResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CryptopiaTradeHistoryResponse createFromParcel(Parcel in) {
            return new CryptopiaTradeHistoryResponse(in);
        }

        public CryptopiaTradeHistoryResponse[] newArray(int size) {
            return (new CryptopiaTradeHistoryResponse[size]);
        }

    };

    protected CryptopiaTradeHistoryResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((Object) in.readValue((Object.class.getClassLoader())));
        in.readList(this.data, (CryptopiaOrder.class.getClassLoader()));
    }

    public CryptopiaTradeHistoryResponse() {
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

    public List<CryptopiaTradeHistory> getData() {
        return data;
    }

    public void setData(List<CryptopiaTradeHistory> data) {
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