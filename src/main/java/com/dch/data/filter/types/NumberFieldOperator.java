package com.dch.data.filter.types;

import com.dch.data.filter.FilterOperator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * NumberFieldOperator.
 */
@Slf4j
public class NumberFieldOperator implements FieldTypeOperator<BigDecimal> {

    @Override
    public BigDecimal parse(String str) {
        return new BigDecimal(str);
    }

    @Override
    public BigDecimal cast(Object fieldValue) {
        BigDecimal result = null;
        if(fieldValue!=null) {
            result = new BigDecimal(fieldValue.toString());
        }
        return result;
    }

    @Override
    public Predicate<BigDecimal> predicate(BigDecimal webValue, FilterOperator operator) {
        switch (operator) {
            case LTE:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) <= 0;
            case LT:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) < 0;
            case GTE:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) >= 0;
            case GT:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) > 0;
            case EQ:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) == 0;
            case NE:
                return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(webValue) != 0;
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
    public Predicate<BigDecimal> predicate(List<BigDecimal> webValues, FilterOperator operator) {
        BigDecimal first = webValues.get(0);
        BigDecimal second = webValues.get(1);
        if(operator == FilterOperator.BETWEEN) {
            return fieldValue -> nonNull(fieldValue) && fieldValue.compareTo(first) >= 0 && fieldValue.compareTo(second) <= 0;
        }
        log.warn("Operator " + operator + " for fieldType " + this + " not found. Return true predicate.");
        return fieldValue->true;
    }

}
