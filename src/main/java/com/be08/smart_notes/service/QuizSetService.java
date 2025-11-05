package com.be08.smart_notes.service;

import com.be08.smart_notes.common.AppConstants;
import com.be08.smart_notes.dto.QuizQuestion;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.enums.OriginType;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizSetService {
    AuthorizationService authorizationService;
    QuizSetRepository quizSetRepository;
    QuizMapper quizMapper;

    /**
     * Save new QuizSet from a single quiz with associated questions
     * @param quizSetTitle the title for new quiz set, use quiz's title if null
     * @param quizQuestion a quiz with its questions
     * @return response dto for saved quiz set
     */
    public QuizSetResponse saveQuizSet(String quizSetTitle, QuizQuestion quizQuestion) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet savedQuizSet = saveAsNewQuizSetEntity(currentUserId, quizSetTitle, quizQuestion);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    /**
     * Save new QuizSet from multiple quizzes with their associated questions
     * @param quizSetTitle a title for new quiz set, use default name if null
     * @param quizQuestionList list of quizzes to be added together with new set
     * @return response dto for saved quiz set
     */
    public QuizSetResponse saveQuizSet(String quizSetTitle, List<QuizQuestion> quizQuestionList) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet savedQuizSet = saveAsNewQuizSetEntity(currentUserId, quizSetTitle, quizQuestionList);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    /**
     * Get a QuizSet with associated quizzes using given id
     * @param quizSetId id of target quiz set
     * @return response dto for quiz set
     */
    public QuizSetResponse getQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        authorizationService.validateOwnership(quiz.getUserId());

        return quizMapper.toQuizSetResponse(quiz);
    }

    /**
     * Delete a QuizSet using given id
     * @param quizSetId id of target quiz set
     */
    public void deleteQuizSetById(int quizSetId) {
        QuizSet quiz = quizSetRepository.findById(quizSetId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found", quizSetId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        authorizationService.validateOwnership(quiz.getUserId());

        quizSetRepository.deleteById(quizSetId);
    }

    /**
     * Delete all QuizSet own by current user, together with its quizzes and questions
     */
    @Transactional
    public void deleteAllQuizSet() {
        int currentUserId = authorizationService.getCurrentUserId();
        quizSetRepository.deleteAllByUserId(currentUserId);
    }

    // ------ Methods that returns entities ------ //
    /**
     * Get or create a default set if not exist for current user. Default set should only unique for each user
     * @param userId id of current user
     * @return default quiz set
     */
    public QuizSet getOrCreateDefaultSet(int userId) {
        return quizSetRepository.findByUserIDAndOriginType(userId, OriginType.DEFAULT).orElseGet(() -> {
            QuizSet defaultSet = QuizSet.builder()
                    .userId(userId)
                    .title(AppConstants.DEFAULT_QUIZ_SET_TITLE)
                    .originType(OriginType.DEFAULT).build();
            return quizSetRepository.save(defaultSet);
        });
    }

    /**
     * Save new quiz to default quiz set (uncategorized)
     * @param userId id of current user
     * @param quiz new quiz to be added
     * @return default quiz set with new quiz added
     */
    public QuizSet saveNewQuizToDefaultSet(int userId, Quiz quiz) {
        QuizSet defaultSet = getOrCreateDefaultSet(userId);

        defaultSet.addQuiz(quiz);
        return quizSetRepository.save(defaultSet);
    }

    /**
     * Create and save new QuizSet from a quiz with associated questions
     * @param userId id of current user
     * @param quizSetTitle title for new quiz set, use quiz title as alternative when null
     * @param quizQuestion the quiz with questions to be added in new quiz set
     * @return the saved QuizSet
     */
    public QuizSet saveAsNewQuizSetEntity(int userId, String quizSetTitle, QuizQuestion quizQuestion) {
        Quiz quiz = quizMapper.toQuiz(quizQuestion);

        QuizSet quizSet = QuizSet.builder()
                .userId(userId)
                .title(quizSetTitle != null ? quizSetTitle : quizQuestion.getTitle())
                .build();
        quizSet.addQuiz(quiz);

        return quizSetRepository.save(quizSet);
    }

    /**
     * Create and save new QuizSet from a list of quizzes with their associated questions
     * @param userId id of current user
     * @param quizSetTitle title for new quiz set, use default title as alternative when null
     * @param quizQuestionList list of quizzes with questions to be added in new quiz set
     * @return the saved QuizSet
     */
    public QuizSet saveAsNewQuizSetEntity(int userId, String quizSetTitle, List<QuizQuestion> quizQuestionList) {
        List<Quiz> newQuizzes = quizMapper.toQuizList(quizQuestionList);

        QuizSet quizSet = QuizSet.builder()
                .userId(userId)
                .title(quizSetTitle != null ? quizSetTitle : AppConstants.DEFAULT_QUIZ_SET_TITLE)
                .build();
        quizSet.addQuizzes(newQuizzes);

        return quizSetRepository.save(quizSet);
    }
}
