package com.dch.data.filter.elastic;

import com.dch.data.filter.FilterFieldType;
import com.dch.data.filter.FilterPredicate;
import com.dch.data.filter.FilterSorter;
import com.dch.data.filter.SortComparator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.util.List;
import java.util.Map;

public interface FilterSorterElastic extends FilterSorter {

    SearchSourceBuilder toSearchSourceBuilder(
            Map<String, FilterFieldType> metaTypes,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset, int limit);

    QueryBuilder buildQuery(FilterPredicate filter);

    FieldSortBuilder buildSort(FilterFieldType type, SortComparator sort);

}
