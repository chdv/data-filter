package com.dch.data.filter.db;

import com.dch.data.filter.FilterPredicate;
import com.dch.data.filter.FilterSorterImpl;
import com.dch.data.filter.SortComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterSorterDbImpl extends FilterSorterImpl implements FilterSorterDb {

    @Override
    public Specification toSpecification(List<FilterPredicate> filters) {
        return filters.stream()
                .map(FilterSpecification::new)
                .map(s -> (Specification) s)
                .reduce(Specification.where(null), (s1, s2) -> s1.and(s2));
    }

    @Override
    public Sort toSort(List<SortComparator> sorters) {
        return sorters.stream()
                .map(sc -> Sort.by(sc.getDirection(), sc.getField()))
                .reduce(Sort.unsorted(), (s1, s2) -> s1.and(s2));
    }

    @Override
    public Pageable toPageable(int offset, int limit, List<SortComparator> sorters) {
        return new OffsetPageable(offset, limit, toSort(sorters));
    }
}
