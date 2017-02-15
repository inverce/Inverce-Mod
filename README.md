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
compile 'com.inverce.mod:Core:1.0.2'
```
###### [Documentation](/Core)

## Events
Implementation of event listeners and event bus for java and android.

###### Instalation
```gradle
compile 'com.inverce.mod:Events:1.0.2'
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


## Legal 


```License
Copyright 2017 Inverce 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

You can read more on this license [here](LICENSE) or on [tldrlegal](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
