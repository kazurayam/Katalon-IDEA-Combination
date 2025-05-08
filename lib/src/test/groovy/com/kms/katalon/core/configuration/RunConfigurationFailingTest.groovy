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
