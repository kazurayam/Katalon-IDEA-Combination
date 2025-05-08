- Table of contents
{:toc}

# Katalon - IntelliJ IDEA Combination Knowhow

-   link to the [repository](https://www.github.com/kazurayam/Katalon-IDEA-Combination/)

-   link to the [this doc](https://kazurayam.github.io/Katalon-IDEA-Combination/)

## What I want to do

What I eventually want to achieve? Let me set the objective. In the [`katalon`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/katalon/) subproject, I wrote a test Case named `misc/listTestObjects`. Its source code is as follows:

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kms.katalon.core.configuration.RunConfiguration

    import io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor
    import io.github.kazurayam.ks.testobject.TestObjectId

    // find the path of "katalon" project folder 
    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    println "projectDir=${projectDir}"

    // find the path of "Object Repository" folder
    Path objectRepositoryDir = projectDir.resolve("Object Repository")
    assert Files.exists(objectRepositoryDir)

    // get an instance of ObjectRepositoryAccessor
    ObjectRepositoryAccessor accessor = 
        new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()

    // get the list of TestObjectId contained in the "Object Repository"
    List<TestObjectId> list = accessor.getTestObjectIdList()

    // print the absolute path of the rs files in the "Object Repository"
    list.each { TestObjectId toi ->
        Path relativePath = toi.getRelativePath()
        Path absolutePath = objectRepositoryDir.resolve(relativePath)
        println absolutePath
    }

Please note that this Test Case uses the class `io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor` and `io.github.kazurayam.ks.testobject.TestObjectId`. I will develop these classes in the [`lib`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/lib/) subproject.

When I run this Test Case, it should print the absolute path Test Objects, which are `*.rs` files contained in the [`Object Repository`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/katalon/Object%20Repository/) folder in the `katalon` subproject. The following messages is a sample output that I am contented with:

    2025-05-08 07:00:39.842 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2025-05-08 07:00:39.845 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/misc/listTestObjects
    2025-05-08 07:00:40.484 DEBUG testcase.listTestObjects                 - 1: projectDir = Paths.get(getProjectDir())
    2025-05-08 07:00:40.502 DEBUG testcase.listTestObjects                 - 2: println(projectDir=$projectDir)
    projectDir=/Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon
    2025-05-08 07:00:40.574 DEBUG testcase.listTestObjects                 - 3: objectRepositoryDir = projectDir.resolve("Object Repository")
    2025-05-08 07:00:40.612 DEBUG testcase.listTestObjects                 - 4: assert Files.exists(objectRepositoryDir)
    2025-05-08 07:00:40.658 DEBUG testcase.listTestObjects                 - 5: accessor = ObjectRepositoryAccessor$Builder(objectRepositoryDir).build()
    2025-05-08 07:00:40.786 DEBUG testcase.listTestObjects                 - 6: list = accessor.getTestObjectIdList()
    2025-05-08 07:00:40.831 DEBUG testcase.listTestObjects                 - 7: list.each({ io.github.kazurayam.ks.testobject.TestObjectId toi -> ... })
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_AppointmentConfirmation/lbl_Comment.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_AppointmentConfirmation/lbl_Facility.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_AppointmentConfirmation/lbl_HospitalReadmission.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_AppointmentConfirmation/lbl_Program.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_AppointmentConfirmation/lbl_VisitDate.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/btn_BookAppointment.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/chk_Medicaid.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/chk_Medicare.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/chk_None.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/chk_Readmission.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/div_Appointment.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/lst_Facility.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/txt_Comment.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraAppointment/txt_VisitDate.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_CuraHomepage/btn_MakeAppointment.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_Login/btn_Login.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_Login/txt_Password.rs
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon/Object Repository/Page_Login/txt_UserName.rs
    2025-05-08 07:00:40.899 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/misc/listTestObjects

When I started trying to write this Test Case, I encountered a series of technical issues. I struggled for a few days. Eventually I could find resolutions. Let me present them to you one by one in the following sections.

## Problem1: How to resolve external dependencies

### Problem1-1 The jar of Groovy is required

In the `lib` subproject, I wrote a Groovy class

-   <https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/src/main/io/github/kazurayam/testobject/ObjectRepositoryAccessor.groovy>

I tried to compile it in the commandline, and got an error.

    $ ./gradlew :lib:compileGroovy
    > Task :lib:compileGroovy FAILED

    FAILURE: Build failed with an exception.

    * What went wrong:
    Execution failed for task ':lib:compileGroovy'.
    > Cannot infer Groovy class path because no Groovy Jar was found on class path: [/Users/kazurayam/tmp/Katalon-IDEA-Combination/lib/build/classes/java/main]

The `lib:compileGroovy` task required the Groovy jar of some version, but I did not configured it. That’s the cause of the error.

Where is the Groovy’s jar file?

Every Katalon project has a file named `.classpath`. Katalon Studio list the jar files it bundles and are required to run a Test Case. See the `katlaon/.classpath`

-   [katalon/.classpath](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/katalon/.classpath)

In the `.classpath` file, I found the following line:

     <classpathentry kind="lib" path="/Applications/Katalon Studio.app/Contents/Eclipse/configuration/resources/lib/groovy-3.0.17.jar"/>

This is the Groovy jar file bundled in the Katalon Studio distribution. So, I should use it for the `lib` subproject as well.

I edited the `lib/build.gradle` file to use the Groovy jar which is located in the Katalon Studio installation directory.

    plugins {
        id 'groovy'
    }
    ...
    static String resolveKatalonStudioInstallationDirectory() {
        OperatingSystem os = OperatingSystem.current()
        if (os.isMacOsX()) {
            return '/Applications/Katalon Studio.app/Contents/Eclipse'
        } else if (os.isWindows()) {
            String userHome = System.getProperty("user.home")
            return "${userHome}/Katalon_Studio_Windows_64-10.2.0"
        } else if (os.isLinux()) {
            throw new UnsupportedOperationException("I don't know")
        } else {
            // Unknown OS
            throw new UnsupportedOperationException("Who knows?")
        }
    }

    ext {
        KATALON_STUDIO_INSTALLATION_DIRECTORY = resolveKatalonStudioInstallationDirectory()
        GroovyVersion = '3.0.17'
    }

    dependencies {

        // will look into the Katalon Studio installation directory

        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/configuration/resources/lib",
                include: [
                        "groovy-${GroovyVersion}.jar"
                ]
        )

With this change, the error "Cannot infer Groovy class path" was resolved.

### Problem1-2 The jar of "com.kms.katalon.core" is required

`./gradlew :lib:compileGroovy` command continued to fail with error.

    $ gradle :lib:compileGroovy

    > Task :lib:compileGroovy
    startup failed:
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy: 5: unable to resolve class com.kms.katalon.core.testobject.TestObject
     @ line 5, column 1.
       import com.kms.katalon.core.testobject.TestObject
       ^

The message said that the `:lib:compileGroovy` task was unable to find the `com.kms.katalon.core.testobject.TestObject` class. I had to include the Katalon’s Core jar in the classpath.

In the `katalon/.classpath` file, I found the following line:

    <classpathentry kind="lib" path="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core_1.0.0.202504231120.jar" sourcepath="/Applications/Katalon Studio.app/Contents/Eclipse/configuration/resources/source/com.kms.katalon.core/com.kms.katalon.core-sources.jar">
            <attributes>
                <attribute name="javadoc_location" value="file:/Applications/Katalon%20Studio.app/Contents/Eclipse/configuration/resources/apidocs/com.kms.katalon.core/"/>
            </attributes>
        </classpathentry>

OK. I will include this jar in the classpath for `:lib:compileGroovy` target. I edited the `lib/build.gradle` file.

    dependencies {
        ...
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        'com.kms.katalon.core*.jar',
                        'com.kms.katalon.util*.jar'
                ])

With this change, the errors concerning `com.kms.katalon.core` module were resolved.

### Problem1-3 The jar of external jar which is bundled in the Katalon Studio distribution

The `:lib:compileGroovy` task continued to fail.

    ~/katalon-workspace/Katalon-IDEA-Combination git:[develop]
    gradle :lib:compileGroovy

    > Task :lib:compileGroovy FAILED
    startup failed:
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy: 3: unable to resolve class com.kazurayam.ant.DirectoryScanner
     @ line 3, column 1.
       import com.kazurayam.ant.DirectoryScanner
       ^

    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy: 6: unable to resolve class org.slf4j.Logger
     @ line 6, column 1.
       import org.slf4j.Logger
       ^

    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy: 7: unable to resolve class org.slf4j.LoggerFactory
     @ line 7, column 1.
       import org.slf4j.LoggerFactory
       ^

`org.slf4j.LoggerFactory` and `org.slf4j.Logger` --- these classes are member of the famous `SLF4J API` library, which is published at the Maven Central Repository:

-   <https://mvnrepository.com/artifact/org.slf4j/slf4j-api>

Is it OK if I download the SLF4J-API jar of arbitrary version from Maven Central? --- No, I shouldn’t. Katalon Studio bundles the SLF4J-API of a specific version. See the `katalon/.classpath` file:

     <classpathentry kind="lib" path="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/slf4j.api_2.0.16.jar"/>

My `:lib:compileGroovy` task should use the specific version of external jars bundled in the Katalon Studio distribution if present.

I edited the `lib/build.gradle` file:

    dependencies {
        ...
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'slf4j*.jar'
                ])
    }

With this change, the error concerning SLF4J-API disappeared.

### Problem1-4 The external jar which is missing from the Katalon’s distribution

The `:lib:compileGroovy` task failed yet.

    gradle :lib:compileGroovy

    > Task :lib:compileGroovy
    startup failed:
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/main/groovy/io/github/kazurayam/ks/testobject/ObjectRepositoryAccessor.groovy: 3: unable to resolve class com.kazurayam.ant.DirectoryScanner
     @ line 3, column 1.
       import com.kazurayam.ant.DirectoryScanner
       ^

The `com.kazurayam.ant.DirectoryScanner` is available at the Maven Central repository

-   <https://mvnrepository.com/artifact/com.kazurayam/monk-directory-scanner>

This jar is not bundled in the Katalon Studio’s distributable.

This type of external jar is easiest to include in the classpath for the `:lib:compileGroovy` task. I edited the `lib/build.gradle` file:

    repositories {
        mavenCentral()
    }

    dependencies {
        ...
        implementation libs.directoryscanner

And I edited the `settings.gradle` file in the rootProject directory:

    dependencyResolutionManagement {
        versionCatalogs {
            libs {
                library('directoryscanner', 'com.kazurayam:monk-directory-scanner:0.1.1')
                ...

With this change, finally, the `:lib:compileGroovy` task succeeded.

### Problem1-5 JUnit Jupiter

I ran the following command, which failed:

    $ ./gradlew :lib:test
    Starting a Gradle Daemon (subsequent builds will be faster)

    > Task :lib:compileTestGroovy FAILED
    startup failed:
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/src/test/groovy/com/kms/katalon/core/testobject/ObjectRepositoryFailingTest.groovy: 3: unable to resolve class org.junit.jupiter.api.Test
     @ line 3, column 1.
       import org.junit.jupiter.api.Test
       ^
       ...

The `org.junit.jupiter.api.Test` class belongs to the JUnit5 jar, which is not bundled in the Katalon Studio’s distributable. I need to add the jar in the classpath for the `:lib:test` target.

I edited the `lib/build.gradle` file:

    dependencies {
        ...
        testImplementation libs.junit.jupiter.api
        testImplementation libs.junit.jupiter.engine
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        ...
    }

Also I edited the `settings.gradle` file:

    dependencyResolutionManagement {
        versionCatalogs {
            libs {
                ...
                library('junit-jupiter-api', 'org.junit.jupiter:junit-jupiter-api:5.12.2')
                library('junit-jupiter-engine', 'org.junit.jupiter:junit-jupiter-engine:5.12.2')
                ...

With this change, the errors concerning "org.junit.jupiter.api" were resolved.

### Problem1-6 groovy.lang.GroovyObject

I continued trying `gradle :lib:test`, but failed

    ~/katalon-workspace/Katalon-IDEA-Combination git:[develop]
    ./gradlew :lib:test
    > Task :lib:compileTestGroovy FAILED

    FAILURE: Build failed with an exception.

    * What went wrong:
    Execution failed for task ':lib:compileTestGroovy'.
    > Unrecoverable compilation error: startup failed:
      General error during conversion: java.lang.NoClassDefFoundError: groovy.lang.GroovyObject

      java.lang.RuntimeException: java.lang.NoClassDefFoundError: groovy.lang.GroovyObject
            at org.codehaus.groovy.control.CompilationUnit$IPrimaryClassNodeOperation.doPhaseOperation(CompilationUnit.java:977)
            at org.codehaus.groovy.control.CompilationUnit.processPhaseOperations(CompilationUnit.java:672)
            at org.codehaus.groovy.control.CompilationUnit.compile(CompilationUnit.java:636)
            at org.codehaus.groovy.control.CompilationUnit.compile(CompilationUnit.java:611)
            at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:285)
            at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:67)
            at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:90)
            at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:76)
            at org.gradle.api.internal.tasks.compile.daemon.AbstractIsolatedCompilerWorkerExecutor$CompilerWorkAction.execute(AbstractIsolatedCompilerWorkerExecutor.java:78)
            at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
            at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:54)
            at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:48)
            at org.gradle.internal.classloader.ClassLoaderUtils.executeInClassloader(ClassLoaderUtils.java:100)
            at org.gradle.workers.internal.AbstractClassLoaderWorker.executeInClassLoader(AbstractClassLoaderWorker.java:48)
            at org.gradle.workers.internal.IsolatedClassloaderWorker.run(IsolatedClassloaderWorker.java:49)
            at org.gradle.workers.internal.IsolatedClassloaderWorker.run(IsolatedClassloaderWorker.java:30)
            at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:96)
            at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:65)
            at org.gradle.process.internal.worker.request.WorkerAction$1.call(WorkerAction.java:138)
            at org.gradle.process.internal.worker.child.WorkerLogEventListener.withWorkerLoggingProtocol(WorkerLogEventListener.java:41)
            at org.gradle.process.internal.worker.request.WorkerAction.lambda$run$0(WorkerAction.java:135)
            at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:80)
            at org.gradle.process.internal.worker.request.WorkerAction.run(WorkerAction.java:127)
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
            at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
            at java.base/java.lang.reflect.Method.invoke(Method.java:568)
            at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
            at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
            at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
            at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
            at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:414)
            at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
            at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
            at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
            at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
            at java.base/java.lang.Thread.run(Thread.java:840)
      Caused by: java.lang.NoClassDefFoundError: groovy.lang.GroovyObject
            at org.codehaus.groovy.ast.decompiled.AsmReferenceResolver.resolveClass(AsmReferenceResolver.java:46)
            at org.codehaus.groovy.ast.decompiled.ClassSignatureParser.configureClass(ClassSignatureParser.java:42)
            at org.codehaus.groovy.ast.decompiled.DecompiledClassNode.lazyInitSupers(DecompiledClassNode.java:189)
            at org.codehaus.groovy.ast.decompiled.DecompiledClassNode.getGenericsTypes(DecompiledClassNode.java:80)
            at org.codehaus.groovy.control.GenericsVisitor.checkGenericsUsage(GenericsVisitor.java:157)
            at org.codehaus.groovy.control.GenericsVisitor.checkGenericsUsage(GenericsVisitor.java:151)
            at org.codehaus.groovy.control.GenericsVisitor.visitDeclarationExpression(GenericsVisitor.java:113)
            at org.codehaus.groovy.ast.expr.DeclarationExpression.visit(DeclarationExpression.java:89)
            at org.codehaus.groovy.ast.CodeVisitorSupport.visitExpressionStatement(CodeVisitorSupport.java:117)
            at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitExpressionStatement(ClassCodeVisitorSupport.java:200)
            at org.codehaus.groovy.ast.stmt.ExpressionStatement.visit(ExpressionStatement.java:40)
            at org.codehaus.groovy.ast.CodeVisitorSupport.visitBlockStatement(CodeVisitorSupport.java:86)
            at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitBlockStatement(ClassCodeVisitorSupport.java:164)
            at org.codehaus.groovy.ast.stmt.BlockStatement.visit(BlockStatement.java:69)
            at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitClassCodeContainer(ClassCodeVisitorSupport.java:138)
            at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitConstructorOrMethod(ClassCodeVisitorSupport.java:111)
            at org.codehaus.groovy.control.GenericsVisitor.visitConstructorOrMethod(GenericsVisitor.java:93)
            at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitMethod(ClassCodeVisitorSupport.java:106)
            at org.codehaus.groovy.ast.ClassNode.visitMethods(ClassNode.java:1094)
            at org.codehaus.groovy.ast.ClassNode.visitContents(ClassNode.java:1087)
            at org.codehaus.groovy.control.GenericsVisitor.visitClass(GenericsVisitor.java:74)
            at org.codehaus.groovy.control.CompilationUnit.lambda$addPhaseOperations$5(CompilationUnit.java:221)
            at org.codehaus.groovy.control.CompilationUnit$IPrimaryClassNodeOperation.doPhaseOperation(CompilationUnit.java:943)
            ... 36 more

      1 error

I have never seen such an error "NoClassDefFound: groovy.lang.GroovyObject". I made a lot of search, eventually found a workaround.

I edited the `lib/build.gradle` file.

    dependencies {
        ...
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        // to import groovy.lang.GroovyObject
                        '**/org.codehaus.groovy*/lib/groovy*-indy.jar',

With this change, the error concerning `groovy.lang.GroovyObject` was resolved.

### Problem1-7 org/dom4j/DocumentException

The `./gradlew :lib:test` command still failed.

    ./gradlew :lib:test

    > Task :lib:test

    ObjectRepositoryFailingTest > test_findObject() FAILED
        java.lang.NoClassDefFoundError at ObjectRepositoryFailingTest.groovy:11
            Caused by: java.lang.ClassNotFoundException at ObjectRepositoryFailingTest.groovy:11

In the report, I found the detail:

    java.lang.NoClassDefFoundError: org/dom4j/DocumentException
        at java.base/java.lang.Class.forName0(Native Method)
        at java.base/java.lang.Class.forName(Class.java:467)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.lambda$createCallStaticSite$2(CallSiteArray.java:65)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:318)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallStaticSite(CallSiteArray.java:63)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallSite(CallSiteArray.java:156)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)
        at com.kms.katalon.core.testobject.ObjectRepositoryFailingTest.test_findObject(ObjectRepositoryFailingTest.groovy:11)
    ...

I changed the `lib/build.gradle` file:

    dependencies {
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'org.dom4j*.jar',

Then the error concerning "org/dom4j/DocumentException" was resolved.

### Problem1-8 com/google/gson/JsonSyntaxException

The `./gradlew :lib:test` command still failed.

    ~/katalon-workspace/Katalon-IDEA-Combination git:[develop]
    ./gradlew :lib:test

    > Task :lib:test

    ObjectRepositoryFailingTest > test_findObject() FAILED
        java.lang.NoClassDefFoundError at ObjectRepositoryFailingTest.groovy:11
            Caused by: java.lang.ClassNotFoundException at ObjectRepositoryFailingTest.groovy:11

The report showed the detail:

    java.lang.NoClassDefFoundError: com/google/gson/JsonSyntaxException
        at com.kms.katalon.core.logging.KeywordLogger.initShouldLogTestSteps(KeywordLogger.java:77)
        at com.kms.katalon.core.logging.KeywordLogger.<init>(KeywordLogger.java:73)
        at com.kms.katalon.core.logging.KeywordLogger.getInstance(KeywordLogger.java:59)
        at com.kms.katalon.core.logging.KeywordLogger.getInstance(KeywordLogger.java:51)
        at com.kms.katalon.core.testobject.ObjectRepository.<clinit>(ObjectRepository.java:54)
        at java.base/java.lang.Class.forName0(Native Method)
        at java.base/java.lang.Class.forName(Class.java:467)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.lambda$createCallStaticSite$2(CallSiteArray.java:65)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:318)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallStaticSite(CallSiteArray.java:63)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallSite(CallSiteArray.java:156)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)
        at com.kms.katalon.core.testobject.ObjectRepositoryFailingTest.test_findObject(ObjectRepositoryFailingTest.groovy:11)

I edited the `lib/build.gradle` file:

    dependencies {
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'com.google.gson*.jar',

With this change, the error concerning "gson" was resolved.

### Problem1-9 org/eclipse/osgi/util/NLS

    ~/katalon-workspace/Katalon-IDEA-Combination git:[develop]
    ./gradlew :lib:test

    > Task :lib:test

    ObjectRepositoryFailingTest > test_findObject() FAILED
        java.lang.NoClassDefFoundError at ObjectRepositoryFailingTest.groovy:11
            Caused by: java.lang.ClassNotFoundException at ObjectRepositoryFailingTest.groovy:11

    java.lang.NoClassDefFoundError: org/eclipse/osgi/util/NLS
        at java.base/java.lang.ClassLoader.defineClass1(Native Method)
        at java.base/java.lang.ClassLoader.defineClass(ClassLoader.java:1017)
        at java.base/java.security.SecureClassLoader.defineClass(SecureClassLoader.java:150)
        at java.base/jdk.internal.loader.BuiltinClassLoader.defineClass(BuiltinClassLoader.java:862)
        at java.base/jdk.internal.loader.BuiltinClassLoader.findClassOnClassPathOrNull(BuiltinClassLoader.java:760)
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClassOrNull(BuiltinClassLoader.java:681)
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:639)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:525)
        at com.kms.katalon.core.constants.StringConstants.<clinit>(StringConstants.java:56)
        at com.kms.katalon.core.configuration.RunConfiguration.<clinit>(RunConfiguration.java:117)
        at com.kms.katalon.core.logging.KeywordLogger.initShouldLogTestSteps(KeywordLogger.java:77)
        at com.kms.katalon.core.logging.KeywordLogger.<init>(KeywordLogger.java:73)
        at com.kms.katalon.core.logging.KeywordLogger.getInstance(KeywordLogger.java:59)
        at com.kms.katalon.core.logging.KeywordLogger.getInstance(KeywordLogger.java:51)
        at com.kms.katalon.core.testobject.ObjectRepository.<clinit>(ObjectRepository.java:54)
        at java.base/java.lang.Class.forName0(Native Method)
        at java.base/java.lang.Class.forName(Class.java:467)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.lambda$createCallStaticSite$2(CallSiteArray.java:65)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:318)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallStaticSite(CallSiteArray.java:63)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallSite(CallSiteArray.java:156)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)
        at com.kms.katalon.core.testobject.ObjectRepositoryFailingTest.test_findObject(ObjectRepositoryFailingTest.groovy:11)

What the hell is it "org/eclipse/osgi/util/NLS"? I have no idea. But I found the following line in the `katalon/.classpath`.

     <classpathentry kind="lib" path="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.eclipse.osgi_3.20.0.v20240509-1421.jar"/>

Probably this is the key. I edited the `lib/build.gradle`

    dependencies {
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'org.eclipse.osgi*.jar',

This change resolved the error concerning "org/eclipse/osgi/util/NLS".

### Problem1-10 org/apache/commons/lang/StringEscapeUtils

The `.gradlew :lib:test` command still failed.

    java.lang.NoClassDefFoundError: org/apache/commons/lang/StringEscapeUtils
        at com.kms.katalon.core.testobject.ObjectRepository.findWebUIObject(ObjectRepository.java:365)
        at com.kms.katalon.core.testobject.ObjectRepository.readTestObjectFile(ObjectRepository.java:273)
        at com.kms.katalon.core.testobject.ObjectRepository.findTestObject(ObjectRepository.java:231)
        at com.kms.katalon.core.testobject.ObjectRepository.findTestObject(ObjectRepository.java:181)
        at com.kms.katalon.core.testobject.ObjectRepository$findTestObject.call(Unknown Source)
        at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
        at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)
        at com.kms.katalon.core.testobject.ObjectRepositoryPassingTest.test_findObject(ObjectRepositoryPassingTest.groovy:18)

I changed the `lib/build.gradle` file:

    dependencies {
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'org.apache.commons.lang*.jar',

This change resolved the error concerning "org/apache/commons/lang/StringEscapeUtils".

### Problem1-11 org/openqa/selenium/WebElement

The `./gradlew :lib:test` task still failed:

    java.lang.NoClassDefFoundError: org/openqa/selenium/WebElement
    at java.base/java.lang.Class.getDeclaredMethods0(Native Method)
    at java.base/java.lang.Class.privateGetDeclaredMethods(Class.java:3402)
    at java.base/java.lang.Class.privateGetPublicMethods(Class.java:3427)
    at java.base/java.lang.Class.getMethods(Class.java:2019)
    at java.desktop/com.sun.beans.introspect.MethodInfo.get(MethodInfo.java:70)
    at java.desktop/com.sun.beans.introspect.ClassInfo.getMethods(ClassInfo.java:80)
    at java.desktop/java.beans.Introspector.getTargetMethodInfo(Introspector.java:1029)
    at java.desktop/java.beans.Introspector.getBeanInfo(Introspector.java:446)
    at java.desktop/java.beans.Introspector.getBeanInfo(Introspector.java:195)
    at groovy.lang.MetaClassImpl.lambda$addProperties$8(MetaClassImpl.java:3401)
    at java.base/java.security.AccessController.doPrivileged(AccessController.java:569)
    at groovy.lang.MetaClassImpl.addProperties(MetaClassImpl.java:3401)
    at groovy.lang.MetaClassImpl.reinitialize(MetaClassImpl.java:3383)
    at groovy.lang.MetaClassImpl.initialize(MetaClassImpl.java:3376)
    at org.codehaus.groovy.reflection.ClassInfo.getMetaClassUnderLock(ClassInfo.java:273)
    at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:315)
    at org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.getMetaClass(MetaClassRegistryImpl.java:258)
    at org.codehaus.groovy.runtime.InvokerHelper.getMetaClass(InvokerHelper.java:1017)
    at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallStaticSite(CallSiteArray.java:71)
    at org.codehaus.groovy.runtime.callsite.CallSiteArray.createCallSite(CallSiteArray.java:156)
    at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
    at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
    at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:139)
    at com.kms.katalon.core.webui.keyword.WebUiBuiltinKeywordsTest.test_comment(WebUiBuiltinKeywordsTest.groovy:10)

I edited the `lib/build.gradle` file:

    dependencies {
        implementation fileTree(dir: "${KATALON_STUDIO_INSTALLATION_DIRECTORY}/plugins",
                include: [
                        ...
                        'org.seleniumhq.selenium.selenium-api*.jar',

This change resolved the error concerning "WebElement".

### Problem1-12 How to print Classpath

I worked on constructing the classpath for the `lib` subproject. How does the content of classpath look like?

I made a Gradle task `printClasspath` in the `lib` subproject.

    tasks.register('printClasspath') {
        group = 'Custom'
        description = "show the entries included in the runtime classpath, sorted alphabetically"
        doLast {
            sourceSets.main.runtimeClasspath.sort().each { println it }
        }
    }

When I ran the `printClasspath` by

    $ ./gradlew :lib:printClasspath

I got the following output:

    /Applications/Katalon Studio.app/Contents/Eclipse/configuration/resources/lib/groovy-3.0.17.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.google.gson_2.11.0.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.appium_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.cucumber_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.mobile_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.testng_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.webservice_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.webui_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.windows_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.util_1.0.0.202504231120.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.apache.commons.lang3_3.14.0.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.apache.commons.lang_2.6.0.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.codehaus.groovy_3.0.22.v202406302347-e2406/lib/groovy-3.0.22-indy.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.codehaus.groovy_3.0.22.v202406302347-e2406/lib/groovy-test-3.0.22-indy.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.dom4j_2.1.4.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.eclipse.osgi.compatibility.state_1.2.1000.v20240213-1057.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.eclipse.osgi.services_3.12.100.v20240327-0645.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.eclipse.osgi.util_3.7.300.v20231104-1118.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.eclipse.osgi_3.20.0.v20240509-1421.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/org.seleniumhq.selenium.selenium-api_4.28.1.jar
    /Applications/Katalon Studio.app/Contents/Eclipse/plugins/slf4j.api_2.0.16.jar
    /Users/kazurayam/github/dotfiles/.gradle/caches/modules-2/files-2.1/com.kazurayam/monk-directory-scanner/0.1.1/51aae11fcb0e55b72e2bd5a97a6fd662af45915e/monk-directory-scanner-0.1.1.jar
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/build/classes/groovy/main
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/build/classes/java/main
    /Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/lib/build/resources/main

I could confirm that the classpath now includes `groovy-3.0.17.jar` and many others.

### Problem1-13 How to confirm versions of runtime environment

I made a custom Gradle task `printVersions` in the `lib/build.gradle`

    import org.gradle.internal.os.OperatingSystem
    import org.gradle.util.GradleVersion
    tasks.register('printVersions') {
        doLast {
            OperatingSystem os = OperatingSystem.current()
            println "${os.toString()}"
            println "Java ${JavaVersion.current()}"
            println "Groovy ${GroovySystem.getVersion()}"
            println GradleVersion.current().toString()
        }
    }

When I ran this, I got the following output:

    Mac OS X 14.7.5 x86_64
    Java 17
    Groovy 3.0.17
    Gradle 8.4

## `RunConfiguration.getProjectDir()` returns null outside katalon project

### The blocker problem

See the source of [Test Cases/misc/listTestObjects](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/katalon/Scripts/misc/listTestObjects/Script1746006212468.groovy). I called `com.kms.katalon.core.configuration.RunConfiguration.getProjectDir()` to find the path of "katalon" project.

    // find the path of "katalon" project folder 
    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    println "projectDir=${projectDir}"

When I ran this code in Katalon Studio, it printed the path of the `katalon` project folder successfully.

    2025-05-08 07:00:40.484 DEBUG testcase.listTestObjects                 - 1: projectDir = Paths.get(getProjectDir())
    2025-05-08 07:00:40.502 DEBUG testcase.listTestObjects                 - 2: println(projectDir=$projectDir)
    projectDir=/Users/kazurayam/katalon-workspace/Katalon-IDEA-Combination/katalon

Yes, `RunConfiguration.getProjectDir()` is the established way of obtaining the file path of a Katalon project’s folder. It works properly in Katalon Runtime Engine as well.

However, `RunConfiguration.getProjectDir()` does not work outside Katalon Studio. I wrote a JUnit test:

-   [`lib/src/test/groovy/com/kms/katalon/core/configuration/RunConfigurationFailingTest.groovy`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/lib/listTestObjects/Script1746006212468.groovy)

<!-- -->

    package com.kms.katalon.core.configuration

    import static org.junit.jupiter.api.Assertions.*
    import org.junit.jupiter.api.Test

    class RunConfigurationFailingTest {

        @Test
        void test_getProjectDir() {
            String projectDir = RunConfiguration.getProjectDir()
            assertTrue(projectDir.endsWith("katalon"), "projectDir=${projectDir}")
        }
    }

When I ran this JUnit test, it failed.

    $ ./gradlew :lib:test --tests com.kms.katalon.core.configuration.RunConfigurationFailingTest

    > Task :lib:test FAILED

    RunConfigurationFailingTest > test_getProjectDir() FAILED
        org.opentest4j.AssertionFailedError at RunConfigurationFailingTest.groovy:11

    org.opentest4j.AssertionFailedError: projectDir=null ==> expected: <true> but was: <false>
        at app//org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at app//org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at app//org.junit.jupiter.api.AssertTrue.failNotTrue(AssertTrue.java:63)
        at app//org.junit.jupiter.api.AssertTrue.assertTrue(AssertTrue.java:36)
        at app//org.junit.jupiter.api.Assertions.assertTrue(Assertions.java:214)
        at java.base@17.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base@17.0.11/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base@17.0.11/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base@17.0.11/java.lang.reflect.Method.invoke(Method.java:568)
        at app//org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:107)
        at app//groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:323)
        at app//org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrap.invoke(StaticMetaMethodSite.java:131)
        at app//org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:100)
        at app//org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:55)
        at app//org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:217)
        at app//org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:240)
        at app//com.kms.katalon.core.configuration.RunConfigurationFailingTest.test_getProjectDir(RunConfigurationFailingTest.groovy:11)
        ...

The **`RunConfiguration.getProjectDir()` returned null in the `lib` subproject**!

This is a serious blocker for me. If my codes in the `lib` subproject can not get the path of `katalon` project folder, my codes can not get the path of `Object Repository` folder and the Test Objects at all. I am at a loss what to do in the `lib` subproject.

### The resolution

I invented a magic spell: `io.github.kazurayam.ks.configuration.RunConfigurationConfigurator`. Later I will explain what it does. See how it works as in the following JUnit test:

-   [`lib/src/test/groovy/com/kms/katalon/core/configuration/RunConfigurationPassingTest.groovy`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/lib/listTestObjects/Script1746006212468.groovy)

<!-- -->

    package com.kms.katalon.core.configuration

    import io.github.kazurayam.ks.configuration.RunConfigurationConfigurator
    import org.junit.jupiter.api.BeforeAll
    import org.junit.jupiter.api.Test

    import static org.junit.jupiter.api.Assertions.assertTrue

    class RunConfigurationPassingTest {

        @BeforeAll
        static void beforeAll() {
            // the magic spell
            RunConfigurationConfigurator.configureProjectDir()
        }

        @Test
        void test_getProjectDir() {
            String projectDir = RunConfiguration.getProjectDir()
            assertTrue(projectDir.endsWith("katalon"), "projectDir=${projectDir}")
        }
    }

When I ran this JUnit test, it passed. This means, a call to `RunConfiguration.getProjectDir()` in a JUnit test in the `lib` subproject successfully returned the path of the `katalon` subproject. The issue has been resolved!

### Description

I developed 2 tricky Groovy classes.

#### KatalonProjectDirectoryResolver

-   [`io.github.kazurayam.ks.configuration.KatalonProjectDirectoryResolver`](https://www.github.com/kazurayam/Katalon-IDEA-Combination/tree/develop/lib/src/main/groovy/io/github/kazurayam/ks/configuration/KatalonProjectDirectoryResolver.groovy)

<!-- -->

    package io.github.kazurayam.ks.configuration

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    class KatalonProjectDirectoryResolver {

        private static final String KATALON_PROJECT_PATH_RELATIVE_TO_THE_LIB_PROJECT = "../katalon"

        private KatalonProjectDirectoryResolver() {}

        /**
         * When the System Property `com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath` is
         * given with a value, it will be converted to a Path value and will be returned.
         * Otherwise, the path to `../katalon` relative to the System Property `user.home` will be returned.
         */
        static Path getProjectDir() {
            Path thePath
            String thePathString = System.getProperty("com.kazurayam.ks.configuration.KatalonProjectDirectoryResolver.thePath")
            if (thePathString != null) {
                thePath = Paths.get(thePathString).toAbsolutePath().normalize()
            } else {
                Path currentDir = Paths.get(System.getProperty('user.dir'))
                thePath = currentDir.resolve(KATALON_PROJECT_PATH_RELATIVE_TO_THE_LIB_PROJECT)
                        .toAbsolutePath().normalize()
            }
            if (Files.exists(thePath)) {
                return thePath
            } else {
                throw new IOException(thePath.toString() + " is not present")
            }
        }
    }

The static `KatalonProjectDirectoryResolver.getProjectDir()` method will return a Path object, which is the `katalon` subproject’s root folder.

When the method is called inside Katalon Studio runtime, the method simply returns a non-null value returned by `RunConfiguration.getProjectDir()`. When the `RunConfiguration.getProjectDir()` returned null, the method presumes that it was invoked in the sibling `lib` subproject, and returns a path `../katalon` relative the the `lib` subproject.

As you can perceive, the `KatalonProjectDirectoryResolver` is designed for limited use local to the `lib` subproject of this `Katalon-IDEA-Combination` project.

#### RunConfigurationConfigurator

## How to transfer artifact jar

## images

-   [images/0\_rootProject\_opened\_in\_IDEA.png](images/0_rootProject_opened_in_IDEA.png)

-   [images/1\_katalon\_subproject\_opened\_in\_GUI.png](images/1_katalon_subproject_opened_in_GUI.png)

## References

-   <https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/>

-   <https://forum.katalon.com/t/i-cant-run-unit-tests-with-junit-in-katalon-studio/120315/9>
