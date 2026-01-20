package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.QuizUpsertDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class QuizService {
    AuthorizationService authorizationService;
    QuizSetService quizSetService;
    QuizRepository quizRepository;
    QuizMapper quizMapper;

    /**
     * Create new quiz based on given information.
     * If the request does not include quiz set id, new quiz will be added to DEFAULT set
     * @param quizUpsertDTO a dto with quiz information and its questions
     * @return quiz response dto
     */
    public QuizResponse createQuiz(QuizUpsertDTO quizUpsertDTO) {
        int currentUserId = authorizationService.getCurrentUserId();

        Integer quizSetId = quizUpsertDTO.getQuizSetId();
        QuizSet existingQuizSet = null;
        if (quizSetId != null) {
            existingQuizSet = quizSetService.getQuizSetEntityById(quizSetId, currentUserId);
        }
        if (existingQuizSet == null) {
            existingQuizSet = quizSetService.getOrCreateDefaultSet(currentUserId);
        }

        Quiz newQuiz = quizMapper.toQuiz(quizUpsertDTO);
        newQuiz.setQuizSet(existingQuizSet);

        Quiz savedQuiz = quizRepository.save(newQuiz);
        return quizMapper.toQuizResponse(savedQuiz);
    }

    /**
     * Get quiz and its questions based on given id
     * @param quizId id of target quiz
     * @return quiz response dto
     */
    public QuizResponse getQuizById(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz quiz = getQuizById(quizId, currentUserId);

        return quizMapper.toQuizResponse(quiz);
    }

    /**
     * Get all quiz and its questions based on given id
     * @return quiz response list dto
     */
    public List<QuizResponse> getAllQuizzes() {
        int currentUserId = authorizationService.getCurrentUserId();

        List<Quiz> quizzes = quizRepository.findAllByQuizSetUserId(currentUserId);

        return quizMapper.toQuizResponseList(quizzes);
    }

    /**
     * Update quiz information
     * @param quizId id of target quiz
     * @param quizUpsertDTO dto that contains required data
     * @return quiz response dto
     */
    public QuizResponse updateQuiz(int quizId, QuizUpsertDTO quizUpsertDTO) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz existingQuiz = quizRepository.findByIdAndQuizSetUserId(quizId, currentUserId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        if (quizUpsertDTO.getQuizSetId() != null) {
            QuizSet quizSet = quizSetService.getQuizSetEntityById(quizUpsertDTO.getQuizSetId(), currentUserId);
            existingQuiz.setQuizSet(quizSet);
        }

        quizMapper.updateQuiz(existingQuiz, quizUpsertDTO);
        existingQuiz = quizRepository.save(existingQuiz);
        return quizMapper.toQuizResponse(existingQuiz);
    }

    /**
     * Delete quiz based on given id
     * @param quizId id of target quiz
     */
    public void deleteQuizById(int quizId) {
        int currentUserId = authorizationService.getCurrentUserId();

        Quiz quiz = quizRepository.findByIdAndQuizSetUserId(quizId, currentUserId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });

        quizRepository.deleteById(quizId);
    }

    // ------ Methods that returns entity ------ //
    public Quiz getQuizById(int quizId, int userId) {
        return quizRepository.findByIdAndQuizSetUserId(quizId, userId).orElseThrow(() -> {
            log.error("Quiz with id {} not found in user's account", quizId);
            return new AppException(ErrorCode.QUIZ_NOT_FOUND);
        });
    }
}
