package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * TimestampLocalFieldOperator.
 */
@Slf4j
public class TimestampInstantFieldOperator implements FieldTypeOperator<Instant> {

    @Override
    public Instant parse(String str) {
        return ZonedDateTime.parse(str).toInstant();
    }

    @Override
    public Instant cast(Object fieldValue) {
        return (Instant) fieldValue;
    }

    @Override
    public Predicate<Instant> predicate(Instant webValue, FilterOperator operator) {
        switch (operator) {
            case LTE:
                return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isBefore(webValue);
            case GTE:
                return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isAfter(webValue);
            default:
                log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
                return fieldValue -> true;
        }
    }

    @Override
    public Predicate<Instant> predicate(List<Instant> webValues, FilterOperator operator) {
        Instant first = webValues.get(0);
        Instant second = webValues.get(1);
        if (operator == FilterOperator.BETWEEN) {
            return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isAfter(first) && fieldValue.isBefore(second);
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
        return fieldValue -> true;
    }
}
