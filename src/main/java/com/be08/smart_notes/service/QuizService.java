package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.filter.QuizFilterDTO;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import com.be08.smart_notes.repository.QuizRepository;
import com.be08.smart_notes.specification.QuizSpecificationBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public PageResponse<QuizResponse> getAllQuizzes(QuizFilterDTO filterDTO, String sortBy, String sortOrder, int pageNumber, int pageSize) {
        int currentUserId = authorizationService.getCurrentUserId();

        Specification<Quiz> spec = QuizSpecificationBuilder.getSpecification(currentUserId, filterDTO);
        Sort sortOption = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortOption);
        Page<Quiz> page = quizRepository.findAll(spec, pageable);

        List<QuizResponse> quizzesResponse = page.getContent().stream().map(quizMapper::toQuizResponse).toList();

        return PageResponse.<QuizResponse>builder()
                .pageInfo(PageResponse.PageInfo.builder()
                        .currentPage(pageNumber)
                        .pageSize(pageSize)
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements()).build())
                .pageData(quizzesResponse).build();
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
