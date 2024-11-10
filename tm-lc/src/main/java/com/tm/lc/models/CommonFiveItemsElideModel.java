package com.tm.lc.models;

import com.tm.lc.convert.DateAndString;
import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class CommonFiveItemsElideModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "add_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    protected String addTime;

    @Column(name = "last_modify_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    protected String lastModifyTime;
    @Column(name = "add_user")
    protected String addUser;
    @Column(name = "last_modify_user")
    protected String lastModifyUser;
}
