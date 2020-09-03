package data;

import java.io.File;

public class FileSortSummaryEntry {
	public File file;
	public String labelledFileUID;
	public boolean isSuccessful;
	public String logStatement;
	
	public FileSortSummaryEntry(File file, boolean isSuccessful, String logStatement) {
		this.file = file;
		this.labelledFileUID = "";
		this.isSuccessful = isSuccessful;
		this.logStatement = logStatement;
	}
	

	public FileSortSummaryEntry(LabelledFile lFile, boolean isSuccessful, String logStatement) {
		this.file = lFile.originalFile;
		this.labelledFileUID = lFile.uid;
		this.isSuccessful = isSuccessful;
		this.logStatement = logStatement;
	}
}
