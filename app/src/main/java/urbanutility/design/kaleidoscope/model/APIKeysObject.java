package urbanutility.design.kaleidoscope.model;

import urbanutility.design.kaleidoscope.utility.KaleidoFunctions;

/**
 * Created by jerye on 4/20/2018.
 */

public class APIKeysObject {
    private String name;
    private String lastSynced;
    private String privateKey;
    private String publicKey;

    public APIKeysObject(String name, long lastSynced, String privateKey, String publicKey) {
        this.name = name;
        this.lastSynced = KaleidoFunctions.convertMilliISO8601(lastSynced);
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastSynced() {
        return lastSynced;
    }

    public void setLastSynced(String lastSynced) {
        this.lastSynced = lastSynced;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
