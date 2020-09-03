package data;

import java.io.File;

public class FileSortSummaryEntry {
	public File file;
	public boolean isSuccessful;
	public String logStatement;
	
	public FileSortSummaryEntry(File file, boolean isSuccessful, String logStatement) {
		this.file = file;
		this.isSuccessful = isSuccessful;
		this.logStatement = logStatement;
	}
}
