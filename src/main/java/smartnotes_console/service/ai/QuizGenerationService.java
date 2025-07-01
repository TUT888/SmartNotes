package smartnotes_console.service.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

import smartnotes_console.common.Storage;
import smartnotes_console.dto.Note;
import smartnotes_console.dto.ai_api_request.GuidedInferenceRequest;
import smartnotes_console.dto.ai_api_request.InferenceRequest;
import smartnotes_console.dto.ai_api_request.InferenceRequestMessage;
import smartnotes_console.dto.ai_api_response.QuizResponse;
import smartnotes_console.dto.ai_api_response.InferenceResponse;

public class QuizGenerationService extends AIService {
	public QuizResponse generateSampleQuiz() {
		// Below is sample response
		String responseAsJSONString = "{\"id\":\"chatcmpl-5e257ec09e5e4bfe834684f6b4799140\",\"choices\":[{\"finish_reason\":\"stop\",\"index\":0,\"logprobs\":null,\"message\":{\"content\":\"{\\\"topic\\\": \\\"Time and Space Complexity\\\", \\\"quizzes\\\": [\\n    {\\n        \\\"question\\\": \\\"What does Time Complexity measure?\\\",\\n        \\\"options\\\": [\\n            \\\"The amount of memory an algorithm uses\\\",\\n            \\\"How the runtime of an algorithm increases with input size\\\",\\n            \\\"The number of times an algorithm repeats\\\",\\n            \\\"The type of data an algorithm processes\\\"\\n        ],\\n        \\\"correctIndex\\\": 1\\n    },\\n    {\\n        \\\"question\\\": \\\"Which of the following time complexities represents a linear algorithm?\\\",\\n        \\\"options\\\": [\\n            \\\"O(n log n)\\\",\\n            \\\"O(n^2)\\\",\\n            \\\"O(1)\\\",\\n            \\\"O(2^n)\\\"\\n        ],\\n        \\\"correctIndex\\\": 2\\n    },\\n    {\\n        \\\"question\\\": \\\"Which of the following is an example of a constant time complexity?\\\",\\n        \\\"options\\\": [\\n            \\\"Accessing an array element by its index\\\",\\n            \\\"Binary Search\\\",\\n            \\\"Finding a number in a sorted array\\\",\\n            \\\"Sorting an array using bubble sort\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    },\\n    {\\n        \\\"question\\\": \\\"What is the primary purpose of analyzing Space Complexity?\\\",\\n        \\\"options\\\": [\\n            \\\"Determining the efficiency of a recursive function\\\",\\n            \\\"Evaluating the performance of a data structure\\\",\\n            \\\"Understanding how much memory an algorithm consumes as input size grows\\\",\\n            \\\"Identifying the optimal data structures for a given problem\\\"\\n        ],\\n        \\\"correctIndex\\\": 3\\n    }\\n]}\",\"refusal\":null,\"role\":\"assistant\",\"audio\":null,\"function_call\":null,\"tool_calls\":[],\"reasoning_content\":null},\"stop_reason\":null}],\"created\":1234567890,\"model\":\"google/gemma-2-2b-it\",\"object\":\"chat.completion\",\"service_tier\":null,\"system_fingerprint\":null,\"usage\":{\"completion_tokens\":548,\"prompt_tokens\":916,\"total_tokens\":1464,\"completion_tokens_details\":null,\"prompt_tokens_details\":null},\"prompt_logprobs\":null}\r\n";
		
		// Extract raw message content from response string
		Gson gson = new Gson();
		QuizResponse quizResponse = null;
		try {
			// Extract raw message content from response string
			InferenceResponse inferenceResponse = gson.fromJson(responseAsJSONString, InferenceResponse.class);
			String chatMessageContent = inferenceResponse.choices[0].message.content;
			
			// Parse to object
			quizResponse = gson.fromJson(chatMessageContent, QuizResponse.class);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		return quizResponse;
	}
	
	public QuizResponse generateQuizFromNote(Note selectedNote) {
		checkPermission();

		// Prepare JSON Body
		String promptForAI = "";
		String guidedSchema = "";
		try {
			String noteContent = Files.readString(Path.of(selectedNote.storeFilePath), StandardCharsets.UTF_8);
			String template = Files.readString(Path.of(Storage.PROMPT_TEMPLATE_PATH), StandardCharsets.UTF_8);	
			promptForAI = String.format(template, noteContent);
			
			guidedSchema = Files.readString(Path.of(Storage.QUIZ_RESPONSE_SCHEMA_PATH), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		if (promptForAI.isEmpty() || guidedSchema.isEmpty()) return null;

		// Create inference request
		Gson gson = new Gson();
		InferenceRequestMessage inferenceRequestMessage = new InferenceRequestMessage(Storage.AI_API_ROLE, promptForAI);
		InferenceRequest info = new GuidedInferenceRequest(
				Storage.AI_API_MODEL, new InferenceRequestMessage[] {inferenceRequestMessage},
				Storage.AI_API_TEMPERATURE, Storage.AI_API_TOP_P, guidedSchema);
		String chatJSON = gson.toJson(info);
		
		// Fetch response from AI API
		String responseAsJSONString = fetchResponseFromInferenceProvider(chatJSON);
		if (responseAsJSONString.isEmpty()) return null;

		QuizResponse quizResponse = null;
		try {
			// Extract raw message content from response string
			InferenceResponse inferenceResponse = gson.fromJson(responseAsJSONString, InferenceResponse.class);
			String chatMessageContent = inferenceResponse.choices[0].message.content;
			
			// Parse to object
			quizResponse = gson.fromJson(chatMessageContent, QuizResponse.class);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		return quizResponse;
	}
}