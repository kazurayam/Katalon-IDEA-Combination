# Developing custom classes for Katalon project using JUnit5 + Gradle + IntelliJ IDEA

## Forward

 This article presents my know-how of extending your capability to utilize Katalon Studio. I assume that you (the readers) have seasoned skill of Groovy programming with [JUnit5](https://junit.org/junit5/) and [Gradle](https://gradle.org/) build tool in [IntelliJ IDEA](https://www.jetbrains.com/idea/). If you are a non-programmer and/or new to Katalon Studio, this article would not be useful for you.

## Problem to solve

One day, I worked on a [Katalon Studio](https://katalon.com/katalon-studio) project. I wanted to list up out unused Test Objects in the "Object Repository" folder in my katalon project. I knew Katalon Studio Enterprise equips the feature of [Test Object Refactoring](https://docs.katalon.com/katalon-studio/maintain-tests/refactor-test-objects-in-katalon-studio), but I don't have an Enterprise license. I only have a Free license. Therefore I started developing a set of Groovy classes that help me identifying unused TestObject. I created a project [**KS_ObjectRepositoryGarbageCollector**](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector). 

See the project of its version 0.2.6, which is already outdated, as follows:

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

The library deserved a set of unit-tests for better quality. But, Katalon Studio does not support performing unit-test for "Custom Keywords" using JUnit. However, as a professional programmer, I desperately wanted to perform unit-testing. So, I introduced my "junit4ks" library to run the unit-tests for my custom Groovy classes inside Katalon Studio. See

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

Katalon Studio GUI has problems for developing and unit-testing custom Groovy classes in the `Keywords` folder. Here, I am not going to go into detail what's problem. I just wanted to use [IntelliJ IDEA](https://www.jetbrains.com/idea/) for this job. 

**But, how can I combine Katalon Studio and IntelliJ IDEA?**

## Solution

I introduced [Gradle Multi-project](https://docs.gradle.org/current/userguide/intro_multi_project_builds.html) into the [KS_ObjectRepositoryGarbageCollection](https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/tree/master) project since v0.3.0. The latest version has the following folder tree:

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

IntelliJ IDEA supports a fully functional integration with Gradle project. It' comfortable. See [the doc](https://www.jetbrains.com/help/idea/gradle.html).

## Technical difficulties to overcome

I introduced Gradle Multi-project structure with 2 subprojects `lib` and `katalon`. Soon I realized that there are several technical difficulties that I needed to overcome.

1. In order to compile and run Groovy codes in the `lib` subproject, Groovy compiler and test runners require many external dependencies (jar files) which are included in the Katalon Studio installation folder. How can it be done?
2. In order to run tests in the `lib` subproject, I needed to be able to read the files in the `Object Repository` folder in the `katalon` subproject. It may sound easy, but it is actually difficult to do.
3. Once I finished developing Groovy classes in the `lib` subproject, I wanted to import them into the sibling `katalon` project. How can I do it?

I have overcome all these technical difficulties. I wrote seperated document. See the [docs](https://kazurayam.github.io/Katalon-IDEA-Combination/) for detail.

## Conclusion

I have developed a good combination of Katalon Studio + IntelliJ IDEA + Gradle. With this combination, I can enjoy stress-free Groovy programming.

## Environment I used

- macOS SONOMA 14.7.5
- IntelliJ IDEA 2024.2 (Ultimate Edition)
- Katalon Studio 10.2.0 Free, JDK 17 bundled
- Groovy 3.0.24
- Gradle 8.14
- git version 2.45.2
