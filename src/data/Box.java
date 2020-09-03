package data;

import java.util.List;

public class Box {
	public String boxTitle;
	public int boxNumber;
	public List<LabelledFile> files;
	
	public Box(String boxTitle, int boxNumber, List<LabelledFile> files) {
		this.boxTitle = boxTitle;
		this.boxNumber = boxNumber;
		this.files = files;
	}
}
