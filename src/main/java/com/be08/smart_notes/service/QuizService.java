package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.QuizQuestion;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import com.be08.smart_notes.repository.QuizRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizService {
    AuthorizationService authorizationService;
    QuizSetService quizSetService;
    QuizRepository quizRepository;
    QuizMapper quizMapper;

    public QuizResponse saveQuiz(QuizQuestion quizQuestion) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz savedQuiz = saveAsNewQuizEntity(currentUserId, quizQuestion);
        return quizMapper.toQuizResponse(savedQuiz);
    }

    public QuizResponse getQuizById(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> {
            log.error("Quiz with id {} not found", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getQuizSet().getUserId());

        return quizMapper.toQuizResponse(quiz);
    }

    public void deleteQuizById(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> {
            log.error("Quiz with id {} not found", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(quiz.getQuizSet().getUserId());

        quizRepository.deleteById(quizId);
    }

    // ------ Methods that returns entities ------ //
    public Quiz saveAsNewQuizEntity(int userId, QuizQuestion quizQuestion) {
        QuizSet defaultSet = quizSetService.getOrCreateDefaultSet(userId);

        Quiz newQuiz = quizMapper.toQuiz(quizQuestion);
        newQuiz.setQuizSet(defaultSet);

        return quizRepository.save(newQuiz);
    }
}
