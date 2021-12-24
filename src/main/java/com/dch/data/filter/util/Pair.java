package com.dch.data.filter.util;

import lombok.Value;

@Value
public class Pair<K,V> {

    private K key;

    private V value;

}