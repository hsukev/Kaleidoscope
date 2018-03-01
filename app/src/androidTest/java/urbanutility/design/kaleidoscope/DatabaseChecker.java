//package urbanutility.design.kaleidoscope;
//
//import android.arch.persistence.room.Room;
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.io.IOException;
//
//import urbanutility.design.kaleidoscope.database.KaleidoDao;
//import urbanutility.design.kaleidoscope.database.KaleidoDatabase;
//import urbanutility.design.kaleidoscope.exchange.binance.model.BinanceOrder;
//import urbanutility.design.kaleidoscope.model.KaleidoOrder;
//import urbanutility.design.kaleidoscope.util.TestBinanceOrder;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by jerye on 1/8/2018.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class DatabaseChecker {
//    private KaleidoDao mKaleidoDao;
//    private KaleidoDatabase mDb;
//
//    @Before
//    public void createDb(){
//        Context context = InstrumentationRegistry.getTargetContext();
//        Room.inMemoryDatabaseBuilder(context, KaleidoDatabase.class).build();
//        mDb=KaleidoDatabase.getAppDatabase(context);
//        mKaleidoDao = mDb.kaleidoDao();
//    }
//
//    @After
//    public void closeDb() throws IOException{
//        KaleidoDatabase.destroyInstance();
//        mDb.close();
//    }
//
//    @Test
//        public void writeOrderAndReadSymbol() throws  Exception {
//        BinanceOrder binanceOrder = TestBinanceOrder.createTestBinanceOrder();
//        mKaleidoDao.insertOrder(new KaleidoOrder(binanceOrder, 1.21122144));
//
//        assertEquals("NEOBTC", mKaleidoDao.getOrder("NEOBTC").getOrdertype().symbol);
//    }
//}
