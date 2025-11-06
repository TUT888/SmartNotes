package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.QuizDTO;
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

    public QuizResponse createQuiz(QuizDTO quizDTO) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet defaultSet = quizSetService.getOrCreateDefaultSet(currentUserId);
        Quiz newQuiz = quizMapper.toQuiz(quizDTO);
        newQuiz.setQuizSet(defaultSet);

        Quiz savedQuiz = quizRepository.save(newQuiz);
        return quizMapper.toQuizResponse(savedQuiz);
    }

    public QuizResponse getQuizById(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz quiz = quizRepository.findByIdAndQuizSetUserId(quizId, currentUserId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        return quizMapper.toQuizResponse(quiz);
    }

    public QuizResponse updateQuiz(int quizId, QuizDTO quizDTO) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz existingQuiz = quizRepository.findByIdAndQuizSetUserId(quizId, currentUserId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        if (quizDTO.getQuizSetId() != null) {
            QuizSet quizSet = quizSetService.getQuizSetEntityById(quizDTO.getQuizSetId());
            existingQuiz.setQuizSet(quizSet);
        }

        quizMapper.updateQuiz(existingQuiz, quizDTO);
        existingQuiz = quizRepository.save(existingQuiz);
        return quizMapper.toQuizResponse(existingQuiz);
    }

    public void deleteQuizById(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz quiz = quizRepository.findByIdAndQuizSetUserId(quizId, currentUserId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        quizRepository.deleteById(quizId);
    }

    // ------ Methods that returns entities ------ //
//    public Quiz saveAsNewQuizEntity(int userId, QuizDTO quizDTO) {
//        QuizSet defaultSet = quizSetService.getOrCreateDefaultSet(userId);
//
//        Quiz newQuiz = quizMapper.toQuiz(quizDTO);
//        newQuiz.setQuizSet(defaultSet);
//
//        return quizRepository.save(newQuiz);
//    }
}
