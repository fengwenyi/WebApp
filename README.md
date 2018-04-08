# WebApp

Web View Android Application

Android应用中使用网页（含HTML5）

## WebAppLib

Web View Application Library

在使用时，请将此Module添加到你的Project下，并在build.gradle(project)中添加
```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
这样你就可以直接在WebAppLib中进行适当修改操作。

## app

这个Module是WebAppLib的简单使用，可以进行参考

## 用法

1、新建Activity，继承StartActivity，并实现方法，需要告诉我，你要加载的URL

2、完善AndroidManifest.xml，注意：主Activity是你新建的，其他事我给你提供的

这样可以了，如果不报错的话，就能够正常使用了。

## 我

    ©author Wenyi Feng

## LICENSE

   Copyright 2018 Wenyi Feng

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
