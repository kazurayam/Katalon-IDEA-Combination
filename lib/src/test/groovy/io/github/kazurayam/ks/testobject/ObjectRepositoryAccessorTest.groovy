package io.github.kazurayam.ks.testobject

import com.kms.katalon.core.testobject.TestObject
import groovy.json.JsonOutput
import io.github.kazurayam.ks.configuration.KatalonProjectDirectoryResolver
import io.github.kazurayam.ks.configuration.RunConfigurationConfigurator
import io.github.kazurayam.ks.reporting.Shorthand
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.jupiter.api.Assertions.*

class ObjectRepositoryAccessorTest {

    private Path objectRepositoryDir

    @BeforeAll
    static void beforeAll() {
        // tune the singleton instance of com.kms.katalon.core.configuration.RunConfiguration
        // so that it refer to the "katalon/Object Repository" directory.
        RunConfigurationConfigurator.configureProjectDir()
    }

    @BeforeEach
    void setup() {
        objectRepositoryDir =
                KatalonProjectDirectoryResolver.getProjectDir().resolve("Object Repository")
        assert Files.exists(objectRepositoryDir)
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
        TestObjectId testObjectId = new TestObjectId(Paths.get("Page_CuraHomepage/btn_MakeAppointment.rs"))
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