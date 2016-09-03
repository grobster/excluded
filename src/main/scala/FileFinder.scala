package util

/**
  * The program searches for PST files on a user's system.
  * If PST file is found, it will determine whether the file is locked
  * or not by another application.  If the it is not locked, the PST file
  * will be compressed and saved to location specified by user.
  * @author Jeffery Quarles
  * @version 1.0.6
  * In version 1.0.5, added a dialogue box to show that the program has finished.
  * In version 1.0.6, I added ability to save found PST file names to a configuration file
  */
object FileFinder {
	def main(args: Array[String]): Unit = {
		import java.io._
		import java.nio.file._
		import javax.swing.filechooser.FileSystemView
		import com.grobster.util._
		import util.Backup._
		import javax.swing._
		import scala.io._
		
		JOptionPane.showMessageDialog(null, "Time to backup your PST files. Close Outlook before proceeding.");
		
		val fsv = FileSystemView.getFileSystemView // used to get file info such as description
		val dirs = File.listRoots.toList // get list of drives on computer as List
		val localDrives = dirs.filter(dr => fsv.getSystemTypeDescription(dr) == "Local Disk").map(d => d.toPath) // only get local drives

		Backup.createBackupLocation(Paths.get(Backup.D_DRIVE), Paths.get(Backup.cDriveExcludedDirectory)) // creates backup dir on C or D
		val backLocation = Backup.returnBackupLocation(Paths.get(Backup.cDriveExcludedDirectory), Paths.get(Backup.dDriveExcludedDirectory))
		println("the backup location: " + backLocation)
		
		val userProfile = System.getProperty("user.home") //creates path to user's profile
		val fs = System.getProperty("file.separator")
		
		//create program directory.  used to store config info
		val programDirectory = Paths.get(userProfile + fs + "PST_FINDER")
		Try(Files.createDirectory(programDirectory))
		
		val locatedPSTFile = programDirectory.resolve(Paths.get("located_pst_files.txt"))
		
		if(Files.notExists(locatedPSTFile)) Try(Files.createFile(locatedPSTFile)) // creates file used to store PST file locations
		println(locatedPSTFile)
		
		for(drive <- localDrives) {
			if(drive.toString == "C:\\") { // checked for every drive listed--unnecessary will re-evaluate this code
				val ulf = locateUnlockedFiles(Paths.get(userProfile), ".pst")
				Backup.safeZip(ulf, backLocation)
				sequenceToFile(ulf, locatedPSTFile)
			} else {
				val rulf = locateUnlockedFiles(drive, ".pst")
				Backup.safeZip(rulf, backLocation)
				//sequenceToFile(rulf, locatedPSTFile)
			}
		}
		JOptionPane.showMessageDialog(null, "PST files have been zipped.");
	}
}