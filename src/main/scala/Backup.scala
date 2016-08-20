package jfile

import java.io._
import java.nio.file._
import scala.annotation._

object Backup {
	/*
	 * This function takes a java.nio.file.Path and convert it to
	 * a list.  It first checks that file exists.  Then it checks
	 * that the path is a directory.  Last it checks that directory
	 * converted to an array doesn't return null.  If any of these
	 * conditions are not true, an empty list is returned.
	 */
	def arrToList(p: Path) = if(Files.exists(p) && Files.isDirectory(p) && 
		p.toFile.listFiles != null) p.toFile.listFiles.toList.map(_.toPath) else List[Path]()
		
	/*
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
	
	def createDirectory(p: Path): Either[Exception, Path] = try Right(Files.createDirectory(p)) catch { case e: Exception => Left(e) }
	
	def backupToDirectory: String = "excluded_backups"
	
	def cDriveExcludedDirectory: String = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" +
		System.getProperty("file.separator") + backupToDirectory
		
	def dDriveExcludedDirectory: String = D_DRIVE + System.getProperty("file.separator") + backupToDirectory
	
	def D_DRIVE: String = "D:\\"
	
	def createBackupLocation(other: Path, defaultDir: Path) = if(Files.exists(other)) createDirectory(Paths.get(dDriveExcludedDirectory)) else
		createDirectory(defaultDir)
	
	def main(args: Array[String]): Unit = {
		//val path1 = Paths.get("C:\\Users\\quarl\\Documents")
		//val files = scan(path1)
		//files.map(p => println(p))
		//val path2 = Paths.get("C:\\Users\\quarl")
		//val javaFiles = scan(path2).filter(_.toString.endsWith(".java"))
		//javaFiles.map(p => println(p))
		
		//val profileSize = scan(path2).size
		//println("number of files in my profile " + profileSize)
		
		val backupDirectory = createBackupLocation(Paths.get("D:\\"), Paths.get(cDriveExcludedDirectory))
		println(backupDirectory)
	}
	
}