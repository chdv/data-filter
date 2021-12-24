package com.dch.data.filter;

import lombok.Getter;
import lombok.NonNull;

/**
 * Операции для фильтрации содержимого коллекции.
 */

public enum FilterOperator {

    IN("in", FilterSorter.DEFAULT_DELIMETER),
    NULL("null"),
    NOT_NULL("!null"),
    EQ("eq"),
    NE("!eq"),
    GT("gt"),
    LT("lt"),
    GTE("gte"),
    LTE("lte"),
    BETWEEN("between", FilterSorter.DEFAULT_DELIMETER),
    LIKE("like");

    @Getter
    private String code;

    @Getter
    private String delimeter;

    FilterOperator(String code, String delimeter) {
        this.code = code;
        this.delimeter = delimeter;
    }

    FilterOperator(String code) {
        this(code, null);
    }

    public static FilterOperator fromCode(@NonNull String c) {
        for (FilterOperator fs : FilterOperator.values()) {
            if (fs.getCode().equals(c)) {
                return fs;
            }
        }
        return null;
    }

    public boolean needSplit() {
        return delimeter != null;
    }

    public String[] splitValue(String value) {
        if (!needSplit())
            throw new IllegalStateException("No need split!");

        return value.split(delimeter);
    }
}
