package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.AttemptDetailUpdateRequest;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.AttemptResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.AttemptDetailMapper;
import com.be08.smart_notes.mapper.AttemptMapper;
import com.be08.smart_notes.model.Attempt;
import com.be08.smart_notes.model.AttemptDetail;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.repository.AttemptDetailRepository;
import com.be08.smart_notes.repository.AttemptRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttemptService {
    AuthorizationService authorizationService;
    QuizService quizService;
    AttemptRepository attemptRepository;
    AttemptDetailRepository attemptDetailRepository;
    AttemptMapper attemptMapper;
    AttemptDetailMapper attemptDetailMapper;

    /**
     * Create new attempt for a given quiz. This also map associated questions to attempts details.
     * @param quizId id of target quiz
     * @return attempt response dto
     */
    public AttemptResponse createNewAttempt(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz quiz = quizService.getQuizById(quizId, currentUserId);
        Attempt newAttempt = Attempt.builder()
                .totalQuestion(quiz.getQuestions().size())
                .quiz(quiz)
                .build();

        // Init and add all attempt details with associated questions
        List<AttemptDetail> attemptDetailList = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            AttemptDetail attemptDetail = AttemptDetail.builder()
                    .attempt(newAttempt)
                    .question(question).build();
            attemptDetailList.add(attemptDetail);
            // Other fields (user answer and correctness) are null at default
            // They can be updated through patch request, after the user finish their attempt
        }
        newAttempt.setAttemptDetails(attemptDetailList);

        Attempt savedAttempt = attemptRepository.save(newAttempt);
        return attemptMapper.toAttemptResponse(savedAttempt);
    }

    /**
     * Get attempt and its detail by id and its quiz id
     * @param quizId id of target quiz
     * @param attemptId id of target attempt
     * @return attempt response dto
     */
    public AttemptResponse getAttemptByIdAndQuizId(int quizId, int attemptId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Attempt attempt = getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);

        return attemptMapper.toAttemptResponse(attempt);
    }

    /**
     * Get list of attempts (by pages) based on given quiz id
     * @param quizId id of target quiz
     * @return list of attempt response dto
     */
    public PageResponse<AttemptResponse> getAllAttemptsByQuizId(int quizId, int pageNumber, int pageSize) {
        int currentUserId = authorizationService.getCurrentUserId();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Attempt> page = attemptRepository.findByQuizIdAndQuiz_QuizSet_UserId(quizId, currentUserId, pageable);

        List<AttemptResponse> attempts = page.getContent().stream().map(attemptMapper::toAttemptResponse).toList();

        return PageResponse.<AttemptResponse>builder()
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageData(attempts).build();
    }

    /**
     * Get list of attempts by quiz id
     * @param quizId id of target quiz
     * @return list of attempt response dto
     */
//    public List<AttemptResponse> getAllAttemptsByQuizId(int quizId) {
//        int currentUserId = authorizationService.getCurrentUserId();
//
//        List<Attempt> attempts = attemptRepository.findByQuizIdAndQuiz_QuizSet_UserId(quizId, currentUserId);
//
//        return attemptMapper.toAttemptResponseList(attempts);
//    }

    /**
     * Update single attempt detail (user answer) of given attempt id and quiz id.
     * The method compare user answer and correct answer to set the correctness of the attempt detail.
     * @param quizId id of target quiz
     * @param attemptId id of target attempt
     * @param request request dto with attempt detail id and user answer
     * @return attempt detail response dto
     */
    public AttemptResponse.Detail updateAttemptDetail(int quizId, int attemptId, AttemptDetailUpdateRequest request) {
        int currentUserId = authorizationService.getCurrentUserId();

        // Re-use method to check if the information is valid
        getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);

        AttemptDetail existingAttemptDetail = attemptDetailRepository.findByIdAndAttemptId(request.getId(), attemptId).orElseThrow(() -> {
            log.error("Attempt detail with id {} not found in attempt {}", request.getId(), attemptId);
            return new AppException(ErrorCode.ATTEMPT_DETAIL_NOT_FOUND);
        });

        attemptDetailMapper.updateAttemptDetail(existingAttemptDetail, request);
        existingAttemptDetail = attemptDetailRepository.save(existingAttemptDetail);
        return attemptDetailMapper.toAttemptResponseDetail(existingAttemptDetail);
    }

    /**
     * Calculate attempt result (score) of given attempt id and quiz id.
     * The score is calculated based on the number of correct answer, ignoring unknown (null answer)
     * @param quizId id of target quiz
     * @param attemptId id of target attempt
     * @return attempt response dto
     */
    public AttemptResponse calculateAttemptResult(int quizId, int attemptId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Attempt existingAttempt = getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);
        int score = 0;
        for (AttemptDetail detail : existingAttempt.getAttemptDetails()) {
            if (detail.getIsCorrect() == null || !detail.getIsCorrect()) {
                continue;
            }
            score += 1;
        }
        existingAttempt.setScore(score);

        existingAttempt = attemptRepository.save(existingAttempt);

        return attemptMapper.toAttemptResponse(existingAttempt);
    }

    /**
     * Delete attempt based on given quiz id and attempt id
     * @param quizId id of target quiz
     * @param attemptId id of target attempt
     */
    public void deleteAttemptByIdAndQuizId(int quizId, int attemptId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Attempt attempt = getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);

        attemptRepository.delete(attempt);
    }

    // ------ Methods that returns entity ------ //
    /**
     * Get user's attempt and its detail from database based on given information
     * @param quizId id of target quiz
     * @param attemptId id of target attempt
     * @param userId id of current user
     * @return attempt entity
     */
    public Attempt getAttemptEntityByIdAndQuizId(int quizId, int attemptId, int userId) {
        Attempt attempt = attemptRepository.findByIdAndQuiz_QuizSet_UserId(attemptId, userId).orElseThrow(() -> {
            log.error("Attempt with id {} not found in user's account", attemptId);
            return new AppException(ErrorCode.ATTEMPT_NOT_FOUND);
        });
        if (attempt.getQuiz().getId() != quizId) {
            log.error("Attempt with id {} not found for quiz {}", attemptId, quizId);
            throw new AppException(ErrorCode.ATTEMPT_NOT_FOUND);
        }
        return attempt;
    }
}
