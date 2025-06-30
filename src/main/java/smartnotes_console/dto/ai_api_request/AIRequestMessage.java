package smartnotes_console.dto.ai_api_request;

public class AIRequestMessage {
	String role;
	String content;
	
	public AIRequestMessage(String role, String content) {
		this.role = role;
		this.content = content;
	}
}
