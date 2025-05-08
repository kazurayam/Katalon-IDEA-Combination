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
