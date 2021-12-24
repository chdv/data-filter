package com.dch.data.filter.elastic;

import com.dch.data.filter.FilterFieldType;
import com.dch.data.filter.FilterPredicate;
import com.dch.data.filter.FilterSorterImpl;
import com.dch.data.filter.SortComparator;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FilterSorterElasticImpl extends FilterSorterImpl implements FilterSorterElastic {

    private static final String TERM_MARKER = ".keyword";

    private static final String FIRST_MISSING_VALUE = "_first";

    private static final String LAST_MISSING_VALUE = "_last";

    @Override
    public SearchSourceBuilder toSearchSourceBuilder(Map<String, FilterFieldType> metaTypes, List<FilterPredicate> filters, List<SortComparator> sorters, int offset, int limit) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(offset);
        sourceBuilder.size(limit - offset);

        addSort(metaTypes, sorters, sourceBuilder);

        sourceBuilder.postFilter(filterQuery(filters));

        return sourceBuilder;
    }

    private BoolQueryBuilder filterQuery(List<FilterPredicate> filters) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        for(FilterPredicate filter : filters) {
            queryBuilder.must(buildQuery(filter));
        }

        return queryBuilder;
    }

    @Override
    public QueryBuilder buildQuery(FilterPredicate filter) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if(!filter.getOperator().needSplit()) {
            Comparable value = (Comparable) filter.getFieldType().getTypeOperator().parse(filter.getValue());
            switch (filter.getOperator()) {
                case GT:
                    queryBuilder.must(QueryBuilders.rangeQuery(filter.getField()).gt(value));
                    break;
                case LT:
                    queryBuilder.must(QueryBuilders.rangeQuery(filter.getField()).lt(value));
                    break;
                case GTE:
                    queryBuilder.must(QueryBuilders.rangeQuery(filter.getField()).gte(value));
                    break;
                case LTE:
                    queryBuilder.must(QueryBuilders.rangeQuery(filter.getField()).lte(value));
                    break;
                case LIKE:
                    String sv = "*" + value + "*";
                    queryBuilder.must(QueryBuilders.wildcardQuery(termMarker(filter.getField()), sv));
                    break;
                case EQ:
                    queryBuilder.must(QueryBuilders.termQuery(filter.getField(), value));
                    break;
                case NE:
                    queryBuilder.mustNot(QueryBuilders.termQuery(filter.getField(), value));
                    break;
                case NULL:
                    queryBuilder.mustNot(QueryBuilders.existsQuery(filter.getField()));
                    break;
                case NOT_NULL:
                    queryBuilder.must(QueryBuilders.existsQuery(filter.getField()));
                    break;
                default:
                    log.warn("Operator " + filter.getOperator() + " for fieldType not found. Return null predicate.");
            }
        } else {
            String[] svalues = filter.getOperator().splitValue(filter.getValue());
            List<Comparable> values = filter.getFieldType().getTypeOperator().parse(svalues);
            switch (filter.getOperator()) {
                case IN:
                    for (Comparable value : values) {
                        queryBuilder.should(QueryBuilders.termQuery(termMarker(filter.getField()), value));
                    }
                    break;
                case BETWEEN:
                    queryBuilder.must(QueryBuilders.rangeQuery(filter.getField()).gt(values.get(0)).lt(values.get(1)));
                    break;
                default:
                    log.warn("Operator " + filter.getOperator() + " for fieldType not found. Return null predicate.");
                    break;
            }
        }

        return queryBuilder;
    }

    private void addSort(Map<String, FilterFieldType> metaTypes, List<SortComparator> sorters, SearchSourceBuilder sourceBuilder) {
        for (SortComparator sort : sorters) {
            sourceBuilder.sort(buildSort(metaTypes.get(sort.getField()), sort));
        }
    }

    @Override
    public FieldSortBuilder buildSort(FilterFieldType type, SortComparator sort) {
        SortOrder order = null;
        String missingValue = null;

        switch(sort.getDirection()) {
            case ASC:
                order = SortOrder.ASC;
                missingValue = LAST_MISSING_VALUE;
                break;
            case DESC:
                order = SortOrder.DESC;
                missingValue = FIRST_MISSING_VALUE;
                break;
            default:
                log.warn("Direction " + sort.getDirection() + " not process.");
                break;
        }

        String filterField = sort.getField();
        if(type == FilterFieldType.STRING) {
            filterField = termMarker(filterField);
        }

        return SortBuilders.fieldSort(filterField).order(order).missing(missingValue);
    }

    private String termMarker(String fieldFilter) {
        return fieldFilter + TERM_MARKER;
    }

}
