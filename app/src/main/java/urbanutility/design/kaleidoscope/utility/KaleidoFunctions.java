package urbanutility.design.kaleidoscope.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jerye on 1/17/2018.
 */

public class KaleidoFunctions {

    public static <T> String convertMilliISO8601(T t){
        long milli = Long.parseLong(t.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date(milli);
        return sdf.format(date);
    }
}
