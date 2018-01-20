package urbanutility.design.kaleidoscope.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static <T, N extends Number> String addMilliISO8601 (T t, N minute) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date time = sFormat.parse(t.toString());
        Calendar cal =Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MINUTE, minute.intValue());
        time = cal.getTime();
        return sFormat.format(time);
    }
}
