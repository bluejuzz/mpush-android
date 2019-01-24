package com.mpush.demo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/21 16:37
 * @des
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE monoid = :monoid")
    User getUserById(String monoid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsersAndFriends(User user, List<User> friends);

    @Delete
    void delete(User user);
}
