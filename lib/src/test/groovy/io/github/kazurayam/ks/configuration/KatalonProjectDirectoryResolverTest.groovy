package io.github.kazurayam.ks.configuration

import org.testng.annotations.Test
import static org.testng.Assert.*
import java.nio.file.Path

class KatalonProjectDirectoryResolverTest {

    @Test
    void test_getProjectDir() {
        Path katalonProjectDir = KatalonProjectDirectoryResolver.getProjectDir()
        println "katalonProjectDir=" + katalonProjectDir.toString()
        assertNotNull(katalonProjectDir)
    }

}