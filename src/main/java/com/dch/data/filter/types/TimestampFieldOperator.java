package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * TimestampFieldOperator.
 */
@Slf4j
public class TimestampFieldOperator implements FieldTypeOperator<ZonedDateTime> {

    @Override
    public ZonedDateTime parse(String str) {
        return ZonedDateTime.parse(str);
    }

    @Override
    public ZonedDateTime cast(Object fieldValue) {
        return (ZonedDateTime)fieldValue;
    }

    @Override
    public Predicate<ZonedDateTime> predicate(ZonedDateTime webValue, FilterOperator operator) {
        switch (operator) {
            case LTE:
                return  fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isBefore(webValue);
            case GTE:
                return  fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isAfter(webValue);
            default:
                log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
                return  fieldValue->true;
        }
    }

    @Override
    public Predicate<ZonedDateTime> predicate(List<ZonedDateTime> webValues, FilterOperator operator) {
        ZonedDateTime first = webValues.get(0);
        ZonedDateTime second = webValues.get(1);
        if(operator == FilterOperator.BETWEEN) {
            return  fieldValue -> Objects.nonNull(fieldValue) && fieldValue.isAfter(first) && fieldValue.isBefore(second);
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
        return  fieldValue->true;
    }
}
