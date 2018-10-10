apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'org.jetbrains.dokka-android'

android {
    compileSdkVersion rootProject.ext.lib.compileSdkVersion

    defaultConfig {
        targetSdkVersion rootProject.ext.lib.targetSdkVersion
        minSdkVersion rootProject.ext.lib.minSdkVersion
        versionCode rootProject.ext.lib.versionCode
        versionName rootProject.ext.lib.libraryVersion
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}

dokka {
    outputFormat = 'javadoc'
    outputDirectory = "$buildDir/javadoc"
}