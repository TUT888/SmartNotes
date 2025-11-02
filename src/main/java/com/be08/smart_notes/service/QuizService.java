package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.repository.QuizRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizService {
    AuthorizationService authorizationService;
    QuizRepository quizRepository;
    QuizMapper quizMapper;

    public QuizResponse saveQuizFromAIResponse(int sourceDocumentId, AIQuizResponse aiQuizResponse) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Map DTO to entity
        Quiz quiz = quizMapper.fromAIQuizResponseToQuiz(aiQuizResponse);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUserId(currentUserId);

        for (Question question : quiz.getQuestions()) {
            question.setSourceDocumentId(sourceDocumentId);
            question.setQuiz(quiz);
        }

        // Create and return saved quiz
        Quiz savedQuiz = quizRepository.save(quiz);
        return quizMapper.fromQuizToQuizResponse(savedQuiz);
    }

    public QuizResponse getQuizById(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> {
            log.error("Quiz with id {} not found", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getUserId());

        return quizMapper.fromQuizToQuizResponse(quiz);
    }

    public void deleteQuizById(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> {
            log.error("Quiz with id {} not found", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getUserId());

        quizRepository.deleteById(quizId);
    }
}
