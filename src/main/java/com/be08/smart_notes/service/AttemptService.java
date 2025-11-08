package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.AttemptDetailUpdateRequest;
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

    public AttemptResponse getAttemptByIdAndQuizId(int quizId, int attemptId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Attempt attempt = getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);

        return attemptMapper.toAttemptResponse(attempt);
    }

    public List<AttemptResponse> getAllAttemptsByQuizId(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        List<Attempt> attempts = attemptRepository.findByQuizIdAndQuiz_QuizSet_UserId(quizId, currentUserId);

        return attemptMapper.toAttemptResponseList(attempts);
    }

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

    public void deleteAttemptByIdAndQuizId(int quizId, int attemptId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Attempt attempt = getAttemptEntityByIdAndQuizId(quizId, attemptId, currentUserId);

        attemptRepository.delete(attempt);
    }

    // ------ Methods that returns entity ------ //
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
