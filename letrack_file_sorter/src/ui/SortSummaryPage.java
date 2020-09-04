package ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import data.FileSortSummary;
import data.FileSortSummaryEntry;

public class SortSummaryPage extends GUIPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6084447430265643581L;

	private FileSortSummary fsSummary;
	private JLabel processRatioValueLabel;
	private JLabel statusValueLabel;
	private JLabel successValueLabel;
	private JLabel failuresValueLabel;
	private JTabbedPane fileSortSummaryPanes;
	
	@Override
	public void prepareGUI() {
		// TODO Auto-generated method stub
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		// Place header
		JLabel pageHeader = new JLabel("File sort - Summary");
		
		this.add(pageHeader);
		layout.putConstraint(SpringLayout.NORTH, pageHeader, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, pageHeader, 5, SpringLayout.WEST, this);
		
		// Summary panel
		SpringLayout summaryLayout = new SpringLayout();
		JPanel summaryPanel = new JPanel();
		summaryPanel.setLayout(summaryLayout);
		
		JLabel processRatioLabel = new JLabel("Processed ratio: ");
		JLabel statusLabel = new JLabel("Sort end status: ");
		JLabel successLabel = new JLabel("Successful sorts: ");
		JLabel failuresLabel = new JLabel("Failed sorts: ");
		
		processRatioValueLabel = new JLabel();
		statusValueLabel = new JLabel();
		successValueLabel = new JLabel();
		failuresValueLabel = new JLabel();
		
		summaryPanel.add(processRatioLabel);
		summaryPanel.add(statusLabel);
		summaryPanel.add(successLabel);
		summaryPanel.add(failuresLabel);
		summaryPanel.add(processRatioValueLabel);
		summaryPanel.add(statusValueLabel);
		summaryPanel.add(successValueLabel);
		summaryPanel.add(failuresValueLabel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, processRatioLabel, 0, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.WEST, processRatioLabel, 0, SpringLayout.WEST, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.WEST, processRatioValueLabel, 0, SpringLayout.EAST, processRatioLabel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, statusLabel, 5, SpringLayout.SOUTH, processRatioLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, statusLabel, 0, SpringLayout.WEST, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.NORTH, statusValueLabel, 0, SpringLayout.NORTH, statusLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, statusValueLabel, 0, SpringLayout.EAST, statusLabel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, successLabel, 0, SpringLayout.NORTH, processRatioLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, successLabel, 0, SpringLayout.HORIZONTAL_CENTER, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.WEST, successValueLabel, 0, SpringLayout.EAST, successLabel);

		summaryLayout.putConstraint(SpringLayout.NORTH, failuresLabel, 5, SpringLayout.SOUTH, successLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, failuresLabel, 0, SpringLayout.WEST, successLabel);
		summaryLayout.putConstraint(SpringLayout.NORTH, failuresValueLabel, 0, SpringLayout.NORTH, failuresLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, failuresValueLabel, 0, SpringLayout.EAST, failuresLabel);
		
		this.add(summaryPanel);
		layout.putConstraint(SpringLayout.NORTH, summaryPanel, 10, SpringLayout.SOUTH, pageHeader);
		layout.putConstraint(SpringLayout.WEST, summaryPanel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, summaryPanel, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, summaryPanel, 60, SpringLayout.SOUTH, pageHeader);
		// Construct tab container
		fileSortSummaryPanes = new JTabbedPane();

		this.add(fileSortSummaryPanes);
		layout.putConstraint(SpringLayout.NORTH, fileSortSummaryPanes, 0, SpringLayout.SOUTH, summaryPanel);
		layout.putConstraint(SpringLayout.WEST, fileSortSummaryPanes, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, fileSortSummaryPanes, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, fileSortSummaryPanes, -5, SpringLayout.SOUTH, this);
	}
	
	public void setFileSortSummary(FileSortSummary fss) {
		this.fsSummary = fss;
		
		// Set stats
		processRatioValueLabel.setText(Integer.toString(fss.getNumProcessed()) + " / " + Integer.toString(fss.numFiles));
		statusValueLabel.setText(fss.isCompleted ? "COMPLETED" : "INCOMPLETE");
		successValueLabel.setText(Integer.toString(fss.successes.size()));
		failuresValueLabel.setText(Integer.toString(fss.failures.size()));
		
		String[] columnHeaders = new String[] {"File Name", "File ID", "Status", "Comments"};
		
		int numCol = columnHeaders.length;
		String[][] overviewData = new String[fsSummary.failures.size()+fsSummary.successes.size()][numCol];
		String[][] successData = new String[fsSummary.successes.size()][numCol];
		String[][] failureData = new String[fsSummary.failures.size()][numCol];
		
		int i = 0;
		int fI = 0;
		int sI = 0;
		for (FileSortSummaryEntry entry: fsSummary.overview) {
			overviewData[i][0] = entry.file.toString();
			overviewData[i][1] = entry.labelledFileUID;
			overviewData[i][2] = entry.isSuccessful  ? "SUCCESS" : "FAILURE";
			overviewData[i][3] = entry.logStatement;
			
			if (entry.isSuccessful) {
				successData[sI++] = overviewData[i];
			} else {
				failureData[fI++] = overviewData[i];
			}
			i++;
		}
		
		
		// Create tabular data
		JTable fileOverviewSummaryTable = new JTable(overviewData, columnHeaders);
		JScrollPane overviewSummaryPanel = new JScrollPane(fileOverviewSummaryTable);
		
		JTable fileSuccessSummaryTable = new JTable(successData, columnHeaders);
		JScrollPane successSummaryPanel = new JScrollPane(fileSuccessSummaryTable);
		
		JTable fileFailureSummaryTable = new JTable(failureData, columnHeaders);
		JScrollPane failureSummaryPanel = new JScrollPane(fileFailureSummaryTable);
				
		fileSortSummaryPanes.add("Overview (" + Integer.toString(overviewData.length) + ")", overviewSummaryPanel);
		fileSortSummaryPanes.add("Successes (" + Integer.toString(successData.length) + ")", successSummaryPanel);
		fileSortSummaryPanes.add("Failures (" + Integer.toString(failureData.length) + ")", failureSummaryPanel);
		
		this.repaint();
	}
}
