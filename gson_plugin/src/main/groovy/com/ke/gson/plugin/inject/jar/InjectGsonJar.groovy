package com.ke.gson.plugin.inject.jar

import com.android.build.api.transform.Context
import com.android.build.api.transform.JarInput
import com.ke.gson.plugin.inject.jar.adapter.InjectArrayTypeAdapter
import com.ke.gson.plugin.inject.jar.adapter.InjectCollectionTypeAdapterFactory
import com.ke.gson.plugin.inject.jar.adapter.InjectGsonTypeAdapter
import com.ke.gson.plugin.inject.jar.adapter.InjectTypeAdapters
import com.ke.gson.plugin.inject.jar.adapter.InjectMapTypeAdapterFactory
import com.ke.gson.plugin.inject.jar.adapter.InjectReflectiveTypeAdapterFactory
import com.ke.gson.plugin.utils.Compressor
import com.ke.gson.plugin.utils.Decompression
import com.ke.gson.plugin.utils.StrongFileUtil
import javassist.NotFoundException
import org.gradle.api.Project

/**
 * Created by tangfuling on 2018/10/25.
 */

class InjectGsonJar {

  public static File inject(JarInput jarInput, Context context, Project project) throws NotFoundException {
    def jarInputName = jarInput.name
    File jarFile = jarInput.file
    if (!jarFile.name.startsWith("gson") && !jarInputName.startsWith("com.google.code.gson:gson")) {
      return null
    }
    println("GsonPlugin: inject gson jar start")
    //原始jar path
    String srcPath = jarFile.getAbsolutePath()

    //原始jar解压后的tmpDir
    String tmpDirName = jarFile.name.substring(0, jarFile.name.length() - 4)
    String tmpDirPath = context.temporaryDir.getAbsolutePath() + File.separator + tmpDirName

    //目标jar path
    String targetPath = context.temporaryDir.getAbsolutePath() + File.separator + jarFile.name

    //解压
    Decompression.uncompress(srcPath, tmpDirPath)

    //修改
    InjectReflectiveTypeAdapterFactory.inject(tmpDirPath)
    InjectMapTypeAdapterFactory.inject(tmpDirPath)
    InjectArrayTypeAdapter.inject(tmpDirPath)
    InjectCollectionTypeAdapterFactory.inject(tmpDirPath)
    InjectTypeAdapters.inject(tmpDirPath)
    InjectGsonTypeAdapter.inject(tmpDirPath)

    //重新压缩
    Compressor.compress(tmpDirPath, targetPath)

    //删除临时目录
    StrongFileUtil.deleteDirPath(tmpDirPath)

    println("GsonPlugin: inject gson jar success")

    //返回目标jar
    File targetFile = new File(targetPath)
    if (targetFile.exists()) {
      return targetFile
    }
    return null
  }
}
