package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.ai.QuizQuestion;
import com.be08.smart_notes.mapper.QuizMapper;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizMapper quizMapper;

    public Quiz createQuiz(int userId, int sourceDocumentId, QuizQuestion quizQuestion) {
        Quiz quiz = quizMapper.fromQuizResponseToQuiz(quizQuestion);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUserId(userId);

        for (Question question : quiz.getQuestions()) {
            question.setSourceDocumentId(sourceDocumentId);
            question.setQuiz(null);
        }

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(int id) {
        quizRepository.deleteById(id);
    }
}
