package com.example.memorybootcamp.generators;

import java.util.List;

/** Interface for generators. */
public interface IGenerator {

    /** Method for generating objects. */
    default List<?> generateSequence(int length) {
        return null;
    }
}
