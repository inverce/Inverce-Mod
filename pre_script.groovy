apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'org.jetbrains.dokka-android'

android {
    compileSdkVersion 27
    buildToolsVersion "27.0.3"

    defaultConfig {
        minSdkVersion 15
        targetSdkVersion 27
        versionCode 1
        versionName rootProject.ext.bintray.libraryVersion
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