buildscript {
<<<<<<< HEAD
    repositories{
        google()
        mavenCentral()
    }
=======
>>>>>>> d8a8787a417949ac4681648dd7cc4a62f74f94eb
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}
