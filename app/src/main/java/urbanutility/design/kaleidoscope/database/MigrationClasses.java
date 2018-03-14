package urbanutility.design.kaleidoscope.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * Created by jerye on 2/25/2018.
 */

public class MigrationClasses {

   public static Migration MIGRATION_14_15 = new Migration(14,15){
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE `kaleido_deposits` (`id` TEXT," +
//                    " `side` TEXT, `exchange` TEXT, `amount` DOUBLE, `sumbol` TEXT, " +
//                    "`commission' DOUBLE, PRIMARY KEY(`id`))");
        }
    };

   public static Migration MIGRATION_15_16 = new Migration(15,16) {
       @Override
       public void migrate(@NonNull SupportSQLiteDatabase database) {
           database.execSQL("ALTER TABLE kaleido_deposits ADD COLUMN time TEXT");
       }
   };
}
