package smartnotes_console.service.ai;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import com.google.gson.Gson;

import smartnotes_console.common.Storage;
import smartnotes_console.dto.Note;
import smartnotes_console.dto.ai_api_request.InferenceRequest;
import smartnotes_console.dto.ai_api_response.InferenceResponse;

public class QuizGenerationService {
	public String generateSampleQuiz() {
		// Below is sample response got from fetchResponseFromInferenceProvider()
		String responseAsJSONString = "{\"id\":\"chat-sample-id\",\"choices\":[{\"finish_reason\":\"stop\",\"index\":0,\"logprobs\":null,\"message\":{\"content\":\"**QUESTION** 1: What does Time Complexity measure? **END QUESTION**\\r\\n**OPTION** 1:  The memory used by an algorithm. **END OPTION**\\r\\n**OPTION** 2: How the runtime of an algorithm increases with input size. **END OPTION**\\r\\n**OPTION** 3: The type of data structure used by an algorithm. **END OPTION**\\r\\n**OPTION** 4: The number of iterations performed by an algorithm. **END OPTION**\\r\\n**ANS**: 2 **END ANS**\\r\\n\\r\\n**QUESTION** 2: What is the time complexity of a Binary Search? **END QUESTION**\\r\\n**OPTION** 1: O(1) **END OPTION**\\r\\n**OPTION** 2: O(log n) **END OPTION**\\r\\n**OPTION** 3: O(n) **END OPTION**\\r\\n**OPTION** 4: O(n log n) **END OPTION**\\r\\n**ANS**: 2 **END ANS**\\r\\n\\r\\n**QUESTION** 3: Which of the following is NOT a common time complexity? **END QUESTION**\\r\\n**OPTION** 1: O(1) **END OPTION**\\r\\n**OPTION** 2: O(n) **END OPTION**\\r\\n**OPTION** 3: O(n^2) **END OPTION**\\r\\n**OPTION** 4: O(2^n) **END OPTION**\\r\\n**ANS**: 4 **END ANS**\\r\\n\",\"refusal\":null,\"role\":\"assistant\",\"audio\":null,\"function_call\":null,\"tool_calls\":[],\"reasoning_content\":null},\"stop_reason\":107}],\"created\":1234567890,\"model\":\"google/gemma-2-2b-it\",\"object\":\"chat.completion\",\"service_tier\":null,\"system_fingerprint\":null,\"usage\":{\"completion_tokens\":858,\"prompt_tokens\":891,\"total_tokens\":1749,\"completion_tokens_details\":null,\"prompt_tokens_details\":null},\"prompt_logprobs\":null}\r\n";
		
		// Extract raw message content from response string
		String chatMessageContent = "";
		try {
			Gson gson = new Gson();
			InferenceResponse inferenceResponse = gson.fromJson(responseAsJSONString, InferenceResponse.class);
			chatMessageContent = inferenceResponse.choices[0].message.content;
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		System.out.println("chatMessageContent: " + chatMessageContent);
		return chatMessageContent;
	}
	
	public String generateQuizFromNote(Note selectedNote) {
		// Check permission
		if (Storage.AI_API_TOKEN == null || Storage.AI_API_URL == null || Storage.AI_API_MODEL == null) {
			System.out.println("Missing AI_API_TOKEN or AI_API_URL or AI_API_MODEL.");
			System.out.println("Please check your .env file and try again.");
			return "";
		}
		
		// Create prompt based on template from text file
		String promptForAI = "";
		try {
			String noteContent = Files.readString(Path.of(selectedNote.storeFilePath), StandardCharsets.UTF_8);
			String template = Files.readString(Path.of(Storage.PROMPT_TEMPLATE_PATH), StandardCharsets.UTF_8);
			
			promptForAI = String.format(template, noteContent);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		if (promptForAI.isEmpty()) return "";
		
		// Fetch response from AI API
		String responseAsJSONString = fetchResponseFromInferenceProvider(promptForAI);
		if (responseAsJSONString.isEmpty()) return "";

		// Extract raw message content from response string
		String chatMessageContent = "";
		try {
			Gson gson = new Gson();
			InferenceResponse inferenceResponse = gson.fromJson(responseAsJSONString, InferenceResponse.class);
			chatMessageContent = inferenceResponse.choices[0].message.content;
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		return chatMessageContent;
	}
	
	public String fetchResponseFromInferenceProvider(String promptForAI) {
		Gson gson = new Gson();
		InferenceRequest info = new InferenceRequest(Storage.AI_API_MODEL, Storage.AI_API_TEMPERATURE, Storage.AI_API_TOP_P, Storage.AI_API_ROLE, promptForAI);
		String chatJSON = gson.toJson(info);
		
		String response = "";
		try {
			// Send request
			URL url = new URI(Storage.AI_API_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty ("Authorization", "Bearer " + Storage.AI_API_TOKEN);
			connection.setDoOutput(true);
			
			OutputStream os = connection.getOutputStream();
			os.write(chatJSON.getBytes());
			os.flush();
			
			// Handle response
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
				while (scanner.hasNextLine()) {
					response += scanner.nextLine() + "\n";
				}
				scanner.close();
			} else {
				System.out.println("Error: HTTP Response code - " + responseCode);
			}
			
			connection.disconnect();
		} catch (IOException | URISyntaxException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return response;
	}
}