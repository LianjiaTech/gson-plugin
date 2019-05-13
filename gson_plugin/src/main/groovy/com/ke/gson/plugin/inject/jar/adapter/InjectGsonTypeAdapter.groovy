package com.ke.gson.plugin.inject.jar.adapter

import com.ke.gson.plugin.global.MyClassPool
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

/**
 * Created by tangfuling on 2018/10/30.
 */

public class InjectGsonTypeAdapter {

  public static void inject(String dirPath) {

    ClassPool classPool = MyClassPool.getClassPool()

    File dir = new File(dirPath)
    if (dir.isDirectory()) {
      dir.eachFileRecurse { File file ->
        if (file.name.startsWith("Gson\$")) {
          String innerClassName = file.name.substring(5, file.name.length() - 6)
          CtClass ctClass = classPool.getCtClass("com.google.gson.Gson\$" + innerClassName)
          //only deal type Number Double Float
          CtMethod[] methods = ctClass.declaredMethods
          boolean isModified = false
          for (CtMethod ctMethod : methods) {
            if ("read".equals(ctMethod.name)) {
              String returnTypeName = ctMethod.getReturnType().name
              if ("java.lang.Number".equals(returnTypeName)
                  || "java.lang.Double".equals(returnTypeName)
                  || "java.lang.Float".equals(returnTypeName)) {
                CtClass etype = classPool.get("java.lang.Exception")
                ctMethod.addCatch("{com.ke.gson.sdk.ReaderTools.onJsonTokenParseException(\$1, \$e); return null;}", etype)
                isModified = true
              }
            }
          }
          if (isModified) {
            ctClass.writeFile(dirPath)
            println("GsonPlugin: inject GsonTypeAdapter success")
          }
          ctClass.detach()
        }
      }
    }
  }
}
