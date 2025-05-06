package io.github.kazurayam.ks.testobject

import com.kazurayam.ant.DirectoryScanner
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Path
import java.nio.file.Paths

/**
 * ObjectRepositoryAccessor requires the path of "Object Repository" folder in a
 * Katalon project. Optionally it accepts Ant-like patterns that represents
 * the sub-folders of "Object Repository" to be included.
 */
class ObjectRepositoryAccessor {

    private static Logger logger = LoggerFactory.getLogger(ObjectRepositoryAccessor.class)

    private Path objectRepositoryDir
    private List<String> includeFilesSpecification
    private DirectoryScanner ds

    private ObjectRepositoryAccessor(Builder builder) {
        this.objectRepositoryDir = builder.objectRepositoryDir
        this.includeFilesSpecification = builder.includeFiles
        init()
        logger.info("ObjectRepositoryAccessor was initialized with ${objectRepositoryDir}")
    }

    private void init() {
        ds = new DirectoryScanner()
        ds.setBasedir(objectRepositoryDir.toFile())
        if (includeFilesSpecification.size() > 0) {
            String[] includes = includeFilesSpecification.toArray(new String[0])
            ds.setIncludes(includes)
        }
        ds.scan()
    }

    String[] getIncludedFiles() {
        return ds.getIncludedFiles()
    }

    List<TestObjectId> getTestObjectIdList() {
        String[] includedFiles = getIncludedFiles()
        List<TestObjectId> result = new ArrayList<>()
        for (int i = 0; i < includedFiles.length; i++) {
            if (includedFiles[i].endsWith(".rs")) {
                Path relativePath = Paths.get(includedFiles[i])
                TestObjectId toi = new TestObjectId(relativePath)
                result.add(toi)
            } else {
                logger.warn("found a file that does not end with '.rs'; ${includedFiles[i]}")
            }
        }
        return result
    }

    static TestObject getTestObject(TestObjectId testObjectId) {
        TestObject tObj = ObjectRepository.findTestObject(testObjectId.getValue())
        return tObj
    }

    /**
     *
     * @author kazurayam
     */
    static class Builder {
        private Path objectRepositoryDir
        private List<String> includeFiles
        Builder(Path orDir) {
            objectRepositoryDir = orDir.toAbsolutePath().normalize()
            includeFiles = new ArrayList<>()
        }
        Builder includeFile(String pattern) {
            Objects.requireNonNull(pattern)
            includeFiles.add(pattern)
            return this
        }
        Builder includeFiles(List<String> patterns) {
            Objects.requireNonNull(patterns)
            includeFiles.addAll(patterns)
            return this
        }
        ObjectRepositoryAccessor build() {
            return new ObjectRepositoryAccessor(this)
        }
    }
}