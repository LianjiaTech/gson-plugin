package com.ke.gson.plugin.inject.jar.adapter

import com.ke.gson.plugin.global.MyClassPool
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

/**
 * Created by tangfuling on 2018/10/30.
 */

public class InjectArrayTypeAdapter {

  public static void inject(String dirPath) {

    ClassPool classPool = MyClassPool.getClassPool()

    File dir = new File(dirPath)
    if (dir.isDirectory()) {
      dir.eachFileRecurse { File file ->
        if ("ArrayTypeAdapter.class".equals(file.name)) {
          CtClass ctClass = classPool.getCtClass("com.google.gson.internal.bind.ArrayTypeAdapter")
          CtMethod ctMethod = ctClass.getDeclaredMethod("read")
          ctMethod.insertBefore("     if (!com.ke.gson.sdk.ReaderTools.checkJsonToken(\$1, com.google.gson.stream.JsonToken.BEGIN_ARRAY)) {\n" +
              "        return null;\n" +
              "      }")
          ctClass.writeFile(dirPath)
          ctClass.detach()
          println("GsonPlugin: inject ArrayTypeAdapter success")
        }
      }
    }
  }
}
