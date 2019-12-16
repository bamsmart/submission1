package learning.shinescdev.jetpack.data.source.local.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;

@Database(entities = {MovieEntity.class, TVEntity.class},
        version = 1,
        exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static LocalDatabase INSTANCE;

    public static LocalDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LocalDatabase.class, "Movie.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract MovieDao movieDao();
    public abstract TVDao tvDao();

}
