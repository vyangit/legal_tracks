package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import dtos.FileSelectionDTO;
import exceptions.SortParamException;

public class BoxSetupPage extends GUIPage {
	private static final long serialVersionUID = 469199057146638676L;
	
	String currentOpenFolderParam = "";
	
	JTextField boxTextInput;
	JTextField boxNumberInput;
	JTextField openFolderText;
	
	JList<File> displayedList;
	JList<File> selectedList;
	
	Set<File> displayedFiles = new HashSet<File>(); // absolute path to file
	Set<File> selectedFiles = new HashSet<File>();

	@Override 
	public void prepareGUI() {
		
		// Set layout
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		// Add page header
		JLabel pageHeader = new JLabel("Files to box setup");
		this.add(pageHeader);
		layout.putConstraint(SpringLayout.NORTH, pageHeader, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, pageHeader, 5, SpringLayout.WEST, this);
		
		// Place box file name text input field
		JLabel boxTextInputLabel = new JLabel("File box name: ");
		boxTextInput = new JTextField();
		this.add(boxTextInputLabel);
		this.add(boxTextInput);
		layout.putConstraint(SpringLayout.NORTH, boxTextInputLabel, 10, SpringLayout.SOUTH, pageHeader);
		layout.putConstraint(SpringLayout.WEST, boxTextInputLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.BASELINE, boxTextInput, 0, SpringLayout.BASELINE, boxTextInputLabel);
		layout.putConstraint(SpringLayout.WEST, boxTextInput, 0, SpringLayout.EAST, boxTextInputLabel);
		layout.putConstraint(SpringLayout.EAST, boxTextInput, -5, SpringLayout.EAST, this);
		
		// Place box number text input field
		JLabel boxNumberInputLabel = new JLabel("File box number: ");
		boxNumberInput = new JTextField();
		this.add(boxNumberInputLabel);
		this.add(boxNumberInput);
		layout.putConstraint(SpringLayout.NORTH, boxNumberInputLabel, 10, SpringLayout.SOUTH, boxTextInputLabel);
		layout.putConstraint(SpringLayout.WEST, boxNumberInputLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.BASELINE, boxNumberInput, 0, SpringLayout.BASELINE, boxNumberInputLabel);
		layout.putConstraint(SpringLayout.WEST, boxNumberInput, 0, SpringLayout.EAST, boxNumberInputLabel);
		layout.putConstraint(SpringLayout.EAST, boxNumberInput, -5, SpringLayout.EAST, this);
				
		
		// Place file chooser button label
		JLabel selectedFolderLabel = new JLabel("Current open folder:");
		this.add(selectedFolderLabel);
		layout.putConstraint(SpringLayout.NORTH, selectedFolderLabel, 10, SpringLayout.SOUTH, boxNumberInput);
		layout.putConstraint(SpringLayout.WEST, selectedFolderLabel, 5, SpringLayout.WEST, this);

		// Construct and place file chooser section
		openFolderText = new JTextField();
		openFolderText.setEditable(false);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton openFolderBtn = new JButton("Open folder");
		openFolderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnFlag = fc.showDialog(openFolderBtn, "Open");
				if (returnFlag == JFileChooser.APPROVE_OPTION) {
					updateOpenFolderParam(fc.getSelectedFile().toString());
				}
			}	
		});
		this.add(openFolderText);
		this.add(openFolderBtn);
		
		layout.putConstraint(SpringLayout.BASELINE, openFolderText, 0, SpringLayout.BASELINE, selectedFolderLabel);
		layout.putConstraint(SpringLayout.NORTH, openFolderText, 0, SpringLayout.NORTH, openFolderBtn);
		layout.putConstraint(SpringLayout.SOUTH, openFolderText, 0, SpringLayout.SOUTH, openFolderBtn);
		layout.putConstraint(SpringLayout.EAST, openFolderText, 0, SpringLayout.WEST, openFolderBtn);
		layout.putConstraint(SpringLayout.WEST, openFolderText, 5, SpringLayout.EAST, selectedFolderLabel);
		
		layout.putConstraint(SpringLayout.BASELINE, openFolderBtn, 0, SpringLayout.BASELINE, selectedFolderLabel);
		layout.putConstraint(SpringLayout.EAST, openFolderBtn, -5, SpringLayout.EAST, this);
	
		// Construct and place file list views container
		JPanel fileListContainer = constructFileListContainer();
		this.add(fileListContainer);
		layout.putConstraint(SpringLayout.NORTH, fileListContainer, 10, SpringLayout.SOUTH, openFolderText);
		layout.putConstraint(SpringLayout.WEST, fileListContainer, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, fileListContainer, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, fileListContainer, -5, SpringLayout.SOUTH, this);
	}

	private JPanel constructFileListContainer() {
		
		// Setup container
		JPanel container = new JPanel();
		SpringLayout sl = new SpringLayout();
		container.setLayout(sl);

		// Construct labels
		JLabel displayedFilesLabel = new JLabel("Folder files");
		container.add(displayedFilesLabel);
		sl.putConstraint(SpringLayout.WEST, displayedFilesLabel, 0, SpringLayout.WEST, container);
		sl.putConstraint(SpringLayout.NORTH, displayedFilesLabel, 0, SpringLayout.NORTH, container);
		
		JLabel selectedFilesLabel = new JLabel("Selected files to sort");
		container.add(selectedFilesLabel);
		sl.putConstraint(SpringLayout.EAST, selectedFilesLabel, 0, SpringLayout.EAST, container);
		sl.putConstraint(SpringLayout.NORTH, selectedFilesLabel, 0, SpringLayout.NORTH, container);
		
		// Construct list views
		displayedList = new JList<File>(new File[]{});
		displayedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane displayedScrollPane = new JScrollPane(displayedList);
		container.add(displayedScrollPane);
		sl.putConstraint(SpringLayout.WEST, displayedScrollPane, 0, SpringLayout.WEST, container);
		sl.putConstraint(SpringLayout.NORTH, displayedScrollPane, 0, SpringLayout.SOUTH, displayedFilesLabel);
		sl.putConstraint(SpringLayout.SOUTH, displayedScrollPane, 0, SpringLayout.SOUTH, container);
		
		selectedList = new JList<File>(new File[]{});
		selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane selectedScrollPane = new JScrollPane(selectedList);
		container.add(selectedScrollPane);
		sl.putConstraint(SpringLayout.EAST, selectedScrollPane, 0, SpringLayout.EAST, container);
		sl.putConstraint(SpringLayout.NORTH, selectedScrollPane, 0, SpringLayout.SOUTH, selectedFilesLabel);
		sl.putConstraint(SpringLayout.SOUTH, selectedScrollPane, 0, SpringLayout.SOUTH, container);

		// Reset and init gbc params for arrow buttons
		JPanel btnArrowWrapper = new JPanel(new GridBagLayout());
		GridBagConstraints wrapperGBC = new GridBagConstraints();
		wrapperGBC.gridx = 0;
		wrapperGBC.gridy = 0;
		wrapperGBC.insets = new Insets(5,5,5,5);
		
		// Place arrow buttons
		JButton selectArrowBtn = new JButton(">>");
		selectArrowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFilesToSelectedList();
			}
		});
		btnArrowWrapper.add(selectArrowBtn, wrapperGBC);

		JButton unselectArrowBtn = new JButton("<<");
		unselectArrowBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeFilesFromSelectedList();
			}
		});
		wrapperGBC.gridy++;
		btnArrowWrapper.add(unselectArrowBtn, wrapperGBC);
		
		container.add(btnArrowWrapper);
		sl.putConstraint(SpringLayout.WEST, selectedScrollPane, 0, SpringLayout.EAST, btnArrowWrapper);
		sl.putConstraint(SpringLayout.EAST, displayedScrollPane, 0, SpringLayout.WEST, btnArrowWrapper);
		sl.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnArrowWrapper, 0, SpringLayout.HORIZONTAL_CENTER, container);
		sl.putConstraint(SpringLayout.VERTICAL_CENTER, btnArrowWrapper, 0, SpringLayout.VERTICAL_CENTER, container);
		
		return container;
	}
	
	public FileSelectionDTO getFileSelectionDTO() throws SortParamException {
		validateInput();
		int boxNumber = Integer.parseInt(boxNumberInput.getText());
		return new FileSelectionDTO(boxTextInput.getText(), 
				boxNumber,
				new ArrayList<String>(selectedFiles.stream().map(file -> file.toString()).collect(Collectors.toList())));
	}
	
	/**
	 * Updates the observed folder path of the gui
	 * 
	 * @param folderPath The path to the folder to open
	 * @return True if folderPath is valid else false
	 */
	private boolean updateOpenFolderParam(String folderPath) {
		if (new File(folderPath).isDirectory()) {
			currentOpenFolderParam = folderPath;
			openFolderText.setText(currentOpenFolderParam);
			repaintFileListViews();
			return true;
		} 
		
		return false;
	}
	
	private void repaintFileListViews() {
	
		// Filter already selected files from display area
		File folder = new File(currentOpenFolderParam);
		
		if (folder.isDirectory()) {
			displayedFiles.clear();
			File[] dirFiles = folder.listFiles();
			
			for (File file: dirFiles) {
				if (file.isFile() && !selectedFiles.contains(file)) {
					displayedFiles.add(file);
				}
			}
		}
		
		// Repaint displayed lists
		// TODO: Replace list data with list models?
		displayedList.setListData(displayedFiles.toArray(new File[displayedFiles.size()]));
		selectedList.setListData(selectedFiles.toArray(new File[selectedFiles.size()]));
		
		displayedList.validate();
		selectedList.validate();
		displayedList.repaint();
		selectedList.repaint();
	}

	/**
	 * Adds files to the selected list GUI view
	 */
	private void addFilesToSelectedList() {
		List<File> files = displayedList.getSelectedValuesList();
		for (File file: files) {
			if (file.isFile()) {
				selectedFiles.add(file);
			}
		}
		repaintFileListViews();
	}
	
	/**
	 * Removes files from the selected list GUI view
	 */
	private void removeFilesFromSelectedList() {
		List<File> files = selectedList.getSelectedValuesList();
		for (File file: files) {
			selectedFiles.remove(file);
		}
		repaintFileListViews();
	}
	
	/**
	 * Checks to see if the sort parameters are valid 
	 * 
	 * @return true if input is valid else false
	 */
	private void validateInput() throws SortParamException {
		Pattern boxNumPattern = Pattern.compile("\\d+");
		Matcher boxNumMatcher = boxNumPattern.matcher(boxNumberInput.getText());
		if (!boxNumMatcher.matches()) throw new SortParamException("Input a valid numeric box number");
	}
	
}
