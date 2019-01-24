package com.mpush.demo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/21 16:36
 * @des
 */
@Entity
public class User {
    private String name;
    private String age;
    @PrimaryKey
    @NonNull
    private String monoid;

    public User(String name, String age, @NonNull String monoid) {
        this.name = name;
        this.age = age;
        this.monoid = monoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMonoid() {
        return monoid;
    }

    public void setMonoid(String monoid) {
        this.monoid = monoid;
    }
}
