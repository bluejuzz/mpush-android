package com.mpush.demo;

import android.app.Application;
import android.arch.persistence.room.Room;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/21 16:40
 * @des
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.init(this);
    }

}
