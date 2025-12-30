package com.be08.smart_notes.service.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.request.QuizGenerationRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.service.AuthorizationService;
import com.be08.smart_notes.service.QuizService;
import com.be08.smart_notes.service.QuizSetService;
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
    String systemPrompt;
    String quizResponseSchema;

    AIService aiService;
    NoteService noteService;
    QuizService quizService;
    QuizSetService quizSetService;
    AuthorizationService authorizationService;
    QuizMapper quizMapper;

    public QuizGenerationService(AIService aiService, NoteService noteService, QuizService quizService, QuizSetService quizSetService, QuizMapper quizMapper, AuthorizationService authorizationService) {
        this.aiService = aiService;
        this.noteService = noteService;
        this.quizService = quizService;
        this.quizSetService = quizSetService;
        this.quizMapper = quizMapper;
        this.authorizationService = authorizationService;

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

    /**
     * A sample function to retrieve a quiz for testing purposes, it will neither interact with the AI provider nor save data to the database.
     * @return quiz response dto
     */
	public QuizResponse generateSampleQuiz() {
        // Below is sample generated content
		String generatedContent = "{\"topic\":\"Object-Oriented Programming Concepts\",\"questions\":[{\"question\":\"What is the main purpose of Object-Oriented Programming (OOP)?\",\"options\":[\"A. To simplify data structures\",\"B. To organize code around objects\",\"C. To create complex algorithms\",\"D. To optimize code execution speed\"], \"correct_index\": 1}, {\"question\":\"Which of the following best describes encapsulation in OOP?\",\"options\":[\"A. Hiding internal data and exposing only necessary information\",\"B. Creating multiple objects from a single class\",\"C. Passing data between different classes\",\"D. Defining the structure of a class\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the primary function of a constructor in OOP?\",\"options\":[\"A. To delete an object from memory\",\"B. To store data for an object\",\"C. To initialize an object when it is created\",\"D. To define the behavior of an object\",\"\"], \"correct_index\": 3}, {\"question\":\"How does inheritance work in OOP?\",\"options\":[\"A. It allows objects to inherit properties and methods from other objects\",\"B. It creates a new class based on an existing one and adds new features\",\"C. It allows objects to access private members of other objects\",\"D. It enables objects to communicate with each other through messages\",\"\"], \"correct_index\": 1}, {\"question\":\"What does polymorphism refer to in OOP?\",\"options\":[\"A. The ability of an object to be accessed from multiple classes\",\"B. The ability of an object to behave differently based on its context\",\"C. The ability of an object to be used in different programming languages\",\"D. The ability of an object to be inherited from other objects\",\"\"], \"correct_index\": 1}, {\"question\":\"Which of the following is NOT a benefit of OOP?\",\"options\":[\"A. Improved code reusability\",\"B. Easier code maintenance\",\"C. Increased program complexity\",\"D. Enhanced code readability\",\"\"], \"correct_index\": 3}, {\"question\":\"What is the primary difference between a class and an object?\",\"options\":[\"A. A class is a blueprint for creating objects, while an object is an instance of that blueprint\",\"B. A class is a data structure, while an object is a programming language\",\"C. A class is a variable, while an object is a function\",\"D. A class is a method, while an object is a program\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the main purpose of a static method?\",\"options\":[\"A. To define a method that is specific to a particular object\",\"B. To define a method that belongs to a class and not to individual objects\",\"C. To define a method that is called when an object is created\",\"D. To define a method that is called when an object is destroyed\",\"\"], \"correct_index\": 1}, {\"question\":\"Which of the following is an example of a common mistake to avoid in OOP?\",\"options\":[\"A. Using inheritance when it is not needed\",\"B. Using public access modifiers for every method\",\"C. Using static methods for every method\",\"D. Creating complex objects that are not needed\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the purpose of an interface in OOP?\",\"options\":[\"A. To define the behavior of a class\",\"B. To create a contract that classes must follow\",\"C. To define the structure of a class\",\"D. To create a blueprint for creating objects\",\"\"], \"correct_index\": 1}, {\"question\":\"What is the purpose of a method overriding?\",\"options\":[\"A. To create a new class that is based on an existing one\",\"B. To define a new method with a different implementation in a child class\",\"C. To create a new method that overrides the behavior of a parent class\",\"D. To create a new class that inherits from a different class\",\"\"], \"correct_index\": 3}]}\n";

		// Extract raw message content from response string
        ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Extract raw message content from response string
            QuizUpsertDTO quizUpsertDTO = objectMapper.readValue(generatedContent, QuizUpsertDTO.class);

            Quiz sampleQuizEntity = quizMapper.toQuiz(quizUpsertDTO);
            return quizMapper.toQuizResponse(sampleQuizEntity);
		} catch (Exception e) {
            log.error("An error occurred when mapping objects, could not create sample quiz.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
		}
	}

    /**
     * Generate a quiz based on a single note using AI Inference API.
     * The generated quiz will be listed in user's DEFAULT set.
     * @param quizGenerationRequest a request dto, should include a document id (docId) inside the request
     * @return quiz response dto, generated by AI
     */
    public QuizResponse generateQuiz(QuizGenerationRequest quizGenerationRequest) {
        if (this.systemPrompt == null || this.quizResponseSchema == null) {
            log.error("Could not generate quiz because of invalid system prompt and/or guided schema.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Extract required data from request
        int totalQuestions = quizGenerationRequest.getSizeOfEachQuiz();
        String prompt = String.format(this.systemPrompt, totalQuestions);
        int noteId = quizGenerationRequest.getDocId();

        NoteResponse selectedNote = noteService.getNote(noteId);
        QuizUpsertDTO quizUpsertDTO = generateQuizFromNote(selectedNote.getContent(), prompt);

        quizUpsertDTO.setSourceDocumentId(noteId);
        return quizService.createQuiz(quizUpsertDTO);
    }

    /**
     * Generate a quiz based on multiple notes using AI Inference API.
     * The generated quizzes will be grouped in new quiz set with default title.
     * The method process the document(s) one by one, document(s) that are not owned by user will be ignored and skipped.
     * @param quizGenerationRequest a request dto, should include list of document ids (docIds) inside the request
     * @return quiz response dto, generated by AI
     */
    public QuizSetResponse generateQuizSet(QuizGenerationRequest quizGenerationRequest) {
        if (this.systemPrompt == null || this.quizResponseSchema == null) {
            log.error("Could not generate quiz because of invalid system prompt and/or guided schema.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Extract required data from request
        int totalQuestions = quizGenerationRequest.getSizeOfEachQuiz();
        String prompt = String.format(this.systemPrompt, totalQuestions);
        List<Integer> noteIds = quizGenerationRequest.getDocIds();

        // Save quizzes from list of notes (generate one by one)
        List<Document> noteList = noteService.getAllNotesByIds(noteIds);
        List<QuizUpsertDTO> quizUpsertDTOList = new ArrayList<>();
        for (Document note : noteList) {
            try {
                authorizationService.validateOwnership(note.getUserId());

                QuizUpsertDTO quizUpsertDTO = generateQuizFromNote(note.getContent(), prompt);
                quizUpsertDTO.setSourceDocumentId(note.getId());
                quizUpsertDTOList.add(quizUpsertDTO);
            } catch (Exception e) {
                System.out.println("Error in generating quiz: " + e.getMessage());
            }
        }

        if (quizUpsertDTOList.isEmpty()) {
            log.error("Quiz set was not created because no quizzes are generated.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }
        return quizSetService.createQuizSet(AppConstants.DEFAULT_QUIZ_SET_TITLE, quizUpsertDTOList, OriginType.AI);
    }

    // ------ Internal methods ------ //
    /**
     * Internal method that interact with AIService and process response for quiz generation purpose
     * @param noteContent the content to be sent for generating quiz
     * @param prompt the system prompt to guide the AI
     * @return a quiz dto that can be used to create new quiz
     */
    private QuizUpsertDTO generateQuizFromNote(String noteContent, String prompt) {
        String generatedContent = aiService.generateContent(
                prompt,
                noteContent,
                quizResponseSchema
        );
        if (generatedContent == null || generatedContent.isEmpty()) {
            log.error("Could not process quiz because generated content is empty.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Map generated content (JSON String) to Object
        ObjectMapper objectMapper = new ObjectMapper();
        QuizUpsertDTO quizUpsertDTO = null;
        try {
            quizUpsertDTO = objectMapper.readValue(generatedContent, QuizUpsertDTO.class);
        } catch (Exception e) {
            log.error("Could not map generated content to QuizUpsertDTO.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }
        if (quizUpsertDTO == null) {
            log.error("Could not generate quiz because of invalid object mapping result.");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }
        return quizUpsertDTO;
    }
}
