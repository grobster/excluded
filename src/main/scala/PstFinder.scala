package util

	import java.io._
	import java.nio.file._
	import javax.swing.filechooser.FileSystemView
	import com.grobster.util._
	import util.Backup._
	import javax.swing._
	import scala.io._

object PstFinder {
	val userProfile = System.getProperty("user.home") //creates path to user's profile
	val fs = System.getProperty("file.separator")
	
	def main(args: Array[String]): Unit = {
		val programDirectory = Paths.get(userProfile + fs + "PST_FINDER") // pst finder program folder
		Try(Files.createDirectory(programDirectory)) // creates pst finder program folder
		val dataFilePath = programDirectory.resolve("data.ser") // pst finder data file; holds the map used to store file name and size
		
		if(Files.notExists(dataFilePath)) { // determines if dataFilePath exists.  if not, it creates it
			Try(Files.createFile(dataFilePath))
		
		} else {
			val o = Data.objectFromFile(dataFilePath)
			val programMap = Data.objectToStringLongMap(o)
		}
	}
	
}