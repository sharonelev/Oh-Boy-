package com.appsbysha.ohboy;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.appsbysha.ohboy.entities.Child;
import java.util.List;

public class ChildViewModel extends AndroidViewModel {
  private SayingRepository repository;
  private LiveData<List<Child>> allChildren;
  
  public ChildViewModel(@NonNull Application application) {
    super(application);
    repository = new SayingRepository(application);
    allChildren =repository.getAllChildren();
    
  }

  public void insert(Child child) {
    repository.insert(child);
  }

  public void update(Child child) {
    repository.update(child);
  }

  public void delete(Child child) {
    repository.delete(child);
  }

  public void deleteAllChildren() {
    repository.deleteAllChildren();
  }

  public LiveData<List<Child>> getAllChildren() {
    return allChildren;
  }
}

