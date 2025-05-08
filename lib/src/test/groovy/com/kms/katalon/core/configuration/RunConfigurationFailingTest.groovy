package com.kms.katalon.core.configuration

import org.junit.jupiter.api.Disabled

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

// This test will always fail. It is intentional.
@Disabled
class RunConfigurationFailingTest {

    @Test
    void test_getProjectDir() {
        String projectDir = RunConfiguration.getProjectDir()
        assertTrue(projectDir.endsWith("katalon"), "projectDir=${projectDir}")
    }
}
