pluginManagement {
    repositories {
        google()
        //mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
//        mavenCentral()
        jcenter()
    }
}

rootProject.name = "FitnessAssistant"
include(":app")
include(":Model")
include(":ViewModel")
include(":Repository")
include(":Helper")
include(":Junk")
