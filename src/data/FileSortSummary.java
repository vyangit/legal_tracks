package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSortSummary {	
	public Date runDate;
	public boolean isCompleted;
	public String terminationMsg;
	public int numFiles;
	public List<FileSortSummaryEntry> overview; // In order of processing
	public List<FileSortSummaryEntry> successes;
	public List<FileSortSummaryEntry> failures;
	
	public FileSortSummary(int numFiles) {
		runDate = new Date();
		isCompleted = false;
		terminationMsg = "";
		this.numFiles = numFiles;
		overview = new ArrayList<FileSortSummaryEntry>();
		successes = new ArrayList<FileSortSummaryEntry>();
		failures = new ArrayList<FileSortSummaryEntry>();
	}
	
	public void addEntryLog(FileSortSummaryEntry entry) {
		overview.add(entry);
		
		if (entry.isSuccessful) {
			successes.add(entry);
		} else {
			failures.add(entry);
		}
	}
	
	public void addEntryLog(List<FileSortSummaryEntry> entries) {
		for (FileSortSummaryEntry entry: entries) {
			addEntryLog(entry);
		}
	}
	
	public int getNumProcessed() {
		return successes.size() + failures.size();
	}
	
}
