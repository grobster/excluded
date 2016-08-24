package com.grobster.util;

import java.io.*;
import java.util.zip.*;

public class MyZipper {
	final public static int BUFFER = 1024 * 4;
	
	public static void zipFile(String in, String out) {
		try {
			BufferedInputStream bis = null;
			FileOutputStream fos = new FileOutputStream(out);
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
			zos.setMethod(ZipOutputStream.DEFLATED);
			
			byte data[] = new byte[BUFFER];
			
			FileInputStream fis = new FileInputStream(in);
			bis = new BufferedInputStream(fis, BUFFER);
			ZipEntry zip = new ZipEntry(new File(in).getName());
			zos.putNextEntry(zip);
			
			int count;
			while((count = bis.read(data, 0, BUFFER)) != -1 ) {
				zos.write(data, 0, count);
			}
			//zos.closeEntry();
			bis.close();
			
			zos.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String fileIn = "C:\\Users\\quarl\\Documents\\jefftoday.txt";
		String fileOut = "C:\\Users\\quarl\\Documents\\jefftoday.zip";
		zipFile(fileIn, fileOut);
	}
}