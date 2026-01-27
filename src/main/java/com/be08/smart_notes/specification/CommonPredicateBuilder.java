package com.be08.smart_notes.specification;

import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class CommonPredicateBuilder {
    public static void addDateRangePredicates(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, BasicFilterDTO filterDTO) {
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
    }

    public static void addKeywordPredicate(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, BasicFilterDTO filterDTO, String targetProp) {
        if (filterDTO.getKeyword() != null && !filterDTO.getKeyword().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get(targetProp)), "%" + filterDTO.getKeyword().toLowerCase() + "%"));
        }
    }

    public static void addKeywordOrPredicate(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates, BasicFilterDTO filterDTO, String targetProp1, String targetProp2) {
        if (filterDTO.getKeyword() != null && !filterDTO.getKeyword().isEmpty()) {
            String targetKeyword = "%" + filterDTO.getKeyword().toLowerCase() + "%";

            Predicate firstMatch = cb.like(cb.lower(root.get(targetProp1)), targetKeyword);
            Predicate secondMatch = cb.like(cb.lower(root.get(targetProp2)), targetKeyword);

            Predicate keywordPredicate = cb.or(firstMatch, secondMatch);
            predicates.add(keywordPredicate);
        }
    }
}
