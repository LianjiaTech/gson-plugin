package com.ke.gsonplugin;

import android.util.Log;
import com.ke.gson.sdk.ReaderTools;

/**
 * Created by tangfuling on 2018/11/1.
 */

public class MyGsonSyntaxErrorListener {

  public static void start() {
    ReaderTools.setListener(mListener);
  }

  private static ReaderTools.JsonSyntaxErrorListener mListener = new ReaderTools.JsonSyntaxErrorListener() {
    @Override
    public void onJsonSyntaxError(String exception, String invokeStack) {
      //upload error info to server
      Log.e("test", "json syntax exception: " + exception);
      Log.e("test", "json syntax invokeStack: " + invokeStack);
    }
  };
}
