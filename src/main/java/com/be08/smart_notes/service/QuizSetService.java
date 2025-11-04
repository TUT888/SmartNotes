package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.QuizQuestion;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizSetService {
    public static String DEFAULT_QUIZ_SET_TITLE = "Untitled Quiz Set";

    AuthorizationService authorizationService;
    QuizSetRepository quizSetRepository;
    QuizMapper quizMapper;

    /**
     * Save QuizSet including single Quiz with associated questions from AI Quiz Response
     * @param quizQuestion QuizQuestion
     * @return QuizSetResponse
     */
    public QuizSetResponse saveQuizSet(QuizQuestion quizQuestion) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet savedQuizSet = saveQuizSetEntity(currentUserId, quizQuestion);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    public QuizSetResponse saveQuizSet(String quizSetTitle, List<QuizQuestion> quizQuestionList) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet savedQuizSet = saveQuizSetEntity(currentUserId, quizSetTitle, quizQuestionList);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    public QuizSetResponse getQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        authorizationService.validateOwnership(quiz.getUserId());

        return quizMapper.toQuizSetResponse(quiz);
    }

    public void deleteQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        authorizationService.validateOwnership(quiz.getUserId());

        quizSetRepository.deleteById(quizSetId);
    }

    @Transactional
    public void deleteAllQuizSet() {
        int currentUserId = authorizationService.getCurrentUserId();
        quizSetRepository.deleteAllByUserId(currentUserId);
    }

    // --- Internal methods --- //
    private QuizSet saveQuizSetEntity(int userId, QuizQuestion quizQuestion) {
        Quiz quiz = quizMapper.toQuiz(quizQuestion);

        QuizSet quizSet = QuizSet.builder()
                .userId(userId)
                .title(quizQuestion.getTitle())
                .build();
        quizSet.addQuiz(quiz);

        return quizSetRepository.save(quizSet);
    }

    private QuizSet saveQuizSetEntity(int userId, String quizSetTitle, List<QuizQuestion> quizQuestionList) {
        List<Quiz> newQuizzes = quizMapper.toQuizList(quizQuestionList);

        QuizSet quizSet = QuizSet.builder()
                .userId(userId)
                .title(quizSetTitle)
                .build();
        quizSet.addQuizzes(newQuizzes);

        return quizSetRepository.save(quizSet);
    }
}
