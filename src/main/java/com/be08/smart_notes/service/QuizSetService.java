package com.be08.smart_notes.service;

import com.be08.smart_notes.common.AppConstants;
import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.mapper.QuizSetMapper;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import com.be08.smart_notes.repository.QuizSetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    QuizSetMapper quizSetMapper;

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
        return quizSetMapper.toQuizSetResponse(savedQuizSet);
    }

    /**
     * Create new QuizSet from multiple quizzes with their associated questions
     * @param quizSetTitle a title for new quiz set, use default name if null
     * @param quizUpsertDTOList list of quizzes to be added together with new set
     * @return response dto for saved quiz set
     */
    public QuizSetResponse createQuizSet(String quizSetTitle, List<QuizUpsertDTO> quizUpsertDTOList, OriginType originType) {
        int currentUserId = authorizationService.getCurrentUserId();

        List<Quiz> newQuizzes = quizMapper.toQuizList(quizUpsertDTOList);
        QuizSet quizSet = QuizSet.builder()
                .userId(currentUserId)
                .title(quizSetTitle != null ? quizSetTitle : AppConstants.DEFAULT_QUIZ_SET_TITLE)
                .originType(originType).build();
        quizSet.addQuizzes(newQuizzes);

        QuizSet savedQuizSet = quizSetRepository.save(quizSet);
        return quizSetMapper.toQuizSetResponse(savedQuizSet);
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

        return quizSetMapper.toQuizSetResponse(quiz);
    }

    /**
     * Get a QuizSet with associated quizzes using given id
     * @return response dto for all quiz set
     */
    public PageResponse<QuizSetResponse> getAllQuizSets(int pageNumber, int pageSize) {
        int currentUserId = authorizationService.getCurrentUserId();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<QuizSet> page = quizSetRepository.findAllByUserId(currentUserId, pageable);

        List<QuizSetResponse> quizSetResponses = page.stream().map(quizSetMapper::toQuizSetResponse).toList();

        return PageResponse.<QuizSetResponse>builder()
                .pageInfo(PageResponse.PageInfo.builder()
                        .currentPage(pageNumber)
                        .pageSize(pageSize)
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements()).build())
                .pageData(quizSetResponses).build();
    }

    /**
     * Get default QuizSet with associated quizzes
     * @return response dto for default quiz set
     */
    public QuizSetResponse getDefaultSet() {
        int currentUserId = authorizationService.getCurrentUserId();

        QuizSet quizSet = getOrCreateDefaultSet(currentUserId);
        return quizSetMapper.toQuizSetResponse(quizSet);
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

        quizSetMapper.updateQuizSet(existingQuizSet, request);
        existingQuizSet = quizSetRepository.save(existingQuizSet);
        return quizSetMapper.toQuizSetResponse(existingQuizSet);
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
     * Get or create a default set if not exist for current user. Default set is unique for each user
     * @return default QuizSet entity
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
     * Get quiz set entity by quiz set id
     * @param quizSetId id of quiz set
     * @return found QuizSet entity
     */
    public QuizSet getQuizSetEntityById(int quizSetId, int userId) {
        QuizSet quizSet = quizSetRepository.findByIdAndUserId(quizSetId, userId).orElseThrow(() -> {
            log.error("Quiz set with id {} not found in user's account", quizSetId);
            return new AppException(ErrorCode.QUIZ_SET_NOT_FOUND);
        });

        return quizSet;
    }
}
