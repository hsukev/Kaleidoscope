package urbanutility.design.kaleidoscope.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import urbanutility.design.kaleidoscope.model.KaleidoBalance;
import urbanutility.design.kaleidoscope.model.KaleidoDeposits;
import urbanutility.design.kaleidoscope.model.KaleidoOrder;

/**
 * Created by jerye on 1/8/2018.
 */

@Database(entities = {KaleidoOrder.class, KaleidoBalance.class, KaleidoDeposits.class}, version = 15)
public abstract class KaleidoDatabase extends RoomDatabase {

    private static KaleidoDatabase INSTANCE;

    public abstract KaleidoDao kaleidoDao();

    public static KaleidoDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), KaleidoDatabase.class, "kaleidoscope_database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .addMigrations(MigrationClasses.MIGRATION_14_15)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

