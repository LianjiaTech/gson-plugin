# gson-plugin
强化Android-Json解析的插件，解决Android-Json解析数据类型转换异常，不影响对Gson库的使用

# 诞生背景
Android主要开发语言是Java，属于强数据类型语言，不少公司后台开发采用的是PHP，属于弱数据类型的语言。  
客户端与服务器在进行数据传输的过程中，常常因为某个字段数据类型不一致，导致客户端gson解析失败，从而导致整个页面的数据均无法展示。

# 功能描述
1.当某个字段解析失败的时候，跳过该字段继续解析其它字段，保证其它正常数据可以展示出来。  
2.当某个字段解析失败的时候，通过观察者模式，将异常抛出，开发者在收到异常后可以进行相应的处理（如将异常日志上传到服务器，然后推动服务端RD解决）。  
3.不影响对Gson库的使用。

# 接入方法
1.工程根目录加入repositories
```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2.工程根目录build.gradle加入ClassPath  
```
dependencies {
 classpath 'com.github.LianjiaTech:gson-plugin:2.1.0'
}
```
3.工程app目录build.gradle加入依赖  
```
apply plugin: 'com.ke.gson.plugin'
```
4.可选调用（监听异常json字段，建议收到后上报给服务器）
```
ReaderTools.setListener(new ReaderTools.JsonSyntaxErrorListener() {
  @Override
 public void onJsonSyntaxError(String exception, String invokeStack) {
    //upload error info to server
 Log.e("test", "json syntax exception: " + exception);
 Log.e("test", "json syntax invokeStack: " + invokeStack);
 }
});
```
5.添加混淆keep
```
-keep class com.google.gson.** { *; }
-keep class com.ke.gson.** { *; }
```
# 注意事项
1.如果也apply了其它plugin插件，请把 apply plugin: 'com.ke.gson.plugin' 加入到其它apply之前  
原因：gson_plugin只会处理file.name包含gson的jar包，有的插件会将jar文件进行merge，统一输出为一个jar包，导致gson_plugin匹配不到，从而不会对该文件进行处理。  

2.如果引用了SNAPSHOT版本，请不要使用本地缓存  
工程根目录的build.gradle与app目录的build.gradle都得加入  
```
configurations.all {
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}
```  

3.编译失败怎么办  
taskkill /im java.exe /f  然后clean，重新build  

# 性能对比  
对如下数据进行2000次循环解析：
```
public class TestBean {
 public String name;
 public int age;
 public String sex;
 public boolean is_success;
 public String[] array;
 public List<String> list;
 public Map<String, String> map;
 public TestBean bean;
}
```
使用原生gson结果：  
第1次：1374ms，第2次：1430ms，第3次：1429ms，平均：1411ms  

使用gson-plugin结果：  
第1次：1503ms，第2次：1381ms，第3次：1418ms，平均：1434ms  

结论：  
gson-plugin比原生gson解析，效率略低（多执行了几行判断逻辑代码），但可忽略不计

# 原理说明
侵入编译流程，在编译过程中，修改gson库的字节码，修改gson解析相关的方法

# 支持gson库版本
支持gson库所有版本

# 特殊说明  
2.1.0之前的版本，对Float、Double、Map数据类型的支持不全面，建议使用2.1.0及以上的版本
