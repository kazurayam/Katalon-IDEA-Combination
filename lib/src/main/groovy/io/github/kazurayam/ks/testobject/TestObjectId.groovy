package io.github.kazurayam.ks.testobject

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Given with a Path "Object Repository\\main\\Page_CURA Healthcare Service\\a_Make Appointment.rs",
 * this.getValue() will return a String "main/Page_CURA Healthcare Service/a_Make Appointment"
 */
class TestObjectId implements Comparable<TestObjectId>{

    private String value

    TestObjectId(Path relativePath) {
        Objects.requireNonNull(relativePath)
        if (relativePath.isAbsolute()) {
            throw new IllegalArgumentException("argument ${relativePath} is not relative")
        }
        String filePath = relativePath.normalize().toString()
                .replaceAll('\\.rs$', '')
        this.value = filePath.replace("\\", "/")
    }

    String getValue() {
        return value
    }

    Path getRelativePath() {
        return Paths.get(value + ".rs")
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof TestObjectId)) {
            return false
        }
        TestObjectId other = (TestObjectId)obj
        return this.value == other.value
    }

    @Override
    int hashCode() {
        return value.hashCode()
    }

    @Override
    String toString() {
        return value
    }

    @Override
    int compareTo(TestObjectId other) {
        return this.value <=> other.value
    }

}