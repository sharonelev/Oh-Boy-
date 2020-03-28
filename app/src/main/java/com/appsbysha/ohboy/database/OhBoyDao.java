package com.appsbysha.ohboy.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.Saying;
import com.appsbysha.ohboy.entities.SayingWithLines;
import com.appsbysha.ohboy.entities.Line;
import java.util.List;

@Dao
public interface OhBoyDao {


  @Insert
  void insert(Saying saying);

  @Insert
  void insert(Child child);

  @Insert
  void insert(Line line);


  @Update
  void update(Saying saying);

  @Update
  void update(Child child);

  @Update
  void update(Line line);

  @Delete
  void delete(Saying saying);

  @Delete
  void delete(Child child);

  @Delete
  void delete(Line line);

  @Query("DELETE FROM saying_table")
  void deleteAllSayings();

  @Query("DELETE FROM lines_table")
  void deleteAllLines();

  @Query("DELETE FROM children_table")
  void deleteAllChildren();

  @Query("SELECT * FROM lines_table a JOIN (SELECT sayingId from saying_table WHERE childId = :inputChildId) b ON a.lineSayingId = b.sayingId WHERE lineSayingId = :inputSayingId ORDER BY position")
  LiveData<List<Line>> getAllLines(int inputChildId, String inputSayingId);

  @Query("SELECT * FROM lines_table a JOIN (SELECT sayingId from saying_table WHERE childId = :inputChildId) b ON a.lineSayingId = b.sayingId WHERE lineSayingId = :inputSayingId ORDER BY position")
  List<Line> getAllLinesView(int inputChildId, String inputSayingId);

  @Query("SELECT * FROM saying_table WHERE childId = :inputId ORDER BY date DESC")
  LiveData<List<Saying>> getAllSayings(int inputId);

  @Query("SELECT * FROM children_table ORDER BY dob")
  LiveData<List<Child>> getAllChildren();


  @Transaction
  @Query("SELECT * FROM saying_table")
  public List<SayingWithLines> getSayingWithLines();

}
