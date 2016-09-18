package util

import java.io._
import java.nio.file._
import scala.annotation._
import scala.collection.parallel.immutable.ParSeq
import com.grobster.util._
import scala.io._

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
	def backupToDirectory: String = "My_PST_Backups"
	
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
	
	/**
	  * This function takes a directory and its filter file type and returns
	  * a compressed file of type specified in zipFileEnding to the location
	  * specified in zipLocation.
	  */
	def safeZip(parList: ParSeq[Path], zipLocation: Path)(implicit zipFileEnding: String): Unit = {
		parList.par.map(f => MyZipper.zipFile(f.toString, zipLocation.toString + System.getProperty("file.separator") + 
			MyFiles.stripExtension(f.getFileName.toString) + zipFileEnding))
	}
	
	/**
	  * Puts the .zip file ending in scope.
	  */
	implicit val zipEnding = ".zip"
	
	/**
	  * This function takes a java.nio.file.Path and file ending filter and
	  * returns a ParSeq that contains java.nio.file Paths that are not
	  * locked or in use by the another program or the operating system.
	  */
	def locateUnlockedFiles(dir: Path, fileEndingFilter: String): ParSeq[Path] = {
		val onlyFileType = scan(dir).par.filter(_.toString.endsWith(fileEndingFilter))
		onlyFileType.par.filter(fp => MyFiles.isNotLocked(fp.toFile))
	}
	
	/** 
	  * This function takes a ParSeq[A] and java.nio.file.Path and
	  * writes the contents of the ParSeq[A] to the path.
	  */
	def sequenceToFile[A](seq: ParSeq[A], writeToPath: Path): Unit = {
		val source = Source.fromFile(writeToPath.toFile).getLines.toList // get lines as List[String]
		val fw = new FileWriter(writeToPath.toFile, true)
		val bw = new BufferedWriter(fw)
		val pw = new PrintWriter(bw)
		Try {
			seq.map(fp => if(!source.contains(fp.toString)) pw.println(fp.toString)) // checks to see if file is already in file.
			pw.close                                                                 //if not, write it to file
		}
	}
	
	/**
	  * Creates object that holds file name and its size.
	  */
	case class FileAndSize(fileName: String, fileSize: Long)
}