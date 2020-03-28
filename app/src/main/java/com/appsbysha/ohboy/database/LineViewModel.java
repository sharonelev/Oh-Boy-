package com.appsbysha.ohboy.database;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.appsbysha.ohboy.entities.Line;
import com.appsbysha.ohboy.entities.Saying;
import com.appsbysha.ohboy.interfaces.DataReadyListener;
import java.util.List;

public class LineViewModel extends AndroidViewModel {

  private OhBoyRepository repository;
  private LiveData<List<Line>> allLines;
  private List<Line> allLinesView;
  private int childId;
  private Saying saying;

  public LineViewModel(@NonNull Application application, Saying saying) {
      super(application);
    this.saying = saying;
    this.childId = saying.getChildId();

    repository = new OhBoyRepository(application, this.childId, this.saying.getSayingId());
    allLines = repository.getAllLines();

  }
  public void insert(Line line) {
    repository.insert(line);
  }


  public void update(Line line) {
    repository.update(line);
  }

  public void delete(Line line) {
    repository.delete(line);
  }

  public void deleteAllSayings() {
    repository.deleteAllSayings();
  }

  public LiveData<List<Line>> getAllLines(){
    return allLines;}

}

