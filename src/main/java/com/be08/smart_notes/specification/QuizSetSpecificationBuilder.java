package com.be08.smart_notes.specification;

import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.model.QuizSet;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class QuizSetSpecificationBuilder {
    public static Specification<QuizSet> getSpecification(int userId, BasicFilterDTO filterDTO) {
        // Build Specification using root (QuizSet), criteria query, and criteria builder
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            CommonPredicateBuilder.addKeywordPredicate(root, cb, predicates, filterDTO, "title");
            CommonPredicateBuilder.addDateRangePredicates(root, cb, predicates, filterDTO);

            predicates.add(cb.equal(root.get("userId"), userId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
