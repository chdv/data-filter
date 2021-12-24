package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * BooleanFieldOperator.
 */
@Slf4j
public class BooleanFieldOperator implements FieldTypeOperator<Boolean> {

    @Override
    public Boolean parse(String str) {
        return Boolean.parseBoolean(str);
    }

    @Override
    public Boolean cast(Object fieldValue) {
        return (Boolean)fieldValue;
    }

    @Override
    public Predicate<Boolean> predicate(Boolean webValue, FilterOperator operator) {
        switch (operator) {
            case EQ:
                return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.compareTo(webValue) == 0;
            case NE:
                return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.compareTo(webValue) != 0;
            case NULL:
                return Objects::isNull;
            case NOT_NULL:
                return Objects::nonNull;
            default:
                log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
                return fieldValue->true;
        }
    }

    @Override
    public Predicate<Boolean> predicate(List<Boolean> webValues, FilterOperator operator) {
        if(operator == FilterOperator.IN) {
            return fieldValue -> {
                if (fieldValue == null) return false;
                for (Comparable c : webValues) {
                    if (Objects.equals(c, fieldValue))
                        return true;
                }
                return false;
            };
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
        return fieldValue->true;
    }

}
