package urbanutility.design.kaleidoscope.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BinanceUserTrades implements Parcelable
{

    @SerializedName("total")
    @Expose
    private Long total;
    @SerializedName("pages")
    @Expose
    private Long pages;
    @SerializedName("data")
    @Expose
    private List<BinanceTrade> data = null;
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("rows")
    @Expose
    private Long rows;
    public final static Parcelable.Creator<BinanceUserTrades> CREATOR = new Creator<BinanceUserTrades>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BinanceUserTrades createFromParcel(Parcel in) {
            return new BinanceUserTrades(in);
        }

        public BinanceUserTrades[] newArray(int size) {
            return (new BinanceUserTrades[size]);
        }

    }
            ;

    protected BinanceUserTrades(Parcel in) {
        this.total = ((Long) in.readValue((Long.class.getClassLoader())));
        this.pages = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.data, (urbanutility.design.kaleidoscope.model.BinanceTrade.class.getClassLoader()));
        this.page = ((Long) in.readValue((Long.class.getClassLoader())));
        this.rows = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public BinanceUserTrades() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public List<BinanceTrade> getData() {
        return data;
    }

    public void setData(List<BinanceTrade> data) {
        this.data = data;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        this.rows = rows;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(total);
        dest.writeValue(pages);
        dest.writeList(data);
        dest.writeValue(page);
        dest.writeValue(rows);
    }

    public int describeContents() {
        return 0;
    }

}