package com.grobster.util;

import java.text.*;
import java.util.*;

public class FileNamer {

	public static String nameFile() {
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	}
}