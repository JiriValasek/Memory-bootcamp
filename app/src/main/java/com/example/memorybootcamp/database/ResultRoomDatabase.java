package com.example.memorybootcamp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.OffsetDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Result.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ResultRoomDatabase extends RoomDatabase {

    public abstract ResultDao resultDao();

    private static volatile ResultRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            //TODO onOpen add 0 results
            //TODO onClose prune 0 results except the oldest ones
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ResultDao dao = INSTANCE.resultDao();
                dao.pruneEmptyRows();
                String[] challengeTypes = {"binary","cards","faces","numbers","words"};
                for (String ct:challengeTypes) {
                    Result result = new Result(0, 0,
                            OffsetDateTime.now(),ct);
                    dao.insert(result);
                    dao.leaveOnlyOneRecordOfTheDay(ct);
                }
            });
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // do something after database has been created
            // for the first time and after all the tables are created.
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                ResultDao dao = INSTANCE.resultDao();
                dao.deleteAll();
                String[] challengeTypes = {"binary","cards","faces","numbers","words"};
                for (String ct:challengeTypes) {
                    Result result = new Result(0, 0,
                            OffsetDateTime.now(),ct);
                    dao.insert(result);
                }
            });
        }
    };

    static ResultRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ResultRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ResultRoomDatabase.class, "results_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}