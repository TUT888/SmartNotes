package com.be08.smart_notes.specification;

import com.be08.smart_notes.dto.filter.QuizFilterDTO;
import com.be08.smart_notes.model.Quiz;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class QuizSpecificationBuilder {
    public static Specification<Quiz> getSpecification(int userId, QuizFilterDTO filterDTO) {
        // Build Specification using root (Quiz), criteria query, and criteria builder
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            CommonPredicateBuilder.addKeywordPredicate(root, cb, predicates, filterDTO, "title");
            CommonPredicateBuilder.addDateRangePredicates(root, cb, predicates, filterDTO);

            if (filterDTO.getQuizSetId() != null) {
                predicates.add(cb.equal(root.get("quizSet").get("id"), filterDTO.getQuizSetId()));
            }

            predicates.add(cb.equal(root.get("quizSet").get("userId"), userId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
