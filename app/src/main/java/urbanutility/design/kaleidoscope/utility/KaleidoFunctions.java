package urbanutility.design.kaleidoscope.utility;

import android.util.Log;

import com.ibm.icu.text.DecimalFormat;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import urbanutility.design.kaleidoscope.datatypes.LiveMarketType;

/**
 * Created by jerye on 1/17/2018.
 */

public class KaleidoFunctions {
    private static DecimalFormatSymbols DFS = new DecimalFormatSymbols();
    private static DecimalFormat myFormatter = new DecimalFormat();
    private static final int maxSigFigUnder1 = 4;

    public static <T> String convertMilliISO8601(T t) {
        long milli = Long.parseLong(t.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date(milli);
        return sdf.format(date);
    }

    public static <T, N extends Number> String addMilliISO8601(T t, N minute) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date time = new Date();
        try {
            time = sFormat.parse(t.toString());

        } catch (ParseException e) {
            Log.e("KaleidoFunctions", e.getMessage());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MINUTE, minute.intValue());
        time = cal.getTime();
        return sFormat.format(time);
    }

    public static String convertSymbol(String[] baseAlts, String originalSymbol) {
        int length = originalSymbol.length();
        if (originalSymbol.contains("USDT")) {
            return "BTCUSDT";
        } else if (originalSymbol.contains("BTC")) {
            return originalSymbol;
        } else {
            return originalSymbol.substring(0, length - 3) + "BTC";
        }
    }


    public static String doubleToFormatedString(double value) {
        if(value>-1 && value <1){
            myFormatter.setSignificantDigitsUsed(true);
            myFormatter.setMaximumSignificantDigits(maxSigFigUnder1);
        }else{
            myFormatter.setMaximumFractionDigits(3);
        }
        return myFormatter.format(value);
    }

    public static String createLiveMarketId(LiveMarketType liveMarketType){

        return liveMarketType.exchange + "-" + liveMarketType.symbol.substring(0,liveMarketType.symbol.length()-3);
    }

    public static String[] decodeBalanceId(String balanceId){
        int dashIndex = balanceId.indexOf('-');
        return new String[]{balanceId.substring(0,dashIndex), balanceId.substring(dashIndex+1)};
    }


}
