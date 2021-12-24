package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * StringFieldOperator.
 */
@Slf4j
public class StringFieldOperator implements FieldTypeOperator<String> {

    @Override
    public String parse(String str) {
        return str;
    }

    @Override
    public String cast(Object fieldValue) {
        String result = null;
        if(fieldValue!=null) {
            result = fieldValue.toString();
        }
        return result;
    }

    @Override
    public Predicate<String> predicate(List<String> webValues, FilterOperator operator) {
        if (operator == FilterOperator.IN) {
            return fieldValue -> {
                for (Comparable c : webValues) {
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
    public Predicate<String> predicate(String webValue, FilterOperator operator) {
        switch (operator) {
            case EQ:
                return fieldValue -> Objects.equals(fieldValue, webValue);
            case NE:
                return fieldValue -> !Objects.equals(fieldValue, webValue);
            case LIKE:
                return fieldValue -> Objects.nonNull(fieldValue) && fieldValue.toLowerCase().contains(webValue.toLowerCase());
            case NULL:
                return fieldValue -> (Objects.isNull(fieldValue) || StringUtils.isEmpty(fieldValue));
            case NOT_NULL:
                return fieldValue -> (Objects.nonNull(fieldValue) && !StringUtils.isEmpty(fieldValue));
            default:
                log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
                return fieldValue->true;
        }
    }

}
