package yuki.m.android.dagger2sample.di;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ActivityComponent newActivityComponent(ActivityModule module);
}
