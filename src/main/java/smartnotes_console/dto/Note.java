package smartnotes_console.dto;

public class Note {
	public int id;
	public String title;
	public String content;
	public String storeFilePath;
	
	public Note(int id, String title, String storeFilePath) {
		this.id = id;
		this.title = title;
		this.content = "";
		this.storeFilePath = storeFilePath;
	}
	
	public Note(String storeFilePath) {
		this.title = "";
		this.content = "";
		this.storeFilePath = storeFilePath;
	}
}
