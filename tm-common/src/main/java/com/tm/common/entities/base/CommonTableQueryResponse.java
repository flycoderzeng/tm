package com.tm.common.entities.base;

import java.util.List;

public class CommonTableQueryResponse<T> {
    private List<T> rows;
    private Integer total;

    public CommonTableQueryResponse() {}

    public CommonTableQueryResponse(List<T> rows, Integer total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
