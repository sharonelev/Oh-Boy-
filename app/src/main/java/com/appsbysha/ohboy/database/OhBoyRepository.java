package com.appsbysha.ohboy.database;


import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.ChildIdSayingId;
import com.appsbysha.ohboy.entities.Line;
import com.appsbysha.ohboy.entities.Saying;
import com.appsbysha.ohboy.interfaces.DataReadyListener;
import java.util.List;

public class OhBoyRepository {

  private OhBoyDao ohBoyDao;
  private LiveData<List<Saying>> allSayings;
  private LiveData<List<Child>> allChildren;
  private LiveData<List<Line>> allLines;
 // private List<Line> allLinesView;
  private int childId;
  private String sayingId;


  public OhBoyRepository(Application application) {  //all children list
    OhBoyDatabase database = OhBoyDatabase.getInstance(application);
    ohBoyDao = database.ohBoyDao();
    allChildren = ohBoyDao.getAllChildren();
  }

  public OhBoyRepository(Application application, int childId) { //all sayings per child
    OhBoyDatabase database = OhBoyDatabase.getInstance(application);
    ohBoyDao = database.ohBoyDao();
    this.childId = childId;
    allSayings = ohBoyDao.getAllSayings(this.childId);
  }
  public OhBoyRepository(Application application, int childId, String sayingId) { //all lines per saying per child
    OhBoyDatabase database = OhBoyDatabase.getInstance(application);
    ohBoyDao = database.ohBoyDao();
    this.childId = childId;
    this.sayingId = sayingId;
    allLines = ohBoyDao.getAllLines(this.childId, this.sayingId);
  }
  public void insert(Saying saying) {
    new InsertSayingAsyncTask(ohBoyDao).execute(saying);
  }

  public void insert(Line line){
new InsertLineAsyncTask(ohBoyDao).execute((line));

  }

  public void update(Saying saying) {
    new UpdateSayingAsyncTask(ohBoyDao).execute(saying);
  }

  public void update(Line line) {
    new UpdateLineAsyncTask(ohBoyDao).execute(line);
  }

  public void delete(Saying saying) {
    new DeleteSayingAsyncTask(ohBoyDao).execute(saying);
  }

  public void delete(Line line) {
    new DeleteLineAsyncTask(ohBoyDao).execute(line);
  }

  public void deleteAllSayings() {
    new DeleteAllSayingsAsyncTask(ohBoyDao).execute();
  }

  public void getAllLinesView(DataReadyListener dataReadyListener) {
      new GetAllLinesAsyncTask(ohBoyDao, dataReadyListener).execute(new ChildIdSayingId(childId, sayingId));
  }


  public LiveData<List<Saying>> getAllSayings() {
    return allSayings;
  }

  public LiveData<List<Line>> getAllLines() {
    return allLines;
  }
  public void insert(Child child) {
    new InsertChildAsyncTask(ohBoyDao).execute(child);
  }

  public void update(Child child) {

    new UpdateChildAsyncTask(ohBoyDao).execute(child);
  }

  public void delete(Child child) {
    new DeleteChildAsyncTask(ohBoyDao).execute(child);
  }

  public void deleteAllChildren() {
    new DeleteAllChildrenAsyncTask(ohBoyDao).execute();
  }


  public LiveData<List<Child>> getAllChildren() {
    return allChildren;
  }


  private static class InsertSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private OhBoyDao ohBoyDao;

    private InsertSayingAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      ohBoyDao.insert(sayings[0]);
      return null;
    }
  }

  private static class UpdateSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private OhBoyDao ohBoyDao;

    private UpdateSayingAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      ohBoyDao.update(sayings[0]);
      return null;
    }
  }

  private static class DeleteSayingAsyncTask extends AsyncTask<Saying, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteSayingAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Saying... sayings) {
      ohBoyDao.delete(sayings[0]);
      return null;
    }
  }

  private static class DeleteAllSayingsAsyncTask extends AsyncTask<Void, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteAllSayingsAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      ohBoyDao.deleteAllSayings();
      return null;
    }
  }

  private static class InsertLineAsyncTask extends AsyncTask<Line, Void, Void> {

    private static final String TAG = "InsertLineAsyncTask" ;
    private OhBoyDao ohBoyDao;

    private InsertLineAsyncTask(OhBoyDao ohBoyDao) {
      Log.d(TAG, "InsertLineAsyncTask: ");
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Line... lines) {
      ohBoyDao.insert(lines[0]);
      Log.d(TAG, "doInBackground: "+ lines[0].getLineId() +"  "+ lines[0].getLineSayingId());

      return null;
    }
  }

  private static class UpdateLineAsyncTask extends AsyncTask<Line, Void, Void> {

    private OhBoyDao ohBoyDao;

    private UpdateLineAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Line... lines) {
      ohBoyDao.update(lines[0]);
      return null;
    }
  }

  private static class DeleteLineAsyncTask extends AsyncTask<Line, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteLineAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Line... lines) {
      ohBoyDao.delete(lines[0]);


      return null;
    }
  }

  private static class DeleteAllLinesAsyncTask extends AsyncTask<Void, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteAllLinesAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      ohBoyDao.deleteAllLines();
      return null;
    }
  }

 private class GetAllLinesAsyncTask extends AsyncTask<ChildIdSayingId, Void, List<Line>> {

    private DataReadyListener dataReadyListener;
    private OhBoyDao ohBoyDao;

    private GetAllLinesAsyncTask(OhBoyDao ohBoyDao, DataReadyListener dataReadyListener) {
      this.dataReadyListener = dataReadyListener;
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected List<Line> doInBackground(ChildIdSayingId... ids) {
      return ohBoyDao.getAllLinesView(ids[0].getChildId(),ids[0].getSayingId());

    }

    @Override
    protected void onPostExecute(List<Line> lines) {
      super.onPostExecute(lines);
      //OhBoyRepository.this.updateAllLinesView(lines);
      dataReadyListener.onSuccess(lines);
    }
  }



  private static class InsertChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private OhBoyDao ohBoyDao;

    private InsertChildAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Child... childs) {
      ohBoyDao.insert(childs[0]);
      return null;
    }
  }

  private static class UpdateChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private OhBoyDao ohBoyDao;

    private UpdateChildAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Child... childs) {
      ohBoyDao.update(childs[0]);
      return null;
    }
  }

  private static class DeleteChildAsyncTask extends AsyncTask<Child, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteChildAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Child... child) {
      ohBoyDao.delete(child[0]);
      return null;
    }
  }

  private static class DeleteAllChildrenAsyncTask extends AsyncTask<Void, Void, Void> {

    private OhBoyDao ohBoyDao;

    private DeleteAllChildrenAsyncTask(OhBoyDao ohBoyDao) {
      this.ohBoyDao = ohBoyDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      ohBoyDao.deleteAllChildren();
      return null;
    }

  }

}
