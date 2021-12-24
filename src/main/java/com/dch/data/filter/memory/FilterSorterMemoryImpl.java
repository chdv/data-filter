package com.dch.data.filter.memory;

import com.dch.data.filter.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dch.data.filter.FilterPredicate.toPredicate;

@Component
public class FilterSorterMemoryImpl extends FilterSorterImpl implements FilterSorterMemory {

    @Override
    public <T> ListResponse<T> filterAndSort(
            Stream<T> stream,
            String metaFile,
            Map<String, String[]> params,
            String sort,
            int offset,
            int limit) throws IOException {

        Map<String, FilterFieldType> meta = parseMeta(metaFile);
        List<SortComparator> sorters = parseSorters(sort, meta);
        List<FilterPredicate> filters = parseFilters(params, meta);

        return filterAndSort(stream, filters, sorters, offset, limit);
    }

    @Override
    public <T> ListResponse<T> filterAndSort(
            Stream<T> stream,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit) {

        List<T> list = stream
                .filter(toPredicate(filters))
                .sorted(SortComparator.toComparator(sorters))
                .collect(Collectors.toList());

        return subListResponse(list, filters, sorters, offset, limit);
    }

    @Override
    public <T> ListResponse<T> subListResponse(
            List<T> list,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit) {

        return ListResponse.<T>fromList(list, offset, limit)
                .filter(toWebFilter(filters))
                .sort(toWebSorters(sorters))
                .build();
    }

}
