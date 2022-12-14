package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;
@Data
public class DistributedLock {
    private Integer id;
    private String name;
    private Date addTime = new Date();

    public DistributedLock(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
