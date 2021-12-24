package com.dch.data.filter.memory;

import com.dch.data.filter.SortComparator;
import com.dch.data.filter.FilterPredicate;
import com.dch.data.filter.FilterSorter;
import com.dch.data.filter.ListResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface FilterSorterMemory extends FilterSorter {

    <T> ListResponse<T> filterAndSort(
            Stream<T> stream,
            String metaFile,
            Map<String, String[]> params,
            String sort,
            int offset,
            int limit) throws IOException;

    <T> ListResponse<T> filterAndSort(
            Stream<T> stream,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit);

    <T> ListResponse<T> subListResponse(
            List<T> list,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit);

}
