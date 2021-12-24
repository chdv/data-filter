package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import com.dch.data.filter.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * ObjectFieldOperator.
 */
@Slf4j
@RequiredArgsConstructor
public class ObjectFieldOperator<T> implements FieldTypeOperator<T> {

    private final Mapper<String,T> mapper;

    @Override
    public T parse(String str) {
        return mapper.toDbo(str);
    }

    @Override
    public T cast(Object fieldValue) {
        return (T)fieldValue;
    }

    @Override
    public Predicate<T> predicate(List<T> webValues, FilterOperator operator) {
        if (operator == FilterOperator.IN) {
            return fieldValue -> {
                for (T c : webValues) {
                    if (Objects.equals(c, fieldValue))
                        return true;
                }
                return false;
            };
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true filter predicate.");
        return fieldValue->true;
    }

    @Override
    public Predicate<T> predicate(T webValue, FilterOperator operator) {
        switch (operator) {
            case EQ:
                return fieldValue -> Objects.equals(fieldValue, webValue);
            case NE:
                return fieldValue -> !Objects.equals(fieldValue, webValue);
            case NULL:
                return Objects::isNull;
            case NOT_NULL:
                return Objects::nonNull;
            default:
                log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
                return fieldValue->true;
        }
    }
}
