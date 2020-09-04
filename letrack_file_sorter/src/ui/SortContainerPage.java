package ui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import data.FileSortSummary;
import dtos.ArchivePathsDTO;
import dtos.FileSelectionDTO;
import listeners.SortListener;
import runnables.SortRunnable;

public class SortContainerPage extends GUIPage implements SortListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7731003110479198889L;
	final static String SORT_PROGRESS_PANEL = "Sort Progress";
	final static String SORT_SUMMARY_PANEL = "Sort Summary";
	
	private ArchivePathsDTO apDTO;
	private FileSelectionDTO fsDTO;
	
	private SortRunnable sortRunnable;
	private Thread sortThread;
	
	private SortProgressPage sortProgressPage;
	private SortSummaryPage sortSummaryPage;
	private JPanel pages;
	private JButton abortBtn;
	private JButton hideSummaryBtn;
	private JButton showSummaryBtn;
	
	public SortContainerPage(ArchivePathsDTO apDTO, FileSelectionDTO fsDTO) {
		this.apDTO = apDTO;
		this.fsDTO = fsDTO;
		executeSort();
	}
	
	@Override
	public void prepareGUI() {
		// Create container
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Position card panel in container
		pages = new JPanel(new CardLayout());
		
		sortProgressPage = new SortProgressPage();
		pages.add(sortProgressPage, SORT_PROGRESS_PANEL);
		
		sortSummaryPage = new SortSummaryPage();
		pages.add(sortSummaryPage, SORT_SUMMARY_PANEL);
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(pages, gbc);
		
		// Position navigation and action buttons in container
		abortBtn = new JButton("Abort");
		abortBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				userAbort();
			}
		});
		
		showSummaryBtn = new JButton("Show summary");
		showSummaryBtn.setVisible(true);
		showSummaryBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showSummary();
			}
		});
		
		hideSummaryBtn = new JButton("Hide summary");
		hideSummaryBtn.setVisible(true);
		hideSummaryBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideSummary();
			}
		});

		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(5,5,5,5);
		this.add(abortBtn, gbc);
		this.add(showSummaryBtn, gbc);
		this.add(hideSummaryBtn, gbc);
	}
	
	private void executeSort() {
		sortRunnable = new SortRunnable(apDTO, fsDTO, sortProgressPage);
		sortRunnable.addSortListener(this);
		sortThread = new Thread(sortRunnable);
		sortThread.run();
	}
	
	private void toggleShowSummaryBtn() {
		sortSummaryPage.setFileSortSummary(sortRunnable.fsSummary);
		abortBtn.setVisible(false);
		hideSummaryBtn.setVisible(false);
		showSummaryBtn.setVisible(true);
		this.repaint();
	}
	
	private void showSummary() {
		abortBtn.setVisible(false);
		showSummaryBtn.setVisible(false);
		hideSummaryBtn.setVisible(true);
		this.repaint();
		toggleState(SORT_SUMMARY_PANEL);
	}
	
	private void hideSummary() {
		abortBtn.setVisible(false);
		hideSummaryBtn.setVisible(false);
		showSummaryBtn.setVisible(true);
		this.repaint();
		toggleState(SORT_PROGRESS_PANEL);
	}
	
	private void toggleState(String pageId) {
		CardLayout cl = (CardLayout) pages.getLayout();
		cl.show(pages, pageId);
	}

	private void userAbort() {
		sortThread.interrupt();
		FileSortSummary fss = sortRunnable.fsSummary;
		fss.isCompleted = true;
		fss.terminationMsg = "User aborted sort";
		toggleShowSummaryBtn();
	}
	
	@Override
	public void completionUpdate() {
		toggleShowSummaryBtn();
	}
}
