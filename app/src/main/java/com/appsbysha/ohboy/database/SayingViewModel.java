package com.appsbysha.ohboy.database;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appsbysha.ohboy.entities.Child;
import com.appsbysha.ohboy.entities.Line;
import com.appsbysha.ohboy.entities.Saying;
import java.util.List;

public class SayingViewModel extends AndroidViewModel {
  private OhBoyRepository repository;
  private LiveData<List<Saying>> allSayings;

  private int childId;

  public SayingViewModel(@NonNull Application application, int childId) {
    super(application);
    this.childId = childId;
    repository = new OhBoyRepository(application, this.childId);
    allSayings = repository.getAllSayings();

  }
  public void insert(Saying saying) {
    repository.insert(saying);
  }

  public LiveData<Child> fetchChildData(){return repository.getChildById(this.childId);}

  public void update(Saying saying) {
    repository.update(saying);
  }

  public void delete(Saying saying) {
    repository.delete(saying);
  }

  public void deleteAllSayings() {
    repository.deleteAllSayings();
  }

  public LiveData<List<Saying>> getAllSayings() {
    return allSayings;
  }


}
