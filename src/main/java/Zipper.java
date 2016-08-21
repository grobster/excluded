package com.grobster.util;

import java.io.*;
import java.util.zip.*;
import java.time.*;
import java.nio.file.*;

public class Zipper {
	public static final String LOG_DIRECTORY_FILTER = ".log";
	public static final int BUFFER = 2048;

	public static void zipDirectory(String sourceDirectoryString, String targetDirectory, String filter, int daysOldThreshold) {
		File sourceDirectoryFile = new File(sourceDirectoryString);
		int numberOfFilesInSourceDirectory = sourceDirectoryFile.listFiles().length; //number of files in directory count
		
		if (containsFileNameFilter(sourceDirectoryFile, filter) && directoryHasFileGreaterThanThreshold(sourceDirectoryString, daysOldThreshold)) { // ensures there are files to zip
			
			FileOutputStream fos = null;
			ZipOutputStream zos = null;
			byte data[] = new byte[BUFFER];
			// here	
			try {
				fos = new FileOutputStream(targetDirectory + "\\" + FileNamer.nameFile() + ".zip");
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				zos.setMethod(ZipOutputStream.DEFLATED);
				BufferedInputStream origin = null;
				
				if (sourceDirectoryFile.isDirectory()) { // check that file is a directory
					File[] filesInDirectory = sourceDirectoryFile.listFiles(); // create an array that holds all of the files in the directory
					String[] filesInDirectoryString = sourceDirectoryFile.list(); // creates String array to hold file names

					for (int i = 0; i < filesInDirectory.length; i++) { // iterate over files in directory
						if (filesInDirectory[i].isFile() && filesInDirectory[i].toString().contains(filter) &&
							checkFileGreaterThanThreshold(filesInDirectory[i], daysOldThreshold)) { // check that file is a file and it contains the filter and the threshold is met
							FileInputStream fin = new FileInputStream(filesInDirectory[i]);
							origin = new BufferedInputStream(fin, BUFFER);
							ZipEntry ze = new ZipEntry(filesInDirectoryString[i]);

							zos.putNextEntry(ze);
							int count;
							while ((count = origin.read(data, 0, BUFFER)) != -1) {
								zos.write(data, 0, count);
							}
							origin.close();
							filesInDirectory[i].delete(); // deletes the original file
						}
					}
					zos.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					zos.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static boolean containsFileNameFilter(File sourceDirectory, String filter) { // method ensures at least 1 file to be zipped in directory
		String[] filesInDirectoryString = sourceDirectory.list();
		
		for (int i = 0; i < filesInDirectoryString.length; i++) {
			if (filesInDirectoryString[i].contains(filter)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkFileGreaterThanThreshold(File f, int threshold) {
		LocalDate today = LocalDate.now(); // java 8 time code
		Path filePath = f.toPath(); // convert file to path
		int year = 0; // initialize variables
		int month = 0;
		int day = 0;
		
		try {
			String fileTimeString = Files.getLastModifiedTime(filePath).toString(); //convert time to a string
			String[] splitTimeDate = fileTimeString.split("T"); //split the date/time string on the letter T
			String dateOnly = splitTimeDate[0]; // take the first part of the split to get only the date
			System.out.println("date only: " + dateOnly); //debugging take out later
			String[] dateSplitHyphen = dateOnly.split("-"); //split the date on the hyphen to year, month, and day individually
			year = Integer.parseInt(dateSplitHyphen[0]);
			month = Integer.parseInt(dateSplitHyphen[1]);
			day = Integer.parseInt(dateSplitHyphen[2]);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		LocalDate fileCreatedDate = LocalDate.of(year, month, day);
		Period daysAgoCreated = Period.between(today, fileCreatedDate); //obtain the period of time between the threshold and file last modified date
		int daysAgoCreatedInt = Integer.parseInt(daysAgoCreated.toString().replaceAll("[\\D]", "")); // convert the daysAgoCreated period to an integer
		
		if (daysAgoCreatedInt > threshold) {
			return true;
		}
		System.out.println("here test");
		return false;
	}
	
	public static boolean directoryHasFileGreaterThanThreshold(String sourceDirectoryString, int daysOldThreshold) {
		File sourceDirectoryFile = new File(sourceDirectoryString);
		File[] filesInDirectory = sourceDirectoryFile.listFiles();
		
		for (int i = 0; i < filesInDirectory.length; i++) {
			if (checkFileGreaterThanThreshold(filesInDirectory[i], daysOldThreshold)) {
				return true;
			}
		}
		return false;
	}
}