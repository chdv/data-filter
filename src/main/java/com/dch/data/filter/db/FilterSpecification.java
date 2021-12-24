package com.dch.data.filter.db;

import com.dch.data.filter.FilterPredicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FilterSpecification<T> implements Specification<T> {

    private final transient FilterPredicate filter;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if(!filter.getOperator().needSplit()) {
            Comparable value = (Comparable) filter.getFieldType().getTypeOperator().parse(filter.getValue());
            switch (filter.getOperator()) {
                case GT:
                    return builder.greaterThan(root.get(filter.getField()), value);
                case LT:
                    return builder.lessThan(root.get(filter.getField()), value);
                case GTE:
                    return builder.greaterThanOrEqualTo(root.get(filter.getField()), value);
                case LTE:
                    return builder.lessThanOrEqualTo(root.get(filter.getField()), value);
                case LIKE:
                    if (root.get(filter.getField()).getJavaType() == String.class && value!=null) {
                        String sv = "%" + value.toString().toUpperCase() + "%";
                        return builder.like(builder.upper(root.get(filter.getField())), sv);
                    } else {
                        return builder.equal(root.get(filter.getField()), value);
                    }
                case EQ:
                    return builder.equal(root.get(filter.getField()), value);
                case NE:
                    return builder.notEqual(root.get(filter.getField()), value);
                case NULL:
                    return builder.isNull(root.get(filter.getField()));
                case NOT_NULL:
                    return builder.isNotNull(root.get(filter.getField()));
                default:
                    log.warn("Operator " + filter.getOperator() + " for fieldType not found. Return null predicate.");
                    return null;
            }
        } else {
            String[] svalues = filter.getOperator().splitValue(filter.getValue());
            List<Comparable> values = filter.getFieldType().getTypeOperator().parse(svalues);
            switch (filter.getOperator()) {
                case IN:
                    CriteriaBuilder.In in = builder.in(root.get(filter.getField()));
                    values.forEach(in::value);
                    return in;
                case BETWEEN:
                    return builder.between(root.get(filter.getField()), values.get(0), values.get(1));
                default:
                    log.warn("Operator " + filter.getOperator() + " for fieldType not found. Return null predicate.");
                    return null;
            }
        }
    }
}
