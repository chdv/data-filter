package com.dch.data.filter;

import com.dch.data.filter.util.InvokeUtils;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

@Value
@AllArgsConstructor
@Slf4j
public class SortComparator<T> implements Comparator<T> {

    private final String field;

    private final Sort.Direction direction;

    @Override
    public int compare(T o1, T o2) {
        Comparable field1 = InvokeUtils.invokeGetter(o1, field);
        Comparable field2 = InvokeUtils.invokeGetter(o2, field);
        var inverter = direction.isAscending() ? 1 : -1;
        return inverter * field1.compareTo(field2);
    }

    public static Comparator<Object> toComparator(List<SortComparator> sorters) {
        return sorters.stream().map(f->(Comparator<Object>)f).reduce((a1, a2)->0, Comparator::thenComparing);
    }

}
