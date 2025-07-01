package smartnotes_console.dto.ai_api_request;

public class GuidedInferenceRequest extends InferenceRequest {
	public double temperature;
	public double top_p;
	public String guided_json;
	
	public GuidedInferenceRequest(String model, InferenceRequestMessage[] messages, double temperature, double top_p, String guided_json) {
		super(model, messages);
		this.temperature = temperature;
		this.top_p = top_p;
		this.guided_json = guided_json;
	}
}
