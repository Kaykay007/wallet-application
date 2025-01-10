package com.korede.wallet.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private List<T> data;
    private Long totalRecords;
    private int currentPage;
    private int totalPages;

    public PageResult() {
    }

    public PageResult(List<T> data, Long totalRecords, int totalPages, int currentPage) {
        this.data = data;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

}