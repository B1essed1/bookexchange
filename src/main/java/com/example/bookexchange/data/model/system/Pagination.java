package com.example.bookexchange.data.model.system;

import lombok.Data;

/**
 * Used for pagination.
 */

@Data
public class Pagination<T> {
    private Integer page;
    private Integer limit;
    private String order;
    private String type;
    private T search;
}
