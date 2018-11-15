package com.ke.gson.plugin.global

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import javassist.ClassPool
import org.gradle.api.Project

/**
 * Created by tangfuling on 2018/10/31.
 */

public class MyClassPool {

  private static ClassPool sClassPool

  public static ClassPool getClassPool() {
    return sClassPool
  }

  public static void resetClassPool(Project project, TransformInvocation transformInvocation) {

    // ClassPool.getDefault() 有可能被其他使用 Javassist 的插件污染（如 nuwa），
    // 导致ClassPool中出现重复的类，Javassist抛出异常，所以不能使用默认的
    sClassPool = new ClassPool()
    sClassPool.appendSystemPath()

    // bootClasspath 包括 android.jar 和 useLibrary 指定的library 的路径（如 org.apache.http.legacy )
    project.android.bootClasspath.each {
      sClassPool.appendClassPath(it.absolutePath)
    }

    // 其它class
    for (TransformInput input : transformInvocation.getInputs()) {
      for (JarInput jarInput : input.getJarInputs()) {
        sClassPool.appendClassPath(jarInput.file.getAbsolutePath())
      }
      for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
        sClassPool.appendClassPath(directoryInput.file.getAbsolutePath())
      }
    }
  }
}
