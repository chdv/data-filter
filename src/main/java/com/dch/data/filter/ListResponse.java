package com.dch.data.filter;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
@Accessors(chain = true)
public class ListResponse<T> {

    @Builder.Default
    private List<T> result = emptyList();
    @Builder.Default
    private long count = 0;
    @Builder.Default
    private long offset = 0;
    @Builder.Default
    private long limit = 10;
    @Builder.Default
    private List<String> filter = emptyList();
    @Builder.Default
    private List<String> sort = emptyList();

    public static <T> ListResponse.ListResponseBuilder<T> fromList(
            final List<T> list,
            final int offset,
            final int limit) {

        int count = list.size();

        int vLimit = limit > count ? count : limit;

        List vList = Collections.emptyList();

        if (offset < vLimit) vList = list.subList(offset, vLimit);

        return ListResponse.<ListResponse<T>>builder().count(count).limit(vLimit).offset(offset).result(vList);
    }

}
