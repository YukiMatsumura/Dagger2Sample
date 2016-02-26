package yuki.android.subcomponent;

import android.util.Log;

import javax.inject.Inject;

import yuki.android.subcomponent.scope.ChildScope;

@ChildScope
public class ChildScopeClass {
  @Inject
  public ChildScopeClass() {
    Log.i("yuki", "ChildScopeClass created. hash=" + hashCode());
  }
}
