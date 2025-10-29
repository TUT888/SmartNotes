package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.ai.QuizResponse;
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

    public QuizResponse saveQuizFromAIResponse(int userId, int sourceDocumentId, AIQuizResponse aiQuizResponse) {
        Quiz quiz = quizMapper.fromAIQuizResponseToQuiz(aiQuizResponse);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUserId(userId);

        for (Question question : quiz.getQuestions()) {
            question.setSourceDocumentId(sourceDocumentId);
            question.setQuiz(quiz);
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        return quizMapper.fromQuizToQuizResponse(savedQuiz);
    }

    public QuizResponse getQuizById(int id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        return quizMapper.fromQuizToQuizResponse(quiz);
    }

    public void deleteQuizById(int id) {
        quizRepository.deleteById(id);
    }
}
