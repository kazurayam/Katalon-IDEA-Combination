package io.github.kazurayam.ks.configuration

import org.junit.jupiter.api.Test

import java.nio.file.Path

import static org.junit.jupiter.api.Assertions.assertNotNull

class KatalonProjectDirectoryResolverTest {

    @Test
    void test_getProjectDir() {
        Path katalonProjectDir = KatalonProjectDirectoryResolver.getProjectDir()
        println "katalonProjectDir=" + katalonProjectDir.toString()
        assertNotNull(katalonProjectDir)
    }

}