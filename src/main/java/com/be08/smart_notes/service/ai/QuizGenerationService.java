package com.be08.smart_notes.service.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import com.be08.smart_notes.common.Storage;
import com.be08.smart_notes.entity.NoteEntity;
import com.be08.smart_notes.service.NoteService;
import com.be08.smart_notes.dto.ai.GuidedInferenceRequest;
import com.be08.smart_notes.dto.ai.InferenceRequest;
import com.be08.smart_notes.dto.ai.InferenceRequestMessage;
import com.be08.smart_notes.dto.ai.InferenceResponse;
import com.be08.smart_notes.dto.ai.QuizResponse;

@Service
public class QuizGenerationService extends AIService {
	@Autowired
	private NoteService noteService;
	
	public QuizResponse generateSampleQuiz() {
		// Below is sample response of fetchResponseFromInferenceProvider()
		String responseAsJSONString = "{\"id\":\"chatcmpl-123456\",\"choices\":[{\"finish_reason\":\"stop\",\"index\":0,\"logprobs\":null,\"message\":{\"content\":\"{\\\"topic\\\": \\\"Object-Oriented Programming Concepts\\\", \\\"quizzes\\\": [\\n    {\\n        \\\"question\\\": \\\"What is the primary focus of Object-Oriented Programming?\\\",\\n        \\\"options\\\": [\\n            \\\"Writing code for specific tasks\\\",\\n            \\\"Organizing code around objects\\\",\\n            \\\"Implementing algorithms for complex problems\\\",\\n            \\\"Using pre-defined functions\\\"\\n        ],\\n        \\\"correctIndex\\\": 1\\n    },\\n    {\\n        \\\"question\\\": \\\"What is the primary benefit of encapsulation?\\\",\\n        \\\"options\\\": [\\n            \\\"Hiding internal details of an object\\\",\\n            \\\"Allowing easy modification of an object\\\",\\n            \\\"Promoting code reusability through inheritance\\\",\\n            \\\"Making code more efficient and faster\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    },\\n    {\\n        \\\"question\\\": \\\"Which of the following is an example of inheritance?\\\",\\n        \\\"options\\\": [\\n            \\\"Creating a class called 'Dog' that inherits from 'Animal'\\\",\\n            \\\"Defining a method called 'calculateArea' within a class\\\",\\n            \\\"Using a constructor to initialize an object's properties\\\",\\n            \\\"Writing code to display a message on the screen\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    },\\n    {\\n        \\\"question\\\": \\\"What does polymorphism allow?\\\",\\n        \\\"options\\\": [\\n            \\\"The same method to behave differently in different classes\\\",\\n            \\\"A single method to handle different data types\\\",\\n            \\\"Classes to inherit common functionalities from their parent classes\\\",\\n            \\\"Objects to access data and methods in a controlled manner\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    },\\n    {\\n        \\\"question\\\": \\\"What is a constructor in OOP?\\\",\\n        \\\"options\\\": [\\n            \\\"A method that runs when an object is created\\\",\\n            \\\"A method that defines the behavior of an object\\\",\\n            \\\"A method that is called repeatedly for a specific task\\\",\\n            \\\"A method that handles exceptions and errors\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    },\\n    {\\n        \\\"question\\\": \\\"What is the role of an interface in OOP?\\\",\\n        \\\"options\\\": [\\n            \\\"A contract that defines a set of methods that classes must implement\\\",\\n            \\\"A blueprint for creating a specific type of object\\\",\\n            \\\"A way to communicate between different objects\\\",\\n            \\\"A way to store and manage data\\\"\\n        ],\\n        \\\"correctIndex\\\": 0\\n    }\\n]}\",\"refusal\":null,\"role\":\"assistant\",\"audio\":null,\"function_call\":null,\"tool_calls\":[],\"reasoning_content\":null},\"stop_reason\":null}],\"created\":1751445406,\"model\":\"google/gemma-2-2b-it\",\"object\":\"chat.completion\",\"service_tier\":null,\"system_fingerprint\":null,\"usage\":{\"completion_tokens\":531,\"prompt_tokens\":953,\"total_tokens\":1484,\"completion_tokens_details\":null,\"prompt_tokens_details\":null},\"prompt_logprobs\":null}\r\n";
		
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
	
	public QuizResponse generateQuizFromNote(long noteId) {
		checkPermission();
		
		NoteEntity selectedNote = noteService.getNote(noteId);
		if (selectedNote == null) return null;
		
		// Prepare JSON Body
		String systemPrompt = "";
		String noteContent = "";
		String guidedSchema = "";
		try {
			noteContent = selectedNote.getContent();
			noteContent = "Just generate some random quizzes";
			systemPrompt = Files.readString(Path.of(Storage.SYSTEM_PROMPT_TEMPLATE_PATH), StandardCharsets.UTF_8);
			guidedSchema = Files.readString(Path.of(Storage.QUIZ_RESPONSE_SCHEMA_PATH), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		if (systemPrompt.isEmpty() || noteContent.isEmpty() || guidedSchema.isEmpty()) return null;

		// Create inference request
		Gson gson = new Gson();
		InferenceRequestMessage systemMessage = new InferenceRequestMessage(Storage.AI_API_SYSTEM_ROLE, systemPrompt);
		InferenceRequestMessage userMessage = new InferenceRequestMessage(Storage.AI_API_USER_ROLE, noteContent);
		InferenceRequest info = new GuidedInferenceRequest(
				Storage.AI_API_MODEL, new InferenceRequestMessage[] {systemMessage, userMessage},
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
