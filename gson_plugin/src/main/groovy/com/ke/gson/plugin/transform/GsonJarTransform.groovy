package com.ke.gson.plugin.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.ke.gson.plugin.global.MyClassPool
import com.ke.gson.plugin.inject.jar.InjectGsonJar
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * Created by tangfuling on 2018/10/25.
 */

class GsonJarTransform extends Transform {

  private Project mProject

  GsonJarTransform(Project project) {
    mProject = project
  }

  @Override
  String getName() {
    return "GsonJarTransform"
  }

  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS
  }

  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    return TransformManager.SCOPE_FULL_PROJECT
  }

  @Override
  boolean isIncremental() {
    return false
  }

  @Override
  void transform(TransformInvocation transformInvocation)
      throws TransformException, InterruptedException, IOException {
    //初始化ClassPool
    MyClassPool.resetClassPool(mProject, transformInvocation)

    //处理jar和file
    TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
    for (TransformInput input : transformInvocation.getInputs()) {
      for (JarInput jarInput : input.getJarInputs()) {
        // name must be unique，or throw exception "multiple dex files define"
        def jarInputName = jarInput.name
        if (jarInputName.endsWith('.jar')) {
          jarInputName = jarInputName.substring(0, jarInputName.length() - 4)
        }
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        //source file
        File file = InjectGsonJar.inject(jarInput, transformInvocation.context, mProject)
        if (file == null) {
          file = jarInput.file
        }
        //dest file
        File dest = outputProvider.getContentLocation(jarInputName + md5Name,
            jarInput.contentTypes, jarInput.scopes, Format.JAR)
        FileUtils.copyFile(file, dest)
      }

      for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
        File dest = outputProvider.getContentLocation(directoryInput.name,
          directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
      }
    }
  }
}