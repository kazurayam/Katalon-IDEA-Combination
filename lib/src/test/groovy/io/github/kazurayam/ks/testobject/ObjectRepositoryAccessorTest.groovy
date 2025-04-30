package io.github.kazurayam.ks.testobject

import com.kms.katalon.core.testobject.TestObject
import groovy.json.JsonOutput
import io.github.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import io.github.kazurayam.ks.configuration.RunConfigurationConfigurator
import io.github.kazurayam.ks.reporting.Shorthand
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.nio.file.Files
import java.nio.file.Path

import static org.testng.Assert.*

class ObjectRepositoryAccessorTest {

    private static Path objectRepositoryDir

    @BeforeClass
    void beforeClass() {
        objectRepositoryDir = KatalonProjectDirectoryResolver.getProjectDir().resolve("Object Repository")
        assert Files.exists(objectRepositoryDir)
        // tune the singleton instance of com.kms.katalon.core.configuration.RunConfiguration
        // so that it refer to the "katalon/Object Repository" directory.
        RunConfigurationConfigurator.configureProjectDir()
    }

    @BeforeMethod
    void setup() {}

    @Test
    void test_getIncludedFiles() {
        ObjectRepositoryAccessor accessor =
                new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
        String[] includedFiles = accessor.getIncludedFiles()
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < includedFiles.length; i++) {
            sb.append(includedFiles[i])
            sb.append("\n")
        }
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_getIncludedFiles.txt").build()
        sh.write(sb.toString())
        assertTrue(includedFiles.length > 0)
    }

    @Test
    void test_getIncludedFiles_mutiple() {
        ObjectRepositoryAccessor accessor =
                new ObjectRepositoryAccessor.Builder(objectRepositoryDir)
                        .includeFile("**/Page_AppointmentConfirmation/**/*.rs")
                        .includeFile("**/Page_CuraAppointment/**/*.rs")
                        .includeFile("**/Page_CuraHomepage/**/*.rs")
                        .includeFile("**/Page_Login/**/*.rs")
                        .build()
        String[] includedFiles = accessor.getIncludedFiles()
        assertEquals(includedFiles.length, 18)
    }

    @Test
    void test_getTestObjectIdList() {
        ObjectRepositoryAccessor accessor =
                new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
        List<TestObjectId> list = accessor.getTestObjectIdList()
        StringBuilder sb = new StringBuilder()
        list.each { id ->
            sb.append(id.getValue())
            sb.append("\n")
        }
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("test_getTestObjectIdList.txt").build()
        sh.write(sb.toString())
        assertTrue(list.size() > 0)
    }

    @Test
    void test_getTestObject() {
        ObjectRepositoryAccessor accessor =
                new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()
        TestObjectId testObjectId = new TestObjectId("Page_CuraHomepage/btn_MakeAppointment")
        TestObject testObject = accessor.getTestObject(testObjectId)
        assertNotNull(testObject)
        assertEquals(testObject.getObjectId(),
                "Object Repository/Page_CuraHomepage/btn_MakeAppointment")
        String json = JsonOutput.toJson(testObject)
        Shorthand sh = new Shorthand.Builder().subDir(this.getClass().getName())
                .fileName("TestObject.toJson.json").build()
        sh.write(JsonOutput.prettyPrint(json.toString()))

    }
}