package urbanutility.design.kaleidoscope.exchange.binance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BinanceDepositList {

    @SerializedName("insertTime")
    @Expose
    private Long insertTime;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("asset")
    @Expose
    private String asset;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("txId")
    @Expose
    private String txId;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("addressTag")
    @Expose
    private String addressTag;

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }

    public BinanceDepositList withInsertTime(Long insertTime) {
        this.insertTime = insertTime;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BinanceDepositList withAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BinanceDepositList withAsset(String asset) {
        this.asset = asset;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BinanceDepositList withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public BinanceDepositList withTxId(String txId) {
        this.txId = txId;
        return this;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public BinanceDepositList withStatus(Long status) {
        this.status = status;
        return this;
    }

    public String getAddressTag() {
        return addressTag;
    }

    public void setAddressTag(String addressTag) {
        this.addressTag = addressTag;
    }

    public BinanceDepositList withAddressTag(String addressTag) {
        this.addressTag = addressTag;
        return this;
    }

}