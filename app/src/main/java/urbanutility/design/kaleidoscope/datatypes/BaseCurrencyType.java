package urbanutility.design.kaleidoscope.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 1/14/2018.
 */

public class BaseCurrencyType {
    public String type; /* BTC or USD */
    public ArrayList<PositionType> positions;

    public BaseCurrencyType () {
    }
}