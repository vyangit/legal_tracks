package dtos;

public class ArchivePathsDTO {
	public String boxArchiveDirPath;
	public String fileMasterArchiveDirPath;
	public String excelSheetFilePath;
	
	public ArchivePathsDTO(String boxArchiveDirPath, String fileMasterArchiveDirPath, String excelSheetFilePath) {
		this.boxArchiveDirPath = boxArchiveDirPath;
		this.fileMasterArchiveDirPath = fileMasterArchiveDirPath;
		this.excelSheetFilePath = excelSheetFilePath;
	}
}
