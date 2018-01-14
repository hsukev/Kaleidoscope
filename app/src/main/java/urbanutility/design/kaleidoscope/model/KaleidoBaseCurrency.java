package urbanutility.design.kaleidoscope.model;

import java.util.ArrayList;

import urbanutility.design.kaleidoscope.datatypes.BaseCurrencyType;
import urbanutility.design.kaleidoscope.datatypes.PositionType;

/**
 * Created by jerhan on 1/11/2018.
 */

public class KaleidoBaseCurrency {
    private BaseCurrencyType baseCurrencyType;

    public KaleidoBaseCurrency(String exchange) {
        this.baseCurrencyType.type = exchange;
        this.baseCurrencyType.positions = new ArrayList<>();
    }
    public BaseCurrencyType getBaseCurrencyType() {
        return this.baseCurrencyType;
    }

    public void setBaseCurrencyType(BaseCurrencyType type) {
        this.baseCurrencyType = type;
    }
}

