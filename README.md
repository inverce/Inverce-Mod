# Inverce's Mod

Collection of simple utilities for android and java

## Prerequisites

Add following maven repository to your root build.gradle

```gradle
...
allprojects {
    ...
    repositories {
        ...
        maven { url 'https://github.com/inverce/maven/raw/master/repo' }
```

## Core

This library includes basic utility methods. 

* Logger 
* Ui and Layout utilities
* Lifecycle utilities
* Preconditions (WIP)
* Access to contexts (application / activity)
* Thread schedulers for background task and ui

###### Instalation
```gradle
compile 'com.inverce.mod:Core:1.0.0'
```
###### [Documentation](/Core)

## Events
Implementation of event listeners and event bus for java and android.

###### Instalation
```gradle
compile 'com.inverce.mod:Events:1.0.0'
```
###### [Documentation](/Events)

## Navigation [WIP]
Allows to easily implement page navigation with fragments

<!--
###### Instalation
```gradle
compile 'com.inverce.mod:Navigation:1.0.0'
```
-->

## Stateless [WIP]
Utilities for model state changes.
For ui states use Formless.
<!--
###### Instalation
```gradle
compile 'com.inverce.mod:Stateless:1.0'
```
-->
