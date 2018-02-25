package urbanutility.design.kaleidoscope.exchange.binance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceWithdrawList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("asset")
    @Expose
    private String asset;
    @SerializedName("txId")
    @Expose
    private String txId;
    @SerializedName("applyTime")
    @Expose
    private Long applyTime;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("addressTag")
    @Expose
    private String addressTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BinanceWithdrawList withId(String id) {
        this.id = id;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public BinanceWithdrawList withAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BinanceWithdrawList withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BinanceWithdrawList withAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public BinanceWithdrawList withTxId(String txId) {
        this.txId = txId;
        return this;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public BinanceWithdrawList withApplyTime(Long applyTime) {
        this.applyTime = applyTime;
        return this;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public BinanceWithdrawList withStatus(Long status) {
        this.status = status;
        return this;
    }

    public String getAddressTag() {
        return addressTag;
    }

    public void setAddressTag(String addressTag) {
        this.addressTag = addressTag;
    }

    public BinanceWithdrawList withAddressTag(String addressTag) {
        this.addressTag = addressTag;
        return this;
    }

}