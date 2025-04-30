import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject

import groovy.json.JsonOutput
import io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor
import io.github.kazurayam.ks.testobject.TestObjectId

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path objectRepositoryDir = projectDir.resolve("Object Repository")
assert Files.exists(objectRepositoryDir)

ObjectRepositoryAccessor accessor =
	new ObjectRepositoryAccessor.Builder(objectRepositoryDir)
		.includeFile("Page_CuraHomepage/*")
		.build()
		
List<TestObjectId> list = accessor.getTestObjectIdList()

list.each { testObjectId ->
	println '--------------------------------------------------------'
	TestObject testObject = ObjectRepository.findTestObject(testObjectId.getValue())
	println JsonOutput.prettyPrint(JsonOutput.toJson(testObject))
}