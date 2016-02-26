package yuki.android.subcomponent.dependencies;

import dagger.Component;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.scope.ChildScope;

@ChildScope
@Component(dependencies = DependeeComponent.class, modules = ScreenModule.class)
public interface DependerComponent {
}
