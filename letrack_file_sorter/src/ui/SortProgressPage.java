package ui;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class SortProgressPage extends GUIPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868939624698874547L;

	public JLabel progressBarLabel;
	public JProgressBar sortProgressBar;
	public JTextArea progressLogger;
	
	public AtomicInteger progressPercent;

	@Override
	public void prepareGUI() {

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		// Place page header
		JLabel pageHeader = new JLabel("File sorter - Progress");
		this.add(pageHeader);
		layout.putConstraint(SpringLayout.NORTH, pageHeader, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, pageHeader, 5, SpringLayout.WEST, this);

		// Place progress bar
		progressBarLabel = new JLabel("In progress");
		sortProgressBar = new JProgressBar(0, 100);
		sortProgressBar.setStringPainted(true);
		this.add(progressBarLabel);
		this.add(sortProgressBar);

		layout.putConstraint(SpringLayout.NORTH, progressBarLabel, 5, SpringLayout.SOUTH, pageHeader);
		layout.putConstraint(SpringLayout.WEST, progressBarLabel, 0, SpringLayout.WEST, sortProgressBar);

		layout.putConstraint(SpringLayout.NORTH, sortProgressBar, 5, SpringLayout.SOUTH, progressBarLabel);
		layout.putConstraint(SpringLayout.WEST, sortProgressBar, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, sortProgressBar, -10, SpringLayout.EAST, this);

		// Place progress log text field
		progressLogger = new JTextArea();
		progressLogger.setEditable(false);

		JScrollPane loggerPane = new JScrollPane(progressLogger);
		this.add(loggerPane);
		layout.putConstraint(SpringLayout.NORTH, loggerPane, 5, SpringLayout.SOUTH, sortProgressBar);
		layout.putConstraint(SpringLayout.WEST, loggerPane, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, loggerPane, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, loggerPane, -5, SpringLayout.SOUTH, this);
	}
}
