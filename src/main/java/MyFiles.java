package com.grobster.util;

import java.io.File;

public class MyFiles {
	public static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }
	
	public static boolean isNotLocked (File f) {
		return f.renameTo(f);
	}
	
}