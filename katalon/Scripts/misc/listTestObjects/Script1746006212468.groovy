import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration

import io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor
import io.github.kazurayam.ks.testobject.TestObjectId

// find the path of "Object Repository" folder
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path objectRepositoryDir = projectDir.resolve("Object Repository")
assert Files.exists(objectRepositoryDir)

// get an instance of ObjectRepositoryAccessor
ObjectRepositoryAccessor accessor = 
	new ObjectRepositoryAccessor.Builder(objectRepositoryDir).build()

// get the list of TestObjectId contained in the "Object Repository"
List<TestObjectId> list = accessor.getTestObjectIdList()

// print the absolute path of the TestObjects in the "Object Repository"
list.each { TestObjectId toi ->
	Path relativePath = toi.getRelativePath()
	Path absolutePath = objectRepositoryDir.resolve(relativePath)
	println absolutePath
}