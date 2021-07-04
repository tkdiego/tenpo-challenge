package com.taka.tenpo.domain.recorder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse<T> {

    private List<T> items;
    private int pages;
    private int currentPage;
    private int totalItems;
}
