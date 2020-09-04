package business_logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;

import data.Box;
import data.LabelledFile;
import exceptions.SortException;

public class LegalFileSorter {

	public static void createScannedBoxArchiveFolder(Box box, File boxArchive) throws SortException {
		// Create new box folder
		File scannedBoxFolder = new File(boxArchive.toString() + "\\" + box.boxTitle);
		if (scannedBoxFolder.exists() && scannedBoxFolder.isDirectory()) {
			throw new SortException("Failure: '" + scannedBoxFolder.toString() + "' box folder already exists");
		}

		scannedBoxFolder.mkdir();

		// Copy files to box folder
		String boxArchivePath = boxArchive.toString();
		for (LabelledFile file : box.files) {
			Path dstPath = new File(boxArchivePath + "\\" + box.boxTitle + "\\" + file.originalFile.getName()).toPath();

			if (file.originalFile.exists() && file.originalFile.isFile()) {
				try {
					Files.copy(file.originalFile.toPath(), dstPath);
				} catch (Exception e) {
					throw new SortException("Failure: File is invalid or already exists in box");
				}
			} else {
				// Error out of non validated input
				throw new SortException("Failure: Could not find original file to copy");
			}
		}
	}

	public static void sortScannedFileToMasterArchive(LabelledFile file, File fileArchive) throws SortException {
		if (!fileArchive.isDirectory()) {
			throw new SortException("Failure: File archive directory not valid");
		}
		
		// DFS since BFS will probably load everything before doing real checking work
		ArrayDeque<File> dirStack = new ArrayDeque<File>();
		dirStack.push(fileArchive);

		while (!dirStack.isEmpty()) {
			File tmp = dirStack.pop();
			if (tmp.getName().equals(file.uid)) {
				// Sort file into matching directory
				String dstPath = tmp.toString() + "\\" + file.uid + " Closed file scanned.pdf";
				File dstFile = new File(dstPath);
				if (dstFile.exists() && dstFile.isFile()) {
					throw new SortException("Failure: File already exists at " + dstPath);
				}
				try {
					Files.copy(file.originalFile.toPath(), dstFile.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			for (File f : tmp.listFiles()) {
				if (f.isDirectory()) {
					dirStack.push(f);
				}
			}
		}

		throw new SortException("Failure: File directory not found");
	}
}
