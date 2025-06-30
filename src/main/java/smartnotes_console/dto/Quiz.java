package smartnotes_console.dto;

public class Quiz {
	public String question;
	public String[] options;
	public int correctIndex;
	
	public Quiz() {
		this.question = "";
		this.options = new String[4];
		this.correctIndex = -1;
	}
	
	public Quiz(String question, String[] options, int correctIndex) {
		this.question = question;
		this.options = options;
		this.correctIndex = correctIndex;
	}
}
