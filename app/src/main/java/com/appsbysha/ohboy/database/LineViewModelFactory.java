package com.appsbysha.ohboy.database;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.appsbysha.ohboy.entities.Saying;

public class LineViewModelFactory implements ViewModelProvider.Factory {

  private Application mApplication;
  private int childId;
  private Saying saying;

  public LineViewModelFactory(Application application, int child, Saying saying) {
    mApplication = application;
    childId = child;
    this.saying = saying;

  }


  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    return (T) new LineViewModel(mApplication, saying);
  }
}
