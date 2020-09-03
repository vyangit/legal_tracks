package data;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LabelledFile {
	public String uid;
	public File originalFile;
	
	public LabelledFile(File originalFile) {
		this.uid = createUIDfromFileName(originalFile.getName());
		this.originalFile = originalFile;
	}
	
	/**
	 * Creates a unique id based on a standardized file name
	 *
	 * @param fileName Name of file
	 * @return
	 */
	private String createUIDfromFileName(String fileName) {
		Pattern uidPattern = Pattern.compile("\\A(\\d+)-[A-Z]");
		Matcher match = uidPattern.matcher(fileName);
		if (match.find()) {
			return match.group();
		}
		
		return null;
	}
}
