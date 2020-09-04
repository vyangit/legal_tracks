package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import dtos.ArchivePathsDTO;
import dtos.FileSelectionDTO;
import exceptions.SortParamException;

public class StartContainerPage extends GUIPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6248207801121919934L;
	final static String FILE_SELECTION_PANEL = "Box Setup";
	final static String ARCHIVES_SELECTION_PANEL = "Archive Paths";

	private JFrame owner;
	private BoxSetupPage boxSetupPage;
	private ArchiveSelectionPage archiveSelectionPage;
	
	public StartContainerPage(JFrame owner) {
		this.owner = owner;
	}
	
	@Override
	public void prepareGUI() {
		boxSetupPage = new BoxSetupPage();
		archiveSelectionPage = new ArchiveSelectionPage();
		
		JTabbedPane parameterPanes = new JTabbedPane();
		parameterPanes.add(FILE_SELECTION_PANEL, boxSetupPage);
		parameterPanes.add(ARCHIVES_SELECTION_PANEL, archiveSelectionPage);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(parameterPanes, gbc);
		
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		gbc.ipady = 10;
		JButton sortBtn = new JButton("Sort");
		sortBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				initiateSort();
			}
		});
		this.add(sortBtn, gbc);
	}
	
	private void initiateSort() {
		FileSelectionDTO fsDTO = null;
		ArchivePathsDTO apDTO = null;
		
		try {
			fsDTO = boxSetupPage.getFileSelectionDTO();
			apDTO = archiveSelectionPage.getArchivePathsDTO();
	
			JDialog sortDialog = new JDialog(owner, "Letrack File Sorter - Sorting \"" + fsDTO.boxName + "\"");
			sortDialog.setMinimumSize(new Dimension(400, 300));
			sortDialog.setLocationRelativeTo(this);
			SortContainerPage scPage = new SortContainerPage(apDTO, fsDTO);
			sortDialog.add(scPage);
			sortDialog.pack();
			sortDialog.setVisible(true);
		} catch (SortParamException e) {
			JDialog errorDialog = new JDialog(owner, "Letrack File Sorter - Parameter Error");
			JPanel errorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			JLabel errorLabel = new JLabel(e.getMessage());
			JButton acknowledgeBtn = new JButton("OK");
			acknowledgeBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					errorDialog.dispose();
				}
			});

			gbc.gridy = 0;
			gbc.weighty = 1;
			gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,5,0,5);
			errorPanel.add(errorLabel, gbc);
			gbc.gridy++;
			errorPanel.add(acknowledgeBtn, gbc);
			
			errorDialog.setMinimumSize(new Dimension(200, 100));
			errorDialog.setLocationRelativeTo(this);
			errorDialog.add(errorPanel);
			errorDialog.setResizable(false);
			errorDialog.pack();
			errorDialog.setVisible(true);
		}
	}
}
