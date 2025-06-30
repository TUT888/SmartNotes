package smartnotes_console.dto.ai_api_request;

public class InferenceRequestMessage {
	public String role;
	public String content;
	
	public InferenceRequestMessage(String role, String content) {
		this.role = role;
		this.content = content;
	}
}
