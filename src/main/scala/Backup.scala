package util

import java.io._
import java.nio.file._
import scala.annotation._

object Backup {
	/**
	  * This function takes a java.nio.file.Path and convert it to
	  * a list.  It first checks that file exists.  Then it checks
	  * that the path is a directory.  Last it checks that directory
	  * converted to an array doesn't return null.  If any of these
	  * conditions are not true, an empty list is returned.
	*/
	def arrToList(p: Path) = if(Files.exists(p) && Files.isDirectory(p) && 
		p.toFile.listFiles != null) p.toFile.listFiles.toList.map(_.toPath) else List[Path]()
		
	/**
	 * This function returns a List of type java.nio.file.Path.
	 *  It is a recursive function that will search a directory for files.
	 *  It is tail safe.
	 */
	def scan(dir: Path): List[Path] = {
		@tailrec
		def _scan(dir: List[Path], acc: List[Path]): List[Path] = dir match {
			case Nil => acc
			case h :: t if(Files.isDirectory(h)) => _scan(arrToList(h) ::: t, acc)
			case h :: t => _scan(t, h :: acc)
		}
		_scan(List(dir), Nil)
	}
	
	/**
	  * Creates a directory or returns an exception if thrown.
	  */
	def createDirectory(p: Path): Either[Exception, Path] = try Right(Files.createDirectory(p)) catch { case e: Exception => Left(e) }
	
	/**
	  * Backup folder name
	  */
	def backupToDirectory: String = "excluded_backups"
	
	/**
	  * Creates backup directory on C drive.
	  */
	def cDriveExcludedDirectory: String = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" +
		System.getProperty("file.separator") + backupToDirectory
		
	/**
	  * Creates backup directory on D drive.
	  */
	def dDriveExcludedDirectory: String = D_DRIVE + System.getProperty("file.separator") + backupToDirectory
	
	/**
	  * returns D drive letter string.
	  */
	def D_DRIVE: String = "D:\\"
	
	/**
	  * Determines backup location.
	  */
	def createBackupLocation(other: Path, defaultDir: Path) = if(Files.exists(other)) createDirectory(Paths.get(dDriveExcludedDirectory)) else
		createDirectory(defaultDir)
		
	def returnBackupLocation(a: Path, b: Path): Path = if(Files.exists(a)) a else b
	
	/** This function takes two java.nio.file.Path objects, and copies
	  * one Path from one location to the other.
	  * It returns either the new Path location or an Exception if thrown.
	  */
	def copyFile(from: Path, to: Path): Either[Exception, Path] = try Right(Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING)) 
		catch { case e: Exception => Left(e)}
		
	/**
	  * General purpose function to convert from an exception-based API to an Option-oriented API.
	  */	
	def Try[A](a: => A): Option[A] = {
		try Some(a) catch { case e: Exception => None }
	}
	
	def main(args: Array[String]): Unit = {		
		import com.grobster.util._
		
		createBackupLocation(Paths.get(D_DRIVE), Paths.get(cDriveExcludedDirectory))
		val backLocation = returnBackupLocation(Paths.get(cDriveExcludedDirectory), Paths.get(dDriveExcludedDirectory))
		println("the backup location: " + backLocation)
		

		//scan(Paths.get(System.getProperty("user.home"))).par.filter(_.toString.endsWith(".pst"))
			//.map(f => Zipper.zipDirectory(f.getParent.toString, backLocation.toString, ".pst", -1))
	}
}