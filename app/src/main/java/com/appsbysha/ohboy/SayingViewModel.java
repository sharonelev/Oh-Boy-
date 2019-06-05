package com.appsbysha.ohboy;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.appsbysha.ohboy.entities.Saying;
import java.util.List;

public class SayingViewModel extends AndroidViewModel {
  private SayingRepository repository;
  private LiveData<List<Saying>> allSayings;
  private int childId;

  public SayingViewModel(@NonNull Application application, int childId) {
    super(application);
    this.childId = childId;
    repository = new SayingRepository(application, this.childId);
    allSayings = repository.getAllSayings();
  }
  public void insert(Saying saying) {
    repository.insert(saying);
  }

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
