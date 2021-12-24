package com.dch.data.filter;

import com.dch.data.filter.util.Pair;
import com.dch.data.filter.util.ResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FilterSorterImpl implements FilterSorter {

    @Override
    public <T> ListResponse<T> response(
            List<T> list,
            List<FilterPredicate> filters,
            List<SortComparator> sorters,
            int offset,
            int limit,
            long count) {

        return ListResponse.<T>builder()
                .count(count)
                .limit(limit)
                .offset(offset)
                .result(list)
                .filter(toWebFilter(filters))
                .sort(toWebSorters(sorters))
                .build();
    }

    @Override
    public List<FilterPredicate> parseFilters(Map<String, String[]> params, Map<String, FilterFieldType> meta) {
        return params.entrySet().stream()

                        .filter(entry->meta.containsKey(entry.getKey()))

                        .flatMap(entry ->
                                Stream.of(entry.getValue())
                                        .map(value ->
                                                new Pair(entry.getKey(), value)
                                        ))

                        .map(entry ->
                                new Pair<>(
                                        entry.getKey(),
                                        parseFilter((String)entry.getValue())))

                        .filter(entry->entry.getValue().getKey()!=null)

                        .map(entry->new FilterPredicate(
                                (String)entry.getKey(),
                                entry.getValue().getKey(),
                                entry.getValue().getValue(),
                                meta.get(entry.getKey())))

                        .collect(Collectors.toList());
    }

    private Pair<FilterOperator, String> parseFilter(String filter) {
        int delimIndex = filter.indexOf(FILTER_OP_VALUE_DELIMETER);
        String filterOp = filter;
        String filterVal = null;
        if(delimIndex > 0) {
            filterOp = filter.substring(0, delimIndex);
            filterVal = filter.substring(delimIndex + 1);
        }
        FilterOperator op = FilterOperator.fromCode(filterOp);
        return new Pair<>(op, filterVal);
    }

    @Override
    public List<SortComparator> parseSorters(String sort, Map<String, FilterFieldType> meta) {
        List<SortComparator> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(sort)) {
            result = Arrays.stream(sort.split(SORTER_DELIMETER))
                            .filter(s -> meta.containsKey(parseSortField(s)))
                            .map(s->new SortComparator(parseSortField(s), parseDirection(s)))
                            .collect(Collectors.toList());
        }

        return result;
    }

    private String parseSortField(String sorter) {
        if (sorter.startsWith(SORTER_DIRECTION))
            return sorter.substring(1);
        else
            return sorter;
    }

    private Sort.Direction parseDirection(String sorter) {
        if (sorter.startsWith(SORTER_DIRECTION))
            return Sort.Direction.DESC;
        else
            return Sort.Direction.ASC;
    }

    @Override
    public Map<String, FilterFieldType> parseMeta(String fileName) throws IOException {
        JsonNode node = new ObjectMapper().readValue(ResourceUtils.readClasspathResource(fileName), JsonNode.class);
        Iterator<Map.Entry<String, JsonNode>> it = node.get("fields").fields();
        Map<String, FilterFieldType> result = new HashMap<>();
        while(it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            result.put(
                    entry.getKey(),
                    FilterFieldType.fromCode(entry.getValue().get("type").asText())
            );
        }

        return result;
    }

    protected String toWebString(FilterPredicate fl) {
        return fl.getField() + FILTER_NAME_VALUE_DELIMETER + fl.getOperator().getCode() + FILTER_OP_VALUE_DELIMETER + fl.getValue();
    }

    protected String toWebString(SortComparator sl) {
        return (Objects.equals(sl.getDirection(),Sort.Direction.DESC) ? SORTER_DIRECTION : "") + sl.getField();
    }

    protected List<String> toWebFilter(List<FilterPredicate> filters) {
        return filters.stream().map(this::toWebString).collect(Collectors.toList());
    }

    protected List<String> toWebSorters(List<SortComparator> sorters) {
        return sorters.stream().map(this::toWebString).collect(Collectors.toList());
    }
}
