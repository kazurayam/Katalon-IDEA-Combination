package io.github.kazurayam.ks.configuration

import static org.junit.jupiter.api.Assertions.*

import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.jupiter.api.Test

import java.nio.file.Path

class RunConfigurationConfiguratorTest {

    @Test
    void test_configure() {
        println "before: ${RunConfiguration.getProjectDir()}"   // will be null
        RunConfigurationConfigurator.configureProjectDir()
        println "after: ${RunConfiguration.getProjectDir()}"    // will be $rootProjectDir/katalon
        //
        String projectDir = RunConfiguration.getProjectDir()
        assertNotNull(projectDir)
        assertTrue("null" != projectDir)
        assertNotNull projectDir, "RunConfiguration.getProjectDir() returned null"
        Path katalonProjectDir = KatalonProjectDirectoryResolver.getProjectDir()
        assertEquals katalonProjectDir.toString(), projectDir
    }
}