package com.grobster.util;

import java.io.*;
import java.util.zip.*;

public class Zipper {
	final public static int BUFFER = 2048;
	
	public static void zip(File inFile, File outFile) {
		try {
			BufferedInputStream origin = null;
			FileOutputStream des = new FileOutputStream(outFile);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(des));
			out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];
			FileInputStream fi = new FileInputStream(inFile.toString());
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(inFile.toString());
			out.putNextEntry(entry);
			int count;
			while((count = origin.read(data, 0, BUFFER)) != -1) {
				//out.write.(data, 0, count);
			}
			origin.close();
			out.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}