package com.dch.data.filter;

import com.dch.data.filter.db.FilterSorterDb;
import com.dch.data.filter.memory.FilterSorterMemory;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@DisplayName("Test FilterSorter")
@SpringJUnitConfig({TestConfig.class, DataSourceConfig.class})
public class FilterSorterTest {

    public static final String META = "meta/test.list.json";

    @Autowired
    private FilterSorterDb filterSorterDb;

    @Autowired
    private FilterSorterMemory filterSorterMemory;

    @Autowired
    private TestEntityRepository repository;

    private void fillTestDbData() {
        initTestData().forEach(repository::save);
    }

    private Stream<TestEntity> initTestData() {
        return Stream.of(TestEntity.builder()
                .id("1")
                .date(LocalDateTime.now())
                .name("name1")
                .check(false)
                .type("type1")
                .build(),

                TestEntity.builder()
                .id("2")
                .date(LocalDateTime.now())
                .name("name2")
                .check(false)
                .type("type2")
                .build());
    }

    @Rollback
    @Test
    @DisplayName("Test DB Filter")
    public void testDbFilter() throws IOException {

        fillTestDbData();

        int cLimit = 20;
        int cOffset = 0;

        Map<String, String[]> parameters = Map.of("name", new String[]{"like:name1"});
        String sort = "name";

        var meta = filterSorterDb.parseMeta(META);

        List<FilterPredicate> filters = filterSorterDb.parseFilters(parameters, meta);
        List<SortComparator> sorters = filterSorterDb.parseSorters(sort, meta);

        Specification filterSpecification = filterSorterDb.toSpecification(filters);
        Pageable pageable = filterSorterDb.toPageable(cOffset, cLimit, sorters);

        Page<TestEntity> page = repository.findAll(filterSpecification, pageable);

        MatcherAssert.assertThat("result founded",
                page.getTotalElements(),
                Matchers.equalTo(1L));
    }

    @Test
    @DisplayName("Test Memory Filter")
    public void testMemoryFilter() throws IOException {
        int cLimit = 20;
        int cOffset = 0;

        Map<String, String[]> parameters = Map.of("name", new String[]{"like:name1"});
        String sort = "name";

        var meta = filterSorterMemory.parseMeta(META);

        List<FilterPredicate> filters = filterSorterMemory.parseFilters(parameters, meta);
        List<SortComparator> sorters = filterSorterMemory.parseSorters(sort, meta);

        var stream = initTestData();

        var list = filterSorterMemory.filterAndSort(stream, filters, sorters, cOffset, cLimit);

        MatcherAssert.assertThat("result founded",
                list.getResult(),
                Matchers.hasSize(1));

    }

}
