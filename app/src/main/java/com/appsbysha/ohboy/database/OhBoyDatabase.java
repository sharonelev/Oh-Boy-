package com.appsbysha.ohboy.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.Saying;
import com.appsbysha.ohboy.entities.Line;

@Database(entities = {Saying.class, Child.class, Line.class}, version = 2, exportSchema = false)
public abstract class OhBoyDatabase extends RoomDatabase {

  private static OhBoyDatabase instance;

  public abstract OhBoyDao ohBoyDao();

  static synchronized OhBoyDatabase getInstance(Context context) {
    if (instance == null) {
 //    context.deleteDatabase("ohboy_database"); //remove if no changes made to db
      instance = Room.databaseBuilder(context.getApplicationContext(),
          OhBoyDatabase.class, "ohboy_database")
          .fallbackToDestructiveMigration()
          .addCallback(roomCallback)
          .build();
    }
    return instance;
  }

  //pre populate table
  private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      super.onCreate(db);
      new PopulateDbAsyncTask(instance).execute();
    }
  };

  private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    private OhBoyDao ohBoyDao;

    private PopulateDbAsyncTask(OhBoyDatabase db) {
      ohBoyDao = db.ohBoyDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
     /* ohBoyDao.insert(new Saying(123,"Title 8", "Description 1", "21/05/2018", null));
      ohBoyDao.insert(new Saying(456,"Title 2", "Description 2", "03/03/2019",null));
      ohBoyDao.insert(new Saying(123,"Title 3", "Description 3", "31/12/2018",  null));
      ohBoyDao.insert(new Child(123,"Yoav", "15/8/14"));
      ohBoyDao.insert(new Child(456,"Adi", "16/2/17"));*/
      return null;
    }
  }

}
