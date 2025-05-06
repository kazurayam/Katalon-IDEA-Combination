# Developing Custom Classes for Katalon project using JUnit5 + Gradle + IntelliJ IDEA

## Forward

 This article presents my know-how extending your capability to utilize Katalon Studio. I assume that you (the readers) have seasoned skill of Groovy programming with [JUnit5](https://junit.org/junit5/) and [Gradle](https://gradle.org/) build tool in [IntelliJ IDEA](https://www.jetbrains.com/idea/). If you are a non-programmer and/or new to Katalon Studio, this article would not be useful for you.

## Problem to solve

One day, I worked on a [Katalon Studio](https://katalon.com/katalon-studio) project. I wanted to find out unused Test Objects in the "Object Repository" folder in my katalon project. I knew Katalon Studio Enterprise equips the feature of [Test Object Refactoring](https://docs.katalon.com/katalon-studio/maintain-tests/refactor-test-objects-in-katalon-studio), but I don't have an Enterprise license. I only have a Free license. Therefore I started developing a set of Groovy classes that help me identifying unused TestObject. I initiated my project **KS_ObjectRepositoryGarbageCollector**. See the project of its version 0.2.6, which is already outdated, as follows:

- [KS_ObjectRepositoryGarbaseCollector 0.2.6](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/tree/0.2.6)

This project is a single Katalon Studio project. I created 21 `.groovy` files in the `Keywords` folder.

```
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.2.6))
$ tree Keywords
Keywords
└── com
    └── kazurayam
        └── ks
            ├── configuration
            │   └── RunConfigurationConfigurator.groovy
            ├── reporting
            │   └── Shorthand.groovy
            ├── testcase
            │   ├── DigestedLine.groovy
            │   ├── DigestedText.groovy
            │   ├── ScriptsAccessor.groovy
            │   ├── ScriptsDecorator.groovy
            │   ├── TestCaseId.groovy
            │   ├── TestCaseScriptDigester.groovy
            │   └── TextDigester.groovy
            └── testobject
                ├── Locator.groovy
                ├── LocatorIndex.groovy
                ├── ObjectRepositoryAccessor.groovy
                ├── ObjectRepositoryDecorator.groovy
                ├── RegexOptedTextMatcher.groovy
                ├── TestObjectEssence.groovy
                ├── TestObjectId.groovy
                └── gc
                    ├── BackwardReferences.groovy
                    ├── Database.groovy
                    ├── ForwardReference.groovy
                    ├── Garbages.groovy
                    └── ObjectRepositoryGarbageCollector.groovy

9 directories, 21 files
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.2.6))
$ tree Keywords | grep .groovy | wc
      21      60    1106
```

These .groovy file comprises my library that help finding unused entries in the `Object Repository` folder.

The library deserved a set of unit-tests for better quality. However, as you already know, Katalon Studio does not support performing unit-test for "Custom Keywords" using JUnit. However, as a professional programmer, I can not live without unit-testing. I introduced my "junit4ks" library to run the unit-tests for my custom Groovy classes inside Katalon Studio. See

- [junit4ks](https://forum.katalon.com/t/running-junit4-in-katalon-studio/12270)

I developed a set of `*.groovy` files using JUnit4.

```
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.2.6))
$ tree Include/scripts
Include/scripts
└── groovy
    └── com
        └── kazurayam
            └── ks
                ├── configuration
                │   └── RunConfigurationConfiguratorTest.groovy
                ├── testcase
                │   ├── DigestedLineTest.groovy
                │   ├── DigestedTextTest.groovy
                │   ├── ScriptsAccessorTest.groovy
                │   ├── ScriptsDecoratorTest.groovy
                │   ├── TestCaseIdTest.groovy
                │   ├── TestCaseScriptDigesterTest.groovy
                │   └── TextDigesterTest.groovy
                └── testobject
                    ├── LocatorIndexTest.groovy
                    ├── LocatorTest.groovy
                    ├── ObjectRepositoryAccessorTest.groovy
                    ├── ObjectRepositoryDecoratorTest.groovy
                    ├── ObjectRepositoryTest.groovy
                    ├── RegexOptedTextMatcherTest.groovy
                    ├── TestObjectEssenceTest.groovy
                    ├── TestObjectIdTest.groovy
                    └── gc
                        ├── DatabaseTest.groovy
                        ├── ForwardReferenceTest.groovy
                        ├── GarbagesTest.groovy
                        └── ObjectRepositoryGarbageCollectorTest.groovy

9 directories, 20 files
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.2.6))
$ tree Include/scripts | grep .groovy | wc
      21      58    1230
```

I ended up with over 40 .groovy classes. I could perform enough unit-tests over my library.

**Did I enjoy that? --- No, I didn't. It was damn hard.** 

Katalon Studio GUI has a lot of problems for developing & unit-testing custom Groovy classes in the `Keywords` folder. I'm not going to go into detail about what's wrong with Katalon Studio here. I definitely wanted to use [IntelliJ IDEA](https://www.jetbrains.com/idea/) for this job. 

**But, how can I combine Katalon Studio and IntelliJ IDEA?**

## Solution

I introduced [Gradle Multi-project](https://docs.gradle.org/current/userguide/intro_multi_project_builds.html) into the [KS_ObjectRepositoryGarbageCollection](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/tree/master) since v0.3.0. The latest version has the following folder tree:

```
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.4.13))
$ tree -L 1 -F .
./
├── README.md
├── docs/
├── gradle/
├── gradlew
├── gradlew.bat
├── katalon/
├── lib/
└── settings.gradle

5 directories, 4 files
```

I can open the root project using IntelliJ IDEA, as follows:

![0](https://kazurayam.github.io/Katalon-IDEA-Combination/images/0_rootProject_opened_in_IDEA.png)

The root project `KS_ObjectRepositoryGarbageCollector` consists of 2 sub-projects: `katalon` and `lib`. The settings.gradle file is as follows:
```
rootProject.name = "KS_ObjectRepositoryGarbageCollector"
include 'lib'
include 'katalon'
```

### `katalon` subproject

The `katalon` subproject contains an usual Katalon project. Its folder tree looks like this:

```
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.4.13))
$ tree -L 1 -F ./katalon
./katalon/
├── Include/
├── KS_ObjectRepositoryGarbageCollector.prj
├── Object Repository/
├── Profiles/
├── Scripts/
├── Test Cases/
├── Test Listeners/
├── Test Suites/
├── build.gradle
├── console.properties
└── settings/
```

Of course, I have no problem opening this project using Katalon Studio GUI, as follows:

![1_katalon_subproject_opened_in_GUI](https://kazurayam.github.io/Katalon-IDEA-Combination/images/1_katalon_subproject_opened_in_GUI.png)

This Katalon project was generated based on the official sample WebUI project "[healthcare](https://docs.katalon.com/katalon-studio/get-started/sample-projects/webui/sample-webui-project-healthcare-sample-in-katalon-studio)". This katalon project has nothing unusual.

### `lib` subproject

The `lib` project is an ordinary Gradle project. The folder tree looks as follows:

```
:~/tmp/KS_ObjectRepositoryGarbageCollector ((0.4.13))
$ tree lib
lib
├── build.gradle
└── src
    ├── main
    │   └── groovy
    │       ├── com
    │       │   └── kazurayam
    │       │       └── ks
    │       │           ├── configuration
    │       │           │   ├── KatalonProjectDirectoryResolver.groovy
    │       │           │   └── RunConfigurationConfigurator.groovy
    │       │           ├── logging
    │       │           │   └── SimplifiedStopWatch.groovy
    │       │           ├── reporting
    │       │           │   └── Shorthand.groovy
    │       │           ├── testcase
    │       │           │   ├── ScriptsDecorator.groovy
    │       │           │   ├── TestCaseId.groovy
    │       │           │   └── TestCaseScriptDigester.groovy
    │       │           ├── testobject
    │       │           │   ├── Locator.groovy
    │       │           │   ├── LocatorDeclarations.groovy
    │       │           │   ├── LocatorIndex.groovy
    │       │           │   ├── ObjectRepositoryDecorator.groovy
    │       │           │   ├── SelectorMethod.groovy
    │       │           │   ├── TestObjectId.groovy
    │       │           │   └── combine
    │       │           │       ├── BackwardReference.groovy
    │       │           │       ├── BackwardReferenceIndex.groovy
    │       │           │       ├── CombinedLocatorDeclarations.groovy
    │       │           │       ├── CombinedLocatorIndex.groovy
    │       │           │       ├── ForwardReference.groovy
    │       │           │       ├── ForwardReferences.groovy
    │       │           │       ├── Garbage.groovy
    │       │           │       ├── ObjectRepositoryGarbageCollector.groovy
    │       │           │       └── RunDescription.groovy
    │       │           └── text
    │       │               ├── DigestedLine.groovy
    │       │               ├── DigestedText.groovy
    │       │               ├── RegexOptedTextMatcher.groovy
    │       │               └── TextDigester.groovy
    │       └── internal
    │           └── GlobalVariable.groovy
    └── test
        └── groovy
            └── com
                ├── kazurayam
                │   └── ks
                │       ├── configuration
                │       │   ├── KatalonProjectDirectoryResolverTest.groovy
                │       │   └── RunConfigurationConfiguratorTest.groovy
                │       ├── logging
                │       │   └── SimplifiedStopWatchTest.groovy
                │       ├── testcase
                │       │   ├── ScriptsAccessorTest.groovy
                │       │   ├── ScriptsDecoratorTest.groovy
                │       │   ├── TestCaseIdTest.groovy
                │       │   └── TestCaseScriptDigesterTest.groovy
                │       ├── testobject
                │       │   ├── LocatorDeclarationsTest.groovy
                │       │   ├── LocatorIndexTest.groovy
                │       │   ├── LocatorTest.groovy
                │       │   ├── ObjectRepositoryAccessorTest.groovy
                │       │   ├── ObjectRepositoryDecoratorIncludeExcludeTest.groovy
                │       │   ├── ObjectRepositoryDecoratorTest.groovy
                │       │   ├── TestObjectIdTest.groovy
                │       │   └── combine
                │       │       ├── BackwardReferenceIndexTest.groovy
                │       │       ├── BackwardReferenceTest.groovy
                │       │       ├── CombinedLocatorDeclarationsTest.groovy
                │       │       ├── CombinedLocatorIndexTest.groovy
                │       │       ├── ForwardReferenceTest.groovy
                │       │       ├── ForwardReferencesTest.groovy
                │       │       ├── GarbageTest.groovy
                │       │       ├── ObjectRepositoryGarbageCollectorTest.groovy
                │       │       └── RunDescriptionTest.groovy
                │       └── text
                │           ├── DigestedLineTest.groovy
                │           ├── DigestedTextTest.groovy
                │           ├── RegexOptedTextMatcherTest.groovy
                │           └── TextDigesterTest.groovy
                └── kms
                    └── katalon
                        └── core
                            └── testobject
                                └── SelectorMethodTest.groovy

30 directories, 56 files
```

I moved all of `.groovy` files in the Katalon project of the v0.2.6 into the `lib` subproject; and I further developed the library. I developed my library on IntelliJ IDEA using Gradle and JUnit4.

## Technical difficulties to overcome

I introduced Gradle Multi-project structure with 2 subprojects `lib` and `katalon`. Soon I realized that there are several technical difficulties that I needed to overcome.

### `lib` subproject depends on external jars including Katalon's

A concrete code fragment will describe things best. See the following source:

- [`io.github.kazurayam.ks.testobject/ObjectRepositoryAccessor`](https://github.com/kazurayam/Katalon-IDEA-Combination/blob/develop/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy)

```
package io.github.kazurayam.ks.testobject
...
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
...
class ObjectRepositoryAccessor {
    ...
    static TestObject getTestObject(TestObjectId testObjectId) {
        TestObject tObj = ObjectRepository.findTestObject(testObjectId.getValue())
        return tObj
    }
    ...
```

This class is located in the `lib` subproject aside from the `katlaon` subproject. This class wants to import classes of the `com.kms.kata.core.testobject` package. Therefore, in order to compile this Groovy class, the `lib` subproject must resolve dependency to the Katalon's core jar. The Katalon's jar is not published in the Maven Central repository. It is bundled in the Katalon Studio's distributable. It is only found in the folder on my machine where I installed Katalon Studio project. 

Problem 1: **How can I resolve this dependency issue?**

### Unit-tests in `lib` subproject requires the `Object Repository` folder inside `katalon` subproject as test fixture

See the following source of a unit-test:

- [`com.kms.katalon.core.testobject.ObjectRepositoryFailingTest`](https://github.com/kazurayam/Katalon-IDEA-Combination/blob/develop/lib/src/test/groovy/com/kms/katalon/core/testobject/ObjectRepositoryFailingTest.groovy)

```
package com.kms.katalon.core.testobject

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertNotNull

class ObjectRepositoryFailingTest {

    @Test
    void test_findObject() {
        TestObject tObj = ObjectRepository.findTestObject("Page_CuraHomepage/btn_MakeAppointment")
        // this assertion fails
        assertNotNull(tObj, "ObjectRepository.findTestObject() returned null")
    }
}
```

The source code of this unit-test class is located inside `lib` subproject aside from `katalon` subproject. This class wants to access the `Object Repository` folder in the `katalon` to get an instance of `com.kms.katalon.core.testobject.TestObject`. 

When I run this test, it always fails. 

Why? --- The class is not informed of the concrete path of the `Object Repository` folder in the `katalon` subproject. Therefore a call `ObjectRepository.findTestObjext(String)` fails to find the folder, and returns null. 

Problem 2: **How can I tell the unit-tests in the `lib` subproject` of the `Object Repository` path inside the `katalon` subproject?**

### `katalon` subproject demands the artifact of the `lib` subproject

Once I could resolve the above difficulties, I would be able to create a jar in the `lib/build/libs` directory.

```
$ gradle jar
...
$ tree lib/build/libs
lib/build/libs
└── my-custom-artifact-0.1.1.jar
```

Katalon Studio does not recognize the jar file in the `lib` in the neighbourhood. Katalon Studio can only load jar files from the `Drivers` folder inside katalon project.

So, I need to export/import the artifact jar from `lib` to `katalon`. Of course, I know I can manually copy & paste the file in the IntelliJ IDEA, but it's not cool. 

Problem 3: **How can I export/import the jar programmatically?**

## Resolution detail

I have overcome all the aforementioned technical difficulties. See the [docs](https://kazurayam.github.io/Katalon-IDEA-Combination/) for know-hows.

## Conclusion

I have developed a good combination of Katalon Studio + IntelliJ IDEA + Gradle. With this combination, I can enjoy stress-free Groovy programming. I would prefer this to programming `Custom Keywords` without unit-tests support in Katalon Studio.

## Environment I used

- macOS SONOMA 14.7.5
- IntelliJ IDEA 2024.2 (Ultimate Edition)
- Katalon Studio 10.2.0 Free, JDK 17 bundled
- Groovy 3.0.24
- Gradle 8.14
- git version 2.45.2
