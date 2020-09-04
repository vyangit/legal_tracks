package runnables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import business_logic.LegalFileSorter;
import data.Box;
import data.FileSortSummary;
import data.FileSortSummaryEntry;
import data.LabelledFile;
import dtos.ArchivePathsDTO;
import dtos.FileSelectionDTO;
import exceptions.SortException;
import listeners.SortListener;
import ui.SortProgressPage;

public class SortRunnable implements Runnable {

	public FileSortSummary fsSummary;
	private final SortProgressPage sortPage;
	private final ArchivePathsDTO apDTO;
	private final FileSelectionDTO fsDTO;
	private boolean isRunning;
	private List<SortListener> sortListeners;
	
	public SortRunnable(ArchivePathsDTO apDTO, FileSelectionDTO fsDTO, SortProgressPage sortPage) {
		this.apDTO = apDTO;
		this.fsDTO = fsDTO;
		this.sortPage = sortPage;
		this.sortListeners = new ArrayList<SortListener>();
	}

	public void run() {
		isRunning = true;
		logMsg("Starting sort process...");
		logMsg("");
		
		// Initiate logger
		fsSummary = new FileSortSummary(fsDTO.boxFiles.size());

		// Create new box archive for file
		List<LabelledFile> lFiles = new ArrayList<LabelledFile>();

		for (String filePath : fsDTO.boxFiles) {
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				lFiles.add(new LabelledFile(file));
			} else {
				fsSummary.failures.add(
						new FileSortSummaryEntry(file, false, "Failure: could not find original file to copy"));
			}
		}

		// Report to client number that passed clearance
		logMsg(fsDTO.boxFiles.size() + " out of " + lFiles.size() + " passed checks");
		logMsg("");

		// Copy files to box archive
		Box scannedBox = new Box(fsDTO.boxName, fsDTO.boxNumber, lFiles);
		try {
			LegalFileSorter.createScannedBoxArchiveFolder(scannedBox, new File(apDTO.boxArchiveDirPath));
		} catch (SortException e) {
			terminateSort(e.getMessage());
			return;
		}
		
		sortPage.sortProgressBar.setValue(10);

		// Init file archive folder
		File fileArchive = new File(apDTO.fileMasterArchiveDirPath);
		
		// Validate excel sheet alive state
//		File excelSheet = new File(apDTO.excelSheetFilePath);
//
//		String excelSheetExtension = apDTO.excelSheetFilePath
//				.substring(apDTO.excelSheetFilePath.lastIndexOf('.'));
//		if (!(excelSheet.isFile() && excelSheet.canWrite() && (excelSheetExtension.equals(".csv")
//				|| excelSheetExtension.equals(".xlsx") || excelSheetExtension.equals(".xls")))) {
//			terminateSort("Failure: Excel sheet is not valid");
//			return;
//		}

		// Sort files from archive to corresponding folder in file archive and edit
		// excel spreadsheet
		int progressPercent = 10;
		int progressPerFile = (int) ((1 / (float) lFiles.size()) * 90);
		int i = 0;
		int n = lFiles.size();
		for (LabelledFile lFile : lFiles) {
			i++;
			
			try {
				LegalFileSorter.sortScannedFileToMasterArchive(lFile, fileArchive);
//					ExcelEditor.editExcelFileEntry(lFile.uid, 0, excelSheet);
				fsSummary.addEntryLog(new FileSortSummaryEntry(lFile, true, "Successful sorted"));
				logMsg("Sorting " + i + " of " + n + "... SUCCESS");
			} catch (SortException e) {
				fsSummary.addEntryLog(new FileSortSummaryEntry(lFile, false, e.getMessage()));
				logMsg("Sorting " + i + " of " + n + "... FAILURE");
			} finally {
				progressPercent += progressPerFile;
				sortPage.sortProgressBar.setValue(Math.min(progressPercent, 100));
			}
		}

		logMsg("");
		logMsg("...Ending sort process");
		
		sortPage.sortProgressBar.setValue(100);
		notifySortListeners();
	}
	
	public boolean addSortListener(SortListener sl) {
		if (isRunning) return false;
		
		return this.sortListeners.add(sl);
	}
	
	private void notifySortListeners() {
		for (SortListener sl: this.sortListeners) {
			sl.completionUpdate();
		}
	}
	
	private void terminateSort(String msg) {
		logMsg(msg);
		sortPage.sortProgressBar.setValue(100);
		fsSummary.terminationMsg = msg;
		fsSummary.isCompleted = false;
		notifySortListeners();
	}
	
	private void logMsg(String msg) {
		sortPage.progressLogger.append(msg);
		sortPage.progressLogger.append("\n");
		sortPage.progressLogger.repaint();
	}
}
