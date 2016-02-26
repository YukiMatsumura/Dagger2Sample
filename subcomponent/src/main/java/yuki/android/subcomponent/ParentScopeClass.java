package yuki.android.subcomponent;

import android.util.Log;

import javax.inject.Inject;

import yuki.android.subcomponent.scope.ParentScope;

@ParentScope
public class ParentScopeClass {
  @Inject
  public ParentScopeClass() {
    Log.i("yuki", "ParentScopeClass created. hash=" + hashCode());
  }
}
