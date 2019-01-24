package com.mpush.demo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/21 16:35
 * @des
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase INSTANCE;

    private static final Object SLOCK = new Object();

    private static final String DB_NAME = "app_catch_v";

    private static final int DB_VERSION = 1;

    private static String DB_FILE_NAME = DB_NAME + DB_VERSION + ".db";

    public static void init(Context context) {
        synchronized (SLOCK) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DB_FILE_NAME)
                        .fallbackToDestructiveMigration()//如果更新版本，没有加入迁移，则会删除所有数据
                        .build();
            }

        }
    }

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("u must initialization me first!!!");
        }
        return INSTANCE;
    }

    /**
     * 关闭链接
     */
    @Override
    public void close() {
        try {
            if (INSTANCE != null) {
                INSTANCE.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public abstract UserDao userDao();
}
