package com.be08.smart_notes.service.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.common.AppConstants;
import com.be08.smart_notes.service.NoteService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizGenerationService {
    static int DEFAULT_TOTAL_QUESTIONS = 10;
    String systemPrompt;
    String quizResponseSchema;

    AIService aiService;
    NoteService noteService;
    QuizService quizService;
    QuizMapper quizMapper;

    public QuizGenerationService(AIService aiService, NoteService noteService, QuizService quizService, QuizMapper quizMapper) {
        this.aiService = aiService;
        this.noteService = noteService;
        this.quizService = quizService;
        this.quizMapper = quizMapper;

        String prompt = null;
        String schema = null;
        try {
            prompt = Files.readString(
                    Path.of(AppConstants.SYSTEM_PROMPT_TEMPLATE_PATH),
                    StandardCharsets.UTF_8
            );
            schema = Files.readString(
                    Path.of(AppConstants.QUIZ_RESPONSE_SCHEMA_PATH),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.error("Load resource for quiz generation failed with error: {}", e.getMessage());
        }

        systemPrompt = prompt;
        quizResponseSchema = schema;
    }

	public QuizResponse generateSampleQuiz() {
        // Below is sample generated content
		String generatedContent = "{\"topic\":\"Object-Oriented Programming Concepts\",\"questions\":[{\"question\":\"What is the main purpose of Object-Oriented Programming (OOP)?\",\"options\":[\"A. To simplify data structures\",\"B. To organize code around objects\",\"C. To create complex algorithms\",\"D. To optimize code execution speed\"], \"correct_index\": 1}, {\"question\":\"Which of the following best describes encapsulation in OOP?\",\"options\":[\"A. Hiding internal data and exposing only necessary information\",\"B. Creating multiple objects from a single class\",\"C. Passing data between different classes\",\"D. Defining the structure of a class\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the primary function of a constructor in OOP?\",\"options\":[\"A. To delete an object from memory\",\"B. To store data for an object\",\"C. To initialize an object when it is created\",\"D. To define the behavior of an object\",\"\"], \"correct_index\": 3}, {\"question\":\"How does inheritance work in OOP?\",\"options\":[\"A. It allows objects to inherit properties and methods from other objects\",\"B. It creates a new class based on an existing one and adds new features\",\"C. It allows objects to access private members of other objects\",\"D. It enables objects to communicate with each other through messages\",\"\"], \"correct_index\": 1}, {\"question\":\"What does polymorphism refer to in OOP?\",\"options\":[\"A. The ability of an object to be accessed from multiple classes\",\"B. The ability of an object to behave differently based on its context\",\"C. The ability of an object to be used in different programming languages\",\"D. The ability of an object to be inherited from other objects\",\"\"], \"correct_index\": 1}, {\"question\":\"Which of the following is NOT a benefit of OOP?\",\"options\":[\"A. Improved code reusability\",\"B. Easier code maintenance\",\"C. Increased program complexity\",\"D. Enhanced code readability\",\"\"], \"correct_index\": 3}, {\"question\":\"What is the primary difference between a class and an object?\",\"options\":[\"A. A class is a blueprint for creating objects, while an object is an instance of that blueprint\",\"B. A class is a data structure, while an object is a programming language\",\"C. A class is a variable, while an object is a function\",\"D. A class is a method, while an object is a program\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the main purpose of a static method?\",\"options\":[\"A. To define a method that is specific to a particular object\",\"B. To define a method that belongs to a class and not to individual objects\",\"C. To define a method that is called when an object is created\",\"D. To define a method that is called when an object is destroyed\",\"\"], \"correct_index\": 1}, {\"question\":\"Which of the following is an example of a common mistake to avoid in OOP?\",\"options\":[\"A. Using inheritance when it is not needed\",\"B. Using public access modifiers for every method\",\"C. Using static methods for every method\",\"D. Creating complex objects that are not needed\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the purpose of an interface in OOP?\",\"options\":[\"A. To define the behavior of a class\",\"B. To create a contract that classes must follow\",\"C. To define the structure of a class\",\"D. To create a blueprint for creating objects\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the purpose of a method overriding?\",\"options\":[\"A. To create a new class that is based on an existing one\",\"B. To define a new method with a different implementation in a child class\",\"C. To create a new method that overrides the behavior of a parent class\",\"D. To create a new class that inherits from a different class\",\"\"], \"correct_index\": 3}]}\n";

		// Extract raw message content from response string
        ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Extract raw message content from response string
            AIQuizResponse aiQuizResponse = objectMapper.readValue(generatedContent, AIQuizResponse.class);

            Quiz sampleQuizEntity = quizMapper.fromAIQuizResponseToQuiz(aiQuizResponse);
            return quizMapper.fromQuizToQuizResponse(sampleQuizEntity);
		} catch (Exception e) {
            log.error("An error occurred when mapping objects, could not create sample quiz.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
		}
	}

	public QuizResponse generateQuizFromSingleNote(int noteId) {
        if (this.systemPrompt == null || this.quizResponseSchema == null) {
            log.error("Could not generate quiz because of invalid system prompt and/or guided schema.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

		// Get note
		NoteResponse selectedNote = noteService.getNote(noteId);

		// Generate content
        String generatedContent = aiService.generateContent(
                String.format(this.systemPrompt, DEFAULT_TOTAL_QUESTIONS),
                selectedNote.getContent(),
                quizResponseSchema
        );
		if (generatedContent == null || generatedContent.isEmpty()) {
            log.error("Could not create quiz because generated content is empty.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Map generated content (JSON String) to Object
        ObjectMapper objectMapper = new ObjectMapper();
        AIQuizResponse aiQuizResponse = null;
        try {
            aiQuizResponse = objectMapper.readValue(generatedContent, AIQuizResponse.class);
        } catch (Exception e) {
            log.error("Could not map generated content to AIQuizResponse.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }
        if (aiQuizResponse  == null) {
            log.error("Could not generate quiz because of invalid object mapping result.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Save and return quiz from mapped object
        return quizService.saveQuizFromAIResponse(noteId, aiQuizResponse);
    }

    public QuizResponse generateQuizFromListOfNotes(List<Integer> noteIds) {
        if (this.systemPrompt == null) {
            return null;
        }

        return null;
        // Get note
//        List<Document> noteList = noteService.getAllNotesByIds(noteIds);
//        if (noteList.isEmpty()) {
//            return null;
//        }
//
//        String systemPrompt = String.format(this.systemPrompt, DEFAULT_TOTAL_QUESTIONS);
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (Document note : noteList) {
//            String generatedContent = aiService.generateContent(
//                    systemPrompt,
//                    note.getContent(),
//                    quizResponseSchema
//            );
//            if (generatedContent == null || generatedContent.isEmpty()) {
//                return null;
//            }
//
//            try {
//                AIQuizResponse quizResponse = objectMapper.readValue(generatedContent, AIQuizResponse.class);
//                quizService.createQuiz(userId, note.getId(), quizResponse);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        AIQuizResponse quizResponse =
//        return noteList;
    }
}
