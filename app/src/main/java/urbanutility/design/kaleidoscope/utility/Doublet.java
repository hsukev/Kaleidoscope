package urbanutility.design.kaleidoscope.utility;

/**
 * Created by jerye on 1/18/2018.
 */

public class Doublet<F, S> {
    private F first;
    private S second;

    public Doublet(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

}
