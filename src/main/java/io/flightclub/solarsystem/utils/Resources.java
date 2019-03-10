package io.flightclub.solarsystem.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

public class Resources {

	public static File[] getResourceFolderFiles (String folder) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folder);
		String path = url.getPath();
		return new File(path).listFiles();
	}

	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}
