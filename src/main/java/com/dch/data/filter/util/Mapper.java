package com.dch.data.filter.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый интерфейс мапперов
 * @param <T>
 * @param <B>
 */
public interface Mapper<T,B> {

    B toDbo(T dto);

    T toDto(B dbo);

    default List<T> toDto(List<B> dboList) {
        List<T> result = new ArrayList<>();
        dboList.forEach(dbo->result.add(toDto(dbo)));
        return result;
    }

    default List<B> toDbo(List<T> dtoList) {
        List<B> result = new ArrayList<>();
        dtoList.forEach(dto->result.add(toDbo(dto)));
        return result;
    }

}
