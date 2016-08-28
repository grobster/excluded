package com.grobster.util;

import java.io.*;
import java.util.zip.*;

public class MyZipper {
	public static int BUFFER = 1024 * 4;
	final public static int MIN_BUFFER = 2;
	final public static int MED_BUFFER = 4;
	final public static int MAX_BUFFER = 8;
	
	/**
	  * Zips a file.
	  */
	public static void zipFile(String in, String out) {
		BufferedInputStream bis = null;
		ZipOutputStream zos = null;
		
		try {
			FileOutputStream fos = new FileOutputStream(out);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
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
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				bis.close();
				zos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}
}