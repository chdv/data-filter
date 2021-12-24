package com.dch.data.filter;

import com.dch.data.filter.util.InvokeUtils;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Predicate;

@Value
@Slf4j
public class FilterPredicate<T> implements Predicate<T> {

    private final String field;

    private final FilterOperator operator;

    private final String value;

    private final FilterFieldType fieldType;

    private final Predicate predicate;

    public FilterPredicate(String field, FilterOperator operator, String value, FilterFieldType fieldType) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.fieldType = fieldType;
        this.predicate = fieldType.getTypeOperator().createPredicate(value, operator);
    }

    public boolean test(T bean) {
        Object fieldValue = InvokeUtils.invokeGetter(bean, field);

        return predicate.test(fieldType.getTypeOperator().cast(fieldValue));
    }

    public static <T> Predicate<T> toPredicate(List<FilterPredicate> filters) {
        return filters.stream().map(f->(Predicate)f).reduce(a->true, Predicate::and);
    }
}
