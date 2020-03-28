package com.appsbysha.ohboy.database;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SayingViewModelFactory implements ViewModelProvider.Factory {
  private Application mApplication;
  private int childId;


  public SayingViewModelFactory(Application application, int param) {
    mApplication = application;
    childId = param;
  }


  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    return (T) new SayingViewModel(mApplication, childId);
  }
}

