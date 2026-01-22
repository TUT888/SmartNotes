package com.be08.smart_notes.specification;

import com.be08.smart_notes.dto.filter.NoteFilterDTO;
import com.be08.smart_notes.model.Document;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NoteSpecificationBuilder {
    public static Specification<Document> getSpecification(int userId, NoteFilterDTO filterDTO) {
        // Build Specification using root (Document), criteria query, and criteria builder
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.getKeyword() != null && !filterDTO.getKeyword().isEmpty()) {
                String targetKeyword = "%" + filterDTO.getKeyword().toLowerCase() + "%";

                Predicate titleMatch = cb.like(cb.lower(root.get("title")), targetKeyword);
                Predicate contentMatch = cb.like(cb.lower(root.get("content")), targetKeyword);

                Predicate keywordPredicate = cb.or(titleMatch, contentMatch);
                predicates.add(keywordPredicate);
            }

            if (filterDTO.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filterDTO.getCreatedFrom()));
            }

            if (filterDTO.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filterDTO.getCreatedTo()));
            }

            if (filterDTO.getUpdatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), filterDTO.getUpdatedFrom()));
            }
            if  (filterDTO.getUpdatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), filterDTO.getUpdatedTo()));
            }

            predicates.add(cb.equal(root.get("userId"), userId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
