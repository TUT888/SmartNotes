package com.be08.smart_notes.service;

import com.be08.smart_notes.common.AppConstants;
import com.be08.smart_notes.dto.QuizQuestion;
import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
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
@Transactional
public class QuizSetService {
    AuthorizationService authorizationService;
    QuizSetRepository quizSetRepository;
    QuizMapper quizMapper;

    /**
     * Create new empty QuizSet with no quizzes inside
     * @param request data packed in client's request
     * @return response dto for saved quiz set
     */
    public QuizSetResponse createQuizSet(QuizSetUpsertRequest request, OriginType originType) {
        int currentUserId = authorizationService.getCurrentUserId();

        String quizSetTitle = request.getTitle();
        QuizSet quizSet = QuizSet.builder()
                .userId(currentUserId)
                .title(quizSetTitle != null ? quizSetTitle : AppConstants.DEFAULT_QUIZ_SET_TITLE)
                .originType(originType).build();

        QuizSet savedQuizSet = quizSetRepository.save(quizSet);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    /**
     * Create new QuizSet from multiple quizzes with their associated questions
     * @param quizSetTitle a title for new quiz set, use default name if null
     * @param quizQuestionList list of quizzes to be added together with new set
     * @return response dto for saved quiz set
     */
    public QuizSetResponse createQuizSet(String quizSetTitle, List<QuizQuestion> quizQuestionList, OriginType originType) {
        int currentUserId = authorizationService.getCurrentUserId();

        List<Quiz> newQuizzes = quizMapper.toQuizList(quizQuestionList);
        QuizSet quizSet = QuizSet.builder()
                .userId(currentUserId)
                .title(quizSetTitle != null ? quizSetTitle : AppConstants.DEFAULT_QUIZ_SET_TITLE)
                .originType(originType).build();
        quizSet.addQuizzes(newQuizzes);

        QuizSet savedQuizSet = quizSetRepository.save(quizSet);
        return quizMapper.toQuizSetResponse(savedQuizSet);
    }

    /**
     * Get a QuizSet with associated quizzes using given id
     * @param quizSetId id of target quiz set
     * @return response dto for quiz set
     */
    public QuizSetResponse getQuizSetById(int quizSetId) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet quiz = quizSetRepository.findByIdAndUserId(quizSetId, currentUserId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found in user's account", quizSetId);
            return new AppException(ErrorCode.QUIZ_SET_NOT_FOUND);
        });


        return quizMapper.toQuizSetResponse(quiz);
    }

    /**
     * Get a QuizSet with associated quizzes using given id
     * @return response dto for all quiz set
     */
    public List<QuizSetResponse> getAllQuizSets() {
        int currentUserId = authorizationService.getCurrentUserId();

        List<QuizSet> quizSets = quizSetRepository.findAllByUserId(currentUserId);
        return quizMapper.toQuizSetResponseList(quizSets);
    }

    /**
     * Update existing QuizSet based on given request from client
     * @param request data packed in client's request
     * @return response dto for saved quiz set
     */
    public QuizSetResponse updateQuizSet(int quizSetId, QuizSetUpsertRequest request) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet existingQuizSet = quizSetRepository.findByIdAndUserId(quizSetId, currentUserId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found in user's account", quizSetId);
            return new AppException(ErrorCode.QUIZ_SET_NOT_FOUND);
        });

        quizMapper.updateQuizSet(existingQuizSet, request);
        existingQuizSet = quizSetRepository.save(existingQuizSet);
        return quizMapper.toQuizSetResponse(existingQuizSet);
    }

    /**
     * Delete a QuizSet using given id
     * @param quizSetId id of target quiz set
     */
    public void deleteQuizSetById(int quizSetId) {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet quiz = quizSetRepository.findByIdAndUserId(quizSetId, currentUserId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found in user's account", quizSetId);
            return new AppException(ErrorCode.QUIZ_SET_NOT_FOUND);
        });

        quizSetRepository.deleteById(quizSetId);
    }

    /**
     * Delete all QuizSet own by current user, together with its quizzes and questions
     */
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
        return quizSetRepository.findByUserIdAndOriginType(userId, OriginType.DEFAULT).orElseGet(() -> {
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
//    public QuizSet saveNewQuizToDefaultSet(int userId, Quiz quiz) {
//        QuizSet defaultSet = getOrCreateDefaultSet(userId);
//
//        defaultSet.addQuiz(quiz);
//        return quizSetRepository.save(defaultSet);
//    }

    /**
     * Create and save new QuizSet from a quiz with associated questions
     * @param userId id of current user
     * @param quizSetTitle title for new quiz set, use quiz title as alternative when null
     * @param quizQuestion the quiz with questions to be added in new quiz set
     * @return the saved QuizSet
     */
//    public QuizSet saveAsNewQuizSetEntity(int userId, String quizSetTitle, QuizQuestion quizQuestion, OriginType originType) {
//        Quiz quiz = quizMapper.toQuiz(quizQuestion);
//
//        QuizSet quizSet = QuizSet.builder()
//                .userId(userId)
//                .title(quizSetTitle != null ? quizSetTitle : quizQuestion.getTitle())
//                .originType(originType).build();
//        quizSet.addQuiz(quiz);
//
//        return quizSetRepository.save(quizSet);
//    }

    /**
     * Create and save new QuizSet from a list of quizzes with their associated questions
     * @param userId id of current user
     * @param quizSetTitle title for new quiz set, use default title as alternative when null
     * @param quizQuestionList list of quizzes with questions to be added in new quiz set
     * @return the saved QuizSet
     */
//    public QuizSet saveAsNewQuizSetEntity(int userId, String quizSetTitle, List<QuizQuestion> quizQuestionList, OriginType originType) {
//        List<Quiz> newQuizzes = quizMapper.toQuizList(quizQuestionList);
//
//        QuizSet quizSet = QuizSet.builder()
//                .userId(userId)
//                .title(quizSetTitle != null ? quizSetTitle : AppConstants.DEFAULT_QUIZ_SET_TITLE)
//                .originType(originType).build();
//        quizSet.addQuizzes(newQuizzes);
//
//        return quizSetRepository.save(quizSet);
//    }
}
