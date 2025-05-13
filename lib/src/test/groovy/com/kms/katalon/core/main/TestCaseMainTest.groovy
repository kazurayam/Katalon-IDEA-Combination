package com.kms.katalon.core.main

import com.kms.katalon.core.model.FailureHandling
import io.github.kazurayam.ks.configuration.RunConfigurationConfigurator
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.testcase.TestCaseBinding

class TestCaseMainTest {

    @BeforeAll
    static void beforeAll() {
        RunConfigurationConfigurator.configureProjectDir()
        TestCaseMain.beforeStart()
    }

    @Test
    void test_run_Main_Test_Cases_TC1() {
        String testCaseId = "Main Test Cases/TC1_Verify Successful Login"
        TestCaseBinding testCaseBinding = new TestCaseBinding(testCaseId, testCaseId, [:])
        TestCaseMain.runTestCase(testCaseId, testCaseBinding, FailureHandling.STOP_ON_FAILURE)
    }
}
