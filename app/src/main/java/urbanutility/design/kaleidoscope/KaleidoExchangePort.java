package urbanutility.design.kaleidoscope;

import android.preference.PreferenceManager;
import android.util.Log;

import urbanutility.design.kaleidoscope.exchange.binance.client.BinanceChainRequestor;

/**
 * Created by jerye on 1/12/2018.
 */

public class KaleidoExchangePort {
    private String TAG = getClass().getName();
    private HistoryFragment historyFragment;
    public KaleidoExchangePort(HistoryFragment historyFragment){
        this.historyFragment = historyFragment;
    }

    public void addExchange(String exchangeName){
        ChainRequestor chainRequestor;
        switch(exchangeName){
            case "binance":
                chainRequestor = new BinanceChainRequestor(historyFragment);
                break;
//            case "cryptopia":
//                chainRequestor = new CryptopiaChainRequestor(historyFragment);
//                break;
            default:
                chainRequestor = null;
        }

        try{
            chainRequestor.requestAndInsert();
        }catch (NullPointerException e){
            Log.e(TAG,"Exchange is not currently supported or does not exist");
        }
    }

    public void UpdateAll(){
        PreferenceManager.getDefaultSharedPreferences(historyFragment.getContext());
    }
}
