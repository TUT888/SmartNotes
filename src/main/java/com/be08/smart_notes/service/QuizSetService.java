package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import com.be08.smart_notes.repository.QuizSetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizSetService {
    static String DEFAULT_SET_TITLE = "Unsorted Quizzes";

    AuthorizationService authorizationService;
    QuizSetRepository quizSetRepository;
    QuizMapper quizMapper;

    /**
     * Save QuizSet including single Quiz with associated questions from AI Quiz Response
     * @param aiQuizResponse AIQuizResponse
     * @return QuizSetResponse
     */
    public QuizSetResponse saveQuizSetFromAIResponse(Integer sourceDocumentId, AIQuizResponse aiQuizResponse) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Map DTO to Quiz entity
        Quiz quiz = quizMapper.toQuiz(aiQuizResponse);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setSourceDocumentId(sourceDocumentId);

        // Create new QuizSet and add new quiz
        QuizSet quizSet = QuizSet.builder()
                .userId(currentUserId)
                .title(aiQuizResponse.getTitle())
                .build();
        quizSet.addQuiz(quiz);

        // Create and return saved quiz
        QuizSet savedQuizSet = quizSetRepository.save(quizSet);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

//    public QuizSet getOrCreateDefaultSet() {
//        // Get current user id
//        int currentUserId = authorizationService.getCurrentUserId();
//
//        return quizSetRepository.findByTitleAndUserId(DEFAULT_SET_TITLE, currentUserId).orElseGet(() -> {
//            QuizSet quizSet = QuizSet.builder()
//                    .userId(currentUserId)
//                    .title(DEFAULT_SET_TITLE)
//                    .build();
//            return quizSetRepository.save(quizSet);
//        });
//    }

    public QuizSetResponse getQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getUserId());

        return quizMapper.toQuizSetResponse(quiz);
    }

    public void deleteQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getUserId());

        quizSetRepository.deleteById(quizSetId);
    }
}
