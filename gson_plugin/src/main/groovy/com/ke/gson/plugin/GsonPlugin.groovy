package com.ke.gson.plugin

import com.ke.gson.plugin.transform.GsonJarTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tangfuling on 2018/10/25.
 */

class GsonPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    
    // add jitpack repo
    project.repositories.maven {
      url "https://jitpack.io"
    }
    
    // add dependencies
    project.dependencies.add("api",
        "com.github.LianjiaTech:gson-plugin-sdk:1.0.0")

    // register transform
    project.android.registerTransform(new GsonJarTransform(project))
  }
}
