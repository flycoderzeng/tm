package com.tm.lc.models;

import com.tm.lc.convert.DateAndString;
import lombok.Data;

import javax.persistence.*;


@MappedSuperclass
@Data
public abstract class CommonSixItemsElideModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "add_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    protected String addTime;

    @Column(name = "last_modify_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    protected String lastModifyTime;

    protected String addUser;
    protected String lastModifyUser;
    // 0-正常 1-删除
    protected Integer status = 0;
}
