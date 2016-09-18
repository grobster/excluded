package util

import java.io._
import java.nio.file._

object Data {
	/**
	  * This function take a Map and save it to a file.
	  */
	def mapToFile[A,B](map: Map[A,B], file: Path): Unit = {
		val fos = new FileOutputStream(file.toFile)
		val oos = new ObjectOutputStream(fos)
		oos.writeObject(map)
		oos.close
	}
	
	/**
	  * This function obtains an object from a file.
	  */
	def objectFromFile(file: Path): AnyRef = {
		val fis = new FileInputStream(file.toFile)
		val ois = new ObjectInputStream(fis)
		val one = ois.readObject
		//val map = one.asInstanceOf[Map[String,Long]]
		ois.close
		//map
		one
	}
	
	/**
	  * This function casts an object as a Map of type String Long.
	  */
	def objectToStringLongMap(o: AnyRef): Map[String, Long] = o.asInstanceOf[Map[String,Long]]
	
	
	/**
	  * This function takes a string argument and removes it file extension.
	  */
	def stripFileExtension(s: Option[String]): Option[String] = s match {
		case None => s
		case Some(a) if(a.lastIndexOf(".") == -1)=> s
		case Some(g) =>
			val pos = g.lastIndexOf(".")
			Some(g.substring(0, pos))
	}
	
	/**
	  * Function determines whether a file is locked or not.
	  */
	def isNotLocked(f: File): Boolean = f.renameTo(f)
}