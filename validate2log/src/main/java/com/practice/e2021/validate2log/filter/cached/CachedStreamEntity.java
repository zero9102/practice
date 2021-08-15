package com.practice.e2021.validate2log.filter.cached;

public interface CachedStreamEntity {

    CachedStream getCachedStream();

    void flushStream();
}
