import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration

import io.github.kazurayam.ks.testobject.ObjectRepositoryAccessor
import io.github.kazurayam.ks.testobject.TestObjectId

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path objectRepositoryDir = projectDir.resolve("Object Repository")
assert Files.exists(objectRepositoryDir)

ObjectRepositoryAccessor accessor = 
	new ObjectRepositoryAccessor.Builder(objectRepositoryDir)
		.includeFile("Page_AppointmentConfirmation/*")
		.includeFile("Page_CuraAppointment/*")
		.includeFile("Page_CuraHomepage/*")
		.includeFile("Page_Login/*")
		.build()
		
List<TestObjectId> list = accessor.getTestObjectIdList()

list.each { s ->
	println s
}