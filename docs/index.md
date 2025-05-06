- Table of contents
{:toc}

# Katalon-IDEA-Combination details

-   [repository](https://www.github.com/kazurayam/Katalon-IDEA-Combination/)

## What I want to achieve

In the `katalon` project, I have written a test Case named `misc/listTestObjects`. Its source code is as follows:

    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.Paths

    import com.kms.katalon.core.configuration.RunConfiguration

    import io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor
    import io.github.kazurayam.ks.testobject.TestObjectId

    // find the path of "Object Repository" folder
    Path projectDir = Paths.get(RunConfiguration.getProjectDir())
    Path objectRepositoryDir = projectDir.resolve("Object Repository")
    assert Files.exists(objectRepositoryDir)

    // get an instance of ObjectRepositoryAccessor
    ObjectRepositoryAccessor accessor = 
        new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()

    // get the list of TestObjectId contained in the "Object Repository"
    List<TestObjectId> list = accessor.getTestObjectIdList()

    // print the absolute path of the TestObjects in the "Object Repository"
    list.each { TestObjectId toi ->
        Path relativePath = toi.getRelativePath()
        Path absolutePath = objectRepositoryDir.resolve(relativePath)
        println absolutePath
    }

Please note that this Test Case uses the class `io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor` and `io.github.kazurayam.ks.testobject.TestObjectId`. I will develop these classes in the `lib` subproject.

When I run this Test Case, it should print the absolute path of `*.rs` files as Test Object contained in the `Object Repository` folder in the `katlaon` subproject, like this:

    2025-05-06 22:09:54.068 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2025-05-06 22:09:54.073 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/misc/listTestObjects
    2025-05-06 22:09:54.589 DEBUG testcase.listTestObjects                 - 1: projectDir = Paths.get(getProjectDir())
    2025-05-06 22:09:54.605 DEBUG testcase.listTestObjects                 - 2: objectRepositoryDir = projectDir.resolve("Object Repository")
    2025-05-06 22:09:54.631 DEBUG testcase.listTestObjects                 - 3: assert Files.exists(objectRepositoryDir)
    2025-05-06 22:09:54.662 DEBUG testcase.listTestObjects                 - 4: accessor = ObjectRepositoryAccessor$Builder(objectRepositoryDir).build()
    2025-05-06 22:09:54.745 DEBUG testcase.listTestObjects                 - 5: list = accessor.getTestObjectIdList()
    2025-05-06 22:09:54.780 DEBUG testcase.listTestObjects                 - 6: list.each({ io.github.kazurayam.ks.testobject.TestObjectId toi -> ... })
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
    2025-05-06 22:09:54.843 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/misc/listTestObjects

## How to resolve external dependencies

## How to configure ObjectRepository

## How to transfer artifact jar

## images

-   [images/0\_rootProject\_opened\_in\_IDEA.png](images/0_rootProject_opened_in_IDEA.png)

-   [images/1\_katalon\_subproject\_opened\_in\_GUI.png](images/1_katalon_subproject_opened_in_GUI.png)

## References

-   <https://github.com/kazurayam/KS_ObjectRepositoryGarbageCollector/>

-   <https://forum.katalon.com/t/i-cant-run-unit-tests-with-junit-in-katalon-studio/120315/9>
