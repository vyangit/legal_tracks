package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import dtos.ArchivePathsDTO;
import exceptions.SortParamException;

public class ArchiveSelectionPage extends GUIPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5559164918960750910L;
	private JTextField boxArchivePath;
	private JTextField fileMasterArchivePath;
	private JTextField excelSheetPath;

	@Override
	public void prepareGUI() {

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		// Place header
		JLabel pageHeader = new JLabel("Archive folders and files to sort or make edits to");
		this.add(pageHeader);
		layout.putConstraint(SpringLayout.NORTH, pageHeader, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, pageHeader, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, pageHeader, 5, SpringLayout.EAST, this);

		// Construct box archive selection line
		boxArchivePath = new JTextField();
		boxArchivePath.setEditable(false);
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JPanel boxArchivePathSelector = constructSelectFilePanel("Box archive:", boxArchivePath, dirChooser);
		this.add(boxArchivePathSelector);
		layout.putConstraint(SpringLayout.NORTH, boxArchivePathSelector, 5, SpringLayout.SOUTH, pageHeader);
		layout.putConstraint(SpringLayout.WEST, boxArchivePathSelector, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, boxArchivePathSelector, -5, SpringLayout.EAST, this);

		// Construct file archive selection line
		fileMasterArchivePath = new JTextField();
		fileMasterArchivePath.setEditable(false);
		JPanel fileArchivePathSelector = constructSelectFilePanel("File archive:", fileMasterArchivePath, dirChooser);
		this.add(fileArchivePathSelector);
		layout.putConstraint(SpringLayout.NORTH, fileArchivePathSelector, 5, SpringLayout.SOUTH,
				boxArchivePathSelector);
		layout.putConstraint(SpringLayout.WEST, fileArchivePathSelector, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, fileArchivePathSelector, -5, SpringLayout.EAST, this);
		
		// Construct excel selection line
		excelSheetPath = new JTextField();
		excelSheetPath.setEditable(false);

		JFileChooser excelSheetChooser = new JFileChooser();
		excelSheetChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		JPanel excelSheetPathSelector = constructSelectFilePanel("Excel list:", excelSheetPath, excelSheetChooser);
//		this.add(excelSheetPathSelector); 	// TODO: Implement excel editation
		layout.putConstraint(SpringLayout.NORTH, excelSheetPathSelector, 5, SpringLayout.SOUTH,
				fileArchivePathSelector);
		layout.putConstraint(SpringLayout.WEST, excelSheetPathSelector, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, excelSheetPathSelector, -5, SpringLayout.EAST, this);
	}

	public ArchivePathsDTO getArchivePathsDTO() throws SortParamException {
		validateInput();
		
		return new ArchivePathsDTO (
				boxArchivePath.getText(),
				fileMasterArchivePath.getText(),
				excelSheetPath.getText()
				);
	}
	
	private JPanel constructSelectFilePanel(String label, JTextField pathTextField, JFileChooser fc) {
		// Create components
		JLabel selectionLabel = new JLabel(label);
		JButton selectionBtn = new JButton("Open folder");
		JPanel container = new JPanel(new GridBagLayout());

		// Initiate file chooser
		selectionBtn.addActionListener(linkTextInputAndFileChooser(pathTextField, fc));
		
		// Position components
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		container.add(selectionLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 5, 0, 0);
		container.add(pathTextField, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		container.add(selectionBtn, gbc);

		return container;
	}

	private ActionListener linkTextInputAndFileChooser(JTextField input, JFileChooser fc) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnFlag = fc.showDialog(input, "Select");
				if (returnFlag == JFileChooser.APPROVE_OPTION) {
					input.setText(fc.getSelectedFile().toString());
				}
			}
		};
	}
	
	/**
	 * Checks to see if the sort parameters are valid
	 * 
	 */
	private void validateInput() throws SortParamException {
		// Added function stub, didn't end up using but could be used if inputs changes are needed later on
	}

}
