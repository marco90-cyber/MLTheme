pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MLTheme"
include(":mltheme")
// l'app di prova non serve a JitPack: la saltiamo quando pubblica la libreria
if (System.getenv("JITPACK") == null) include(":sample")
