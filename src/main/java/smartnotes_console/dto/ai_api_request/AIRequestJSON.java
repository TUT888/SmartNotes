package smartnotes_console.dto.ai_api_request;

import java.util.ArrayList;

public class AIRequestJSON {
	String model;
	double temperature;
	double top_p;
	ArrayList<AIRequestMessage> messages;
	
	public AIRequestJSON(String model, double temperature, double top_p, String role, String promptForAI) {
		this.model = model;
		this.temperature = temperature;
		this.top_p = top_p;
		this.messages = new ArrayList<AIRequestMessage>();
		this.messages.add(new AIRequestMessage(role, promptForAI));
	}
}
