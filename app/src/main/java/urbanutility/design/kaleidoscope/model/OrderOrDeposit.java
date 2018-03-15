package urbanutility.design.kaleidoscope.model;

/**
 * Created by jerye on 3/14/2018.
 */

public class OrderOrDeposit {
    private KaleidoOrder kaleidoOrder;
    private KaleidoDeposits kaleidoDeposits;

    public OrderOrDeposit(KaleidoOrder kaleidoOrder){
        this.kaleidoOrder = kaleidoOrder;
        this.kaleidoDeposits = null;
    }

    public OrderOrDeposit(KaleidoDeposits kaleidoDeposits){
        this.kaleidoDeposits = kaleidoDeposits;
        this.kaleidoOrder = null;
    }
    public KaleidoOrder getKaleidoOrder() {
        return kaleidoOrder;
    }

    public void setKaleidoOrder(KaleidoOrder kaleidoOrder) {
        this.kaleidoOrder = kaleidoOrder;
    }

    public KaleidoDeposits getKaleidoDeposits() {
        return kaleidoDeposits;
    }

    public void setKaleidoDeposits(KaleidoDeposits kaleidoDeposits) {
        this.kaleidoDeposits = kaleidoDeposits;
    }

    public boolean isOrder(){
        return kaleidoOrder!=null;
    }

}
