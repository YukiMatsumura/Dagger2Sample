package yuki.m.android.dagger2sample;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.jakewharton.threetenabp.AndroidThreeTen;

import yuki.m.android.dagger2sample.di.ApplicationComponent;
import yuki.m.android.dagger2sample.di.ApplicationModule;
import yuki.m.android.dagger2sample.di.DaggerApplicationComponent;

public class MyApp extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @VisibleForTesting
    public void setComponent(ApplicationComponent applicationComponent) {
        this.component = applicationComponent;
    }
}
