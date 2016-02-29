package yuki.m.android.dagger2sample.di;


import org.threeten.bp.LocalTime;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class ActivityModule {

  @Provides
  @ActivityScope
  @Named("executeScheduler")
  public Scheduler provideExecutionScheduler() {
    return Schedulers.newThread();
  }

  @Provides
  @ActivityScope
  @Named("postScheduler")
  public Scheduler providePostScheduler() {
    return AndroidSchedulers.mainThread();
  }

  @Provides
  @ActivityScope
  public LocalTime provideLocalTime() {
    return LocalTime.now();
  }
}