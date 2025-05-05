package com.kms.katalon.core.webui.keyword

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.junit.jupiter.api.Test

class WebUiBuiltinKeywordsTest {

    @Test
    void test_comment() {
        WebUI.comment("Hello, World!")
    }
}
