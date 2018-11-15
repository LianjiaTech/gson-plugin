package com.ke.gsonplugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MyGsonSyntaxErrorListener.start();
    
    TextView tv = findViewById(R.id.tv);
    tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TestBean innerBean = new TestBean();
        innerBean.age = 18;
        innerBean.name = "test1";
        innerBean.sex = "boy";
        innerBean.array = new String[2];
        innerBean.array[0] = "array1";
        innerBean.array[1] = "array2";
        innerBean.list = new ArrayList<>();
        innerBean.list.add("list1");
        innerBean.list.add("list2");
        innerBean.map = new HashMap<>();
        innerBean.map.put("map1", "map1");
        innerBean.map.put("map2", "map2");

        TestBean testBean = new TestBean();
        testBean.age = 20;
        testBean.name = "testBean";
        testBean.sex = "girl";
        testBean.bean = innerBean;

        String testStr = new Gson().toJson(testBean);
        Log.d("test", "str: " + testStr);
        testStr = "{\"age\":20,\"is_success\":true,\"sex\":\"girl\",\"bean\":{\"age\":18.8,\"is_success\":1,\"array\":{},\"list\":[\"list1\",\"list2\"],\"sex\":\"boy\",\"map\":\"\",\"name\":\"test1\"},\"name\":\"test2\"}";

        TestBean resultBean = new Gson().fromJson(testStr, TestBean.class);
        Log.d("test", "result bean: " + resultBean);
      }
    });
  }
}
