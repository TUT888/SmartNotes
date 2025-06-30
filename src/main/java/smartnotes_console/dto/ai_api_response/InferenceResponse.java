package smartnotes_console.dto.ai_api_response;

public class InferenceResponse {
	public String id;
	public InferenceResponseChoice[] choices;
	public String model;
	public String object;
}
