package net.masterthought.cucumber.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

/**
 * @see http://javadevtips.blogspot.de/2011/10/unzip-files.html
 */
public class UnzipUtils {

    private final static int BUFFER_SIZE = 2048;
    private final static String ZIP_EXTENSION = ".zip";

    public static void unzipToFile(File srcZipFile, File destDirectory) {
        try {
            // create the destination directory structure (if needed)
            destDirectory.mkdirs();

            // open archive for reading
            try (ZipFile zipFile = new ZipFile(srcZipFile, ZipFile.OPEN_READ)) {

                // for every zip archive entry do
                Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
                while (zipFileEntries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

                    // create destination file
                    File destFile = new File(destDirectory, entry.getName());

                    // create parent directories if needed
                    File parentDestFile = destFile.getParentFile();
                    parentDestFile.mkdirs();

                    if (!entry.isDirectory()) {
                        try (BufferedInputStream bufIS = new BufferedInputStream(zipFile.getInputStream(entry))) {

                            // write the current file to disk
                            try (BufferedOutputStream bufOS = new BufferedOutputStream(new FileOutputStream(destFile),
                                    BUFFER_SIZE)) {
                                IOUtils.copy(bufIS, bufOS);
                            }
                            // recursively unzip files
                            if (entry.getName().toLowerCase().endsWith(ZIP_EXTENSION)) {
                                String zipFilePath = destDirectory.getPath() + File.separatorChar + entry.getName();

                                unzipToFile(
                                        new File(zipFilePath),
                                        new File(
                                                zipFilePath.substring(0, zipFilePath.length() - ZIP_EXTENSION.length())));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
