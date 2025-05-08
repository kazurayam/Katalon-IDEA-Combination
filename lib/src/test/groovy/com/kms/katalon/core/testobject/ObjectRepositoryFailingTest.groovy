package com.kms.katalon.core.testobject

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertNotNull

class ObjectRepositoryFailingTest {

    @Test
    void test_findObject() {
        TestObject tObj = ObjectRepository.findTestObject("Page_CuraHomepage/btn_MakeAppointment")
        // this assertion fails
        assertNotNull(tObj, "ObjectRepository.findTestObject() returned null")
    }
}
