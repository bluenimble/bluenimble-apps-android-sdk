package com.bluenimble.apps.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchiveUtils {

	public static void decompress (final InputStream inputStream, final File folder) throws IOException {
		final ZipInputStream zis = new ZipInputStream (new BufferedInputStream (inputStream));
		ZipEntry ze;
		try {
			while (null != (ze = zis.getNextEntry ())) {
				final File f = new File (folder, ze.getName ());
				if (f.exists ()) {
					f.delete ();
				}

				if (ze.isDirectory ()) {
					f.mkdirs ();
					continue;
				}
				f.getParentFile ().mkdirs ();
				final OutputStream fos = new BufferedOutputStream (new FileOutputStream (f));

				try {
					try {
						final byte [] buf = new byte [8192];
						int bytesRead;
						while (-1 != (bytesRead = zis.read (buf))) {
							fos.write(buf, 0, bytesRead);
						}
					} finally {
						fos.close ();
					}
				} catch (final IOException ioe) {
					f.delete ();
					throw ioe;
				}
			}
		} finally {
			zis.close();
		}
	}
	
	public static void decompress (final File file, final File folder, final boolean deleteZipAfter) throws IOException {
		decompress (new FileInputStream (file), folder);
		if (deleteZipAfter) {
			file.delete ();
		}
	}

	public static void decompress (final String fileName,
			final String folderName, final boolean deleteZipAfter)
			throws IOException {
		decompress(new File(fileName), new File(folderName), deleteZipAfter);
	}

	public static void decompress (final String fileName, final String folderName)
			throws IOException {
		decompress(new File(fileName), new File(folderName), false);
	}

	public static void decompress (final File file, final boolean deleteZipAfter)
			throws IOException {
		decompress(file, file.getCanonicalFile().getParentFile(),
				deleteZipAfter);
	}

	public static void decompress (final String fileName,
			final boolean deleteZipAfter) throws IOException {
		decompress(new File(fileName), deleteZipAfter);
	}

	public static void decompress (final File file) throws IOException {
		decompress(file, file.getCanonicalFile().getParentFile(), false);
	}

	public static void decompress (final String fileName) throws IOException {
		decompress(new File(fileName));
	}
	
	public static void compress (File folder, File zipFile, boolean contentOnly) throws IOException {
		
		ZipOutputStream zip = new ZipOutputStream (new FileOutputStream (zipFile));
						zip.setMethod 	(ZipOutputStream.DEFLATED);
						zip.setLevel 	(Deflater.BEST_COMPRESSION);
		
		if (contentOnly) {
			File [] files = folder.listFiles ();
			if (files != null && files.length > 0) {
				for (File f : files) {
					addEntry (null, f, zip);
				}
			}
		} else {
			addEntry (null, folder, zip);
		}
		
		IOUtils.closeQuietly (zip);
		
	}
	
	private static void addEntry (String parent, File file, ZipOutputStream zip) throws IOException {
		
		String name = file.getName () +	(file.isDirectory () ? Lang.SLASH : Lang.BLANK);
		if (parent != null) {
			if (parent.endsWith (Lang.SLASH)) {
				parent = parent.substring (0, parent.length () - 1);
			}
			name = parent + Lang.SLASH + name;
		}
		
		if (file.isDirectory ()) {
			File [] files = file.listFiles ();
			if (files == null || files.length == 0) {
				ZipEntry entry = new ZipEntry (name);
				entry.setTime (file.lastModified ());
				zip.putNextEntry (entry);
			} else {
				for (File f : files) {
					addEntry (name, f, zip);
				}
			}
		} else {
			final ZipEntry entry = new ZipEntry (name);
			entry.setTime (file.lastModified ());
			entry.setSize (file.length ());
			
			zip.putNextEntry (entry);
			
			InputStream in = null;
			try {
				in = new BufferedInputStream (new FileInputStream (file));
				IOUtils.copy (in, zip);
			} finally {
				IOUtils.closeQuietly (in);
			}
			zip.closeEntry ();
		}
		
	}
	
	public static void main (String[] args) throws IOException {
		compress (new File ("/temp/odwek"), new File ("/temp/file.dar"), true);
	}
	
}
