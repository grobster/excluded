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
	val fsv = FileSystemView.getFileSystemView // used to get file info such as description
	val dirs = File.listRoots.toList // get list of drives on computer as List
	val localDrives = dirs.filter(dr => fsv.getSystemTypeDescription(dr) == "Local Disk").map(d => d.toPath) // only get local drives
	
	def main(args: Array[String]): Unit = {
		val programDirectory = Paths.get(userProfile + fs + "PST_FINDER") // pst finder program folder
		Try(Files.createDirectory(programDirectory)) // creates pst finder program folder
		val dataFilePath = programDirectory.resolve("data.ser") // pst finder data file; holds the map used to store file name and size
		
		Backup.createBackupLocation(Paths.get(Backup.D_DRIVE), Paths.get(Backup.cDriveExcludedDirectory)) // creates backup dir on C or D
		val backLocation = Backup.returnBackupLocation(Paths.get(Backup.cDriveExcludedDirectory), Paths.get(Backup.dDriveExcludedDirectory))
		
		
		
		if(Files.notExists(dataFilePath)) { // determines if dataFilePath exists.  if not, it creates it
			Try(Files.createFile(dataFilePath))
		
		} else {
			val o = Data.objectFromFile(dataFilePath)
			val programMap = Data.objectToStringLongMap(o) // map could be empty here. need to account for this
			val pathMap = programMap.map(t => (Paths.get(t._1), t._2)) // convert each string in map to a Path
			pathMap.map{ t =>
				if(Files.exists(t._1) && Files.size(t._1) != t._2) { //check file still exists and its size differs from size in map
					if(Data.isNotLocked(t._1.toFile)) { // if is, zip the file.  if not, leave as is.
						MyZipper.zipFile(t._1.toString, backLocation.toString + fs + 
							Data.stripFileExtension(Some(t._1.getFileName.toString)).getOrElse(t._1.toString) + zipEnding)
					}
				}
			}
			
		}
	}
	
	def scan[A](li: List[A], backupLocation: Path, fileMap: Map[String,Long]): Unit = { // scan local drives for PST files
		for(drive <- localDrives) {
			if(drive.toString == "C:\\") {
				val pstLocatedUserProfileOnC = locateUnlockedFiles(Paths.get(userProfile), ".pst") //only obtained unlocked files
				val filesInMap = pstLocatedUserProfileOnC.map { f =>
					if(fileMap.contains(f.toString)) { //checks if file is already in map
						if(Files.size(f) != fileMap(f.toString)) { //if it is, then check if file size is different
							MyZipper.zipFile(f.toString, backupLocation.toString + fs +
								Data.stripFileExtension(Some(f.getFileName.toString)).getOrElse(f.toString) + zipEnding) //dupe code
								updateMapFile(f, fileMap, backupLocation)
						}
					}
				}
				Backup.safeZip(pstLocatedUserProfileOnC, backupLocation)
			} else {
			
			}
		}
	}
	
	def updateMapFile(path: Path, existingMap: Map[String, Long], serializedFile: Path): Unit = {
		if(existingMap.contains(path.toString)) {
			val updatedMap = existingMap.filterNot(t => t._1 == path.toString) ++ Map(path.toString -> Files.size(path))
			Data.mapToFile(updatedMap, serializedFile)
		} else {
			val itemNotInMapMap = existingMap ++ Map(path.toString -> Files.size(path))
			Data.mapToFile(itemNotInMapMap, serializedFile)
		}
	}
	
}