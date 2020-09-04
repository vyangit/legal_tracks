package dtos;

import java.io.File;
import java.util.List;

public class FileSelectionDTO {
	public String boxName;
	public List<String> boxFiles;
	public int boxNumber;
	
	public FileSelectionDTO(String boxName, int boxNumber, List<String> boxFiles) {
		this.boxName = boxName;
		this.boxNumber = boxNumber;
		this.boxFiles = boxFiles;
	}
}
