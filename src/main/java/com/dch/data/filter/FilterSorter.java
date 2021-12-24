package com.dch.data.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * FilterSorter.
 */
public interface FilterSorter {

    String DEFAULT_DELIMETER = ",";

    String SORTER_DELIMETER = DEFAULT_DELIMETER;

    String SORTER_DIRECTION = "!";

    String FILTER_OP_VALUE_DELIMETER = ":";

    String FILTER_NAME_VALUE_DELIMETER = "=";

    <T> ListResponse<T> response(
            List<T> list,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit,
            long count);

    List<FilterPredicate> parseFilters(Map<String, String[]> params, Map<String, FilterFieldType> meta);

    List<SortComparator> parseSorters(String sort, Map<String, FilterFieldType> meta);

    Map<String, FilterFieldType> parseMeta(String fileName) throws IOException;
}
