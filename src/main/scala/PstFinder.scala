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
		JOptionPane.showMessageDialog(null, "Time to backup your PST files. Close Outlook before proceeding.")
		val programDirectory = Paths.get(userProfile + fs + "PST_FINDER") // pst finder program folder
		Try(Files.createDirectory(programDirectory)) // creates pst finder program folder
		val dataFilePath = programDirectory.resolve("data.ser") // pst finder data file; holds the map used to store file name and size
		
		Backup.createBackupLocation(Paths.get(Backup.D_DRIVE), Paths.get(Backup.cDriveExcludedDirectory)) // creates backup dir on C or D
		val backLocation = Backup.returnBackupLocation(Paths.get(Backup.cDriveExcludedDirectory), Paths.get(Backup.dDriveExcludedDirectory))
		
		
		
		if(Files.notExists(dataFilePath)) { // determines if dataFilePath exists.  if not, it creates it
			Try(Files.createFile(dataFilePath))
			scan(localDrives, backLocation, Map[String, Long](), dataFilePath)
			JOptionPane.showMessageDialog(null, "PST files have been zipped.")
		
		} else {
			val o = Try(Data.objectFromFile(dataFilePath))
			val programMap = Data.objectToStringLongMap(o.getOrElse(Map[String, Long]())) // map could be empty here. need to account for this
			val pathMap = programMap.map(t => (Paths.get(t._1), t._2)) // convert each string in map to a Path
			pathMap.map{ t =>
				if(Files.exists(t._1) && Files.size(t._1) != t._2) { //check file still exists and its size differs from size in map
					if(Data.isNotLocked(t._1.toFile)) { // if is, zip the file.  if not, leave as is.
						MyZipper.zipFile(t._1.toString, backLocation.toString + fs + 
							Data.stripFileExtension(Some(t._1.getFileName.toString)).getOrElse(t._1.toString) + zipEnding)
							val o2 = Try(Data.objectFromFile(dataFilePath))
							val programMap2 = Data.objectToStringLongMap(o2.getOrElse(Map[String, Long]()))
							updateMapFile(t._1, programMap2, dataFilePath) // debug just added
					}
				}
			}
			scan(localDrives, backLocation, programMap, dataFilePath)
			JOptionPane.showMessageDialog(null, "PST files have been zipped.")
		}
	}
	
	def scan(li: List[Path], backupLocation: Path, fileMap: Map[String,Long], serialFile: Path): Unit = { // scan local drives for PST files
		for(drive <- li) {
			if(drive.toString == "C:\\") {
				val pstLocatedUserProfileOnC = locateUnlockedFiles(Paths.get(userProfile), ".pst") //only obtained unlocked files
				val filesInMap = pstLocatedUserProfileOnC.map { f =>
					println("First check: " + f) // debug
					if(fileMap.contains(f.toString)) { //checks if file is already in map
						println("map contains file /" + f) // debug
						if(Files.size(f) != fileMap(f.toString)) { //if it is, then check if file size is different
							MyZipper.zipFile(f.toString, backupLocation.toString + fs +
								Data.stripFileExtension(Some(f.getFileName.toString)).getOrElse(f.toString) + zipEnding) //dupe code
								val o3 =  Try(Data.objectFromFile(serialFile))
								val programMap3 = Data.objectToStringLongMap(o3.getOrElse(Map[String, Long]()))
								println("Debug code: " + f) // debug code
								updateMapFile(f, programMap3, serialFile)
						}
					} else {
						println("file not in map / " + f) // debug
						MyZipper.zipFile(f.toString, backupLocation.toString + fs +
							Data.stripFileExtension(Some(f.getFileName.toString)).getOrElse(f.toString) + zipEnding) //dupe code
							val o4 = Try(Data.objectFromFile(serialFile))
							val programMap4 = Data.objectToStringLongMap(o4.getOrElse(Map[String, Long]()))
						updateMapFile(f, programMap4, serialFile)
					}
				}
			} else {
				val unlockedFilesOnOtherDrives = locateUnlockedFiles(drive, ".pst")
				val filesInMap2 = unlockedFilesOnOtherDrives.map { f =>
					if(fileMap.contains(f.toString)) { //checks if file is already in map
						if(Files.size(f) != fileMap(f.toString)) { //if it is, then check if file size is different
							MyZipper.zipFile(f.toString, backupLocation.toString + fs +
								Data.stripFileExtension(Some(f.getFileName.toString)).getOrElse(f.toString) + zipEnding) //dupe code
								val o5 = Try(Data.objectFromFile(serialFile))
								val programMap5 = Data.objectToStringLongMap(o5.getOrElse(Map[String, Long]()))
								updateMapFile(f, programMap5, serialFile)
						}
					}
				}
			}
		}
	}
	
	def updateMapFile(path: Path, existingMap: Map[String, Long], serializedFile: Path): Unit = {
		if(existingMap.contains(path.toString)) {
			println("according to updateMapFile function, file is in map") //debug
			val updatedMap = existingMap.filterNot(t => t._1 == path.toString) ++ Map(path.toString -> Files.size(path))
			Data.mapToFile(updatedMap, serializedFile)
		} else {
			val itemNotInMapMap = existingMap ++ Map(path.toString -> Files.size(path))
			println("updated map in updateMapFile function " + itemNotInMapMap)
			Data.mapToFile(itemNotInMapMap, serializedFile)
		}
	}
	
}