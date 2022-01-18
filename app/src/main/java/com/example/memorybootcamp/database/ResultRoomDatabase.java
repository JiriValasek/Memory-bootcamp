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

/** Result room database singleton. */
@Database(entities = {Result.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ResultRoomDatabase extends RoomDatabase {

    /** Database access object to the DB. */
    public abstract ResultDao resultDao();
    /** Room DB for results. */
    private static volatile ResultRoomDatabase INSTANCE;
    /** Number of threads to be used for write tasks. */
    private static final int NUMBER_OF_THREADS = 4;
    /** Executors for the DB. */
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    /** Room DB callback. */
    private final static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        /** Method executing commands during each opening. */
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
                    Result result = new Result(0, 0, OffsetDateTime.now(),ct);
                    dao.insert(result);
                    dao.leaveOnlyOneRecordOfTheDay(ct);
                }
            });
        }

        /** Method executing commands during creating of the DB. */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // do something after database has been created
            // for the first time and after all the tables are created.
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more types, just add them.
                ResultDao dao = INSTANCE.resultDao();
                dao.deleteAll();
                String[] challengeTypes = {"binary","cards","faces","numbers","words"};
                for (String ct:challengeTypes) {
                    Result result = new Result(0, 0, OffsetDateTime.now(),ct);
                    dao.insert(result);
                }
            });
        }
    };

    /** Getter for the DB. */
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