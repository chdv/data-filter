package com.dch.data.filter.db;

import com.dch.data.filter.FilterPredicate;
import com.dch.data.filter.SortComparator;
import com.dch.data.filter.FilterSorter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FilterSorterDb extends FilterSorter {

    Specification toSpecification(List<FilterPredicate> filters);

    Sort toSort(List<SortComparator> sorters);

    Pageable toPageable(int offset, int limit, List<SortComparator> sorters);

}
