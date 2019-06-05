package com.appsbysha.ohboy;


import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.Saying;
import java.util.List;

public class SayingRepository {

  private SayingDao sayingDao;
  private LiveData<List<Saying>> allSayings;
  private LiveData<List<Child>> allChildren;
  private int childId;


  public SayingRepository(Application application) {  //all children list
    SayingDatabase database = SayingDatabase.getInstance(application);
    sayingDao = database.sayingDao();
    allChildren = sayingDao.getAllChildren();
  }

  public SayingRepository(Application application, int childId) { //all sayings per child
    SayingDatabase database = SayingDatabase.getInstance(application);
    sayingDao = database.sayingDao();
    this.childId = childId;
    allSayings = sayingDao.getAllSayings(this.childId);
  }

  public void insert(Saying saying) {
    new InsertSayingAsyncTask(sayingDao).execute(saying);
  }

  public void update(Saying saying) {
    new UpdateSayingAsyncTask(sayingDao).execute(saying);
  }

  public void delete(Saying saying) {
    new DeleteSayingAsyncTask(sayingDao).execute(saying);
  }

  public void deleteAllSayings() {
    new DeleteAllSayingsAsyncTask(sayingDao).execute();
  }

  public LiveData<List<Saying>> getAllSayings() {
    return allSayings;
  }

  public void insert(Child child) {
    new InsertChildAsyncTask(sayingDao).execute(child);
  }

  public void update(Child child) {

    new UpdateChildAsyncTask(sayingDao).execute(child);
  }

  public void delete(Child child) {
    new DeleteChildAsyncTask(sayingDao).execute(child);
  }

  public void deleteAllChildren() {
    new DeleteAllChildrenAsyncTask(sayingDao).execute();
  }


  public LiveData<List<Child>> getAllChildren() {
    return allChildren;
  }

  private static class InsertSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private SayingDao sayingDao;

    private InsertSayingAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      sayingDao.insert(sayings[0]);
      return null;
    }
  }

  private static class UpdateSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private SayingDao sayingDao;

    private UpdateSayingAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      sayingDao.update(sayings[0]);
      return null;
    }
  }

  private static class DeleteSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private SayingDao sayingDao;

    private DeleteSayingAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      sayingDao.delete(sayings[0]);
      return null;
    }
  }

  private static class DeleteAllSayingsAsyncTask extends AsyncTask<Void, Void, Void> {

    private SayingDao sayingDao;

    private DeleteAllSayingsAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      sayingDao.deleteAllSayings();
      return null;
    }
  }

  private static class InsertChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private SayingDao sayingDao;

    private InsertChildAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Child... childs) {
      sayingDao.insert(childs[0]);
      return null;
    }
  }

  private static class UpdateChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private SayingDao sayingDao;

    private UpdateChildAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Child... childs) {
      sayingDao.update(childs[0]);
      return null;
    }
  }

  private static class DeleteChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private SayingDao sayingDao;

    private DeleteChildAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Child... child) {
      sayingDao.delete(child[0]);
      return null;
    }
  }

  private static class DeleteAllChildrenAsyncTask extends AsyncTask<Void, Void, Void> {

    private SayingDao sayingDao;

    private DeleteAllChildrenAsyncTask(SayingDao sayingDao) {
      this.sayingDao = sayingDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      sayingDao.deleteAllChildren();
      return null;
    }

  }

}
