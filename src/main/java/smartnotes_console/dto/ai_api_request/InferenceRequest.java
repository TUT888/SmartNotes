package smartnotes_console.dto.ai_api_request;

public class InferenceRequest {
	public String model;
	public double temperature;
	public double top_p;
	public InferenceRequestMessage[] messages;
	
	public InferenceRequest(String model, double temperature, double top_p, String role, String promptForAI) {
		this.model = model;
		this.temperature = temperature;
		this.top_p = top_p;
		this.messages = new InferenceRequestMessage[] {new InferenceRequestMessage(role, promptForAI)};
	}
}
