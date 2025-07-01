package smartnotes_console.dto.ai_api_request;

public class InferenceRequest {
	public String model;
	public InferenceRequestMessage[] messages;
	
	public InferenceRequest(String model, InferenceRequestMessage[] messages) {
		super();
		this.model = model;
		this.messages = messages;
	}
}
