package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * FieldTypeOperator.
 *
 */
public interface FieldTypeOperator<T> {

    T parse(String str);

    T cast(Object fieldValue);

    Predicate<T> predicate(T webValue, FilterOperator operator);

    Predicate<T> predicate(List<T> webValues, FilterOperator operator);

    default List<T> parse(String[] values) {
        return Arrays.stream(values)
                .map(this::parse).collect(Collectors.toList());
    }

    default Predicate<T> createPredicate(String webValue, FilterOperator operator) {
        if(operator.needSplit()) {
            String[] values = operator.splitValue(webValue);
            return predicate(parse(values), operator);
        } else {
            return predicate(parse(webValue), operator);
        }
    }
}
