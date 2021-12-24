package com.dch.data.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class OffsetPage<T> {

    private final List<T> list;

    private final long count;

}
