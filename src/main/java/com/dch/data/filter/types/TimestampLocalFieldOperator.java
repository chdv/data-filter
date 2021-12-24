package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * TimestampLocalFieldOperator.
 */
@Slf4j
public class TimestampLocalFieldOperator implements FieldTypeOperator<LocalDateTime> {

    @Override
    public LocalDateTime parse(String str) {
        return ZonedDateTime.parse(str).toLocalDateTime();
    }

    @Override
    public LocalDateTime cast(Object fieldValue) {
        return (LocalDateTime) fieldValue;
    }

    @Override
    public Predicate<LocalDateTime> predicate(LocalDateTime webValue, FilterOperator operator) {
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
    public Predicate<LocalDateTime> predicate(List<LocalDateTime> webValues, FilterOperator operator) {
        LocalDateTime first = webValues.get(0);
        LocalDateTime second = webValues.get(1);
        if (operator == FilterOperator.BETWEEN) {
            return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isAfter(first) && fieldValue.isBefore(second);
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
        return fieldValue -> true;
    }
}
