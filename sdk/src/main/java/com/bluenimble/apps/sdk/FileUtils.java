package com.bluenimble.apps.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	public static void copy (File source, File destFolder, boolean copyRoot) throws IOException {
		
		if (source == null || destFolder == null) {
			throw new IOException ("null args for FileUtils.copy");
		}
		
		if (source.isFile ()) {
			copyFile (source, destFolder);
			return;
		}
		
		if (copyRoot) {
			destFolder = new File (destFolder, source.getName ());
			destFolder.mkdir ();
		}
		
		File [] files = source.listFiles ();
		for (File file : files) {
			copy (file, destFolder, true);
		}
		
	}
	
	private static void copyFile (File f, File folder) throws IOException {
		
		if (f == null || !f.exists () || !f.isFile ()) {
			throw new IOException ("'" + f + "' not a valid file");
		}
		
		File df = new File (folder, f.getName ());
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream (f);
			os = new FileOutputStream (df);
			IOUtils.copy (is, os);
		} finally {
			IOUtils.closeQuietly (is);
			IOUtils.closeQuietly (os);
		} 
	}
	
	public static boolean delete (File source) throws IOException {
		
		if (source.isDirectory ()) {
			File [] files = source.listFiles ();
			for (File file : files) {
				delete (file);
			}
		} 
		
		return source.delete ();
		
	}
	
	public static void main (String [] args) throws IOException {
		copy (new File ("/semantic"), new File ("/tmp"), false);
	}

}
