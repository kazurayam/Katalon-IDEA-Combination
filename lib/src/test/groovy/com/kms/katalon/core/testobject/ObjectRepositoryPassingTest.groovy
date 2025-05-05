package com.kms.katalon.core.testobject

import org.junit.jupiter.api.BeforeAll

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import io.github.kazurayam.ks.configuration.RunConfigurationConfigurator

class ObjectRepositoryPassingTest {

    @BeforeAll
    static void beforeAll() {
        RunConfigurationConfigurator.configureProjectDir()
    }

    @Test
    void test_findObject() {
        TestObject tObj = ObjectRepository.findTestObject("Page_CuraHomepage/btn_MakeAppointment")
        assertNotNull(tObj, "ObjectRepository.findTestObject() returned null")
    }
}
