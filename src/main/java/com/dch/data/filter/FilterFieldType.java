package com.dch.data.filter;

import com.dch.data.filter.types.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilterFieldType {

    private static final List<FilterFieldType> values = new ArrayList<>();

    public static final FilterFieldType TIMESTAMP = new FilterFieldType("timestamp", new TimestampFieldOperator());
    public static final FilterFieldType TIMESTAMP_LOCAL = new FilterFieldType("timestampLocal", new TimestampLocalFieldOperator());
    public static final FilterFieldType TIMESTAMP_INSTANT = new FilterFieldType("timestampInstant", new TimestampInstantFieldOperator());
    public static final FilterFieldType STRING = new FilterFieldType("string", new StringFieldOperator());
    public static final FilterFieldType NUMBER = new FilterFieldType("number", new NumberFieldOperator());
    public static final FilterFieldType BOOLEAN = new FilterFieldType("boolean", new BooleanFieldOperator());

    static {
        values.add(TIMESTAMP);
        values.add(TIMESTAMP_LOCAL);
        values.add(TIMESTAMP_INSTANT);
        values.add(STRING);
        values.add(NUMBER);
        values.add(BOOLEAN);
    }

    @Getter
    private String code;

    @Getter
    private FieldTypeOperator typeOperator;

    public FilterFieldType(String code, FieldTypeOperator typeOperator) {
        this.code = code;
        this.typeOperator = typeOperator;
    }

    public static FilterFieldType fromCode(String code) {
        for(FilterFieldType type : values){
            if(type.code.equals(code))
                return type;
        }

        throw new IllegalArgumentException("Error code: " + code);
    }
}
