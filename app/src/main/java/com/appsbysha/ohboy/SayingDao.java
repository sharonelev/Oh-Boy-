package com.appsbysha.ohboy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.Saying;
import java.util.List;

@Dao
public interface SayingDao {
  

    @Insert
    void insert(Saying saying);

    @Insert
    void insert(Child child);

    @Update
    void update(Saying saying);

    @Update
    void update(Child child);

    @Delete
    void delete(Saying saying);

    @Delete
    void delete(Child child);

    @Query("DELETE FROM saying_table")
    void deleteAllSayings();

    @Query("DELETE FROM children_table")
    void deleteAllChildren();

    @Query("SELECT * FROM saying_table WHERE childId = :inputId ORDER BY date DESC")
    LiveData<List<Saying>> getAllSayings(int inputId);

    @Query("SELECT * FROM children_table ORDER BY dob")
    LiveData<List<Child>> getAllChildren();
}
