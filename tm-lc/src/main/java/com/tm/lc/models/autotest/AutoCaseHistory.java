package com.tm.lc.models.autotest;

import com.tm.lc.convert.DateAndString;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.UpdatePermission;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "auto_case_history")
@Include(name="auto_case_history")
@Data
@UpdatePermission(expression = "user is a root admin")
@DeletePermission(expression = "user is a root admin")
public class AutoCaseHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "auto_case_id")
    private Integer autoCaseId;
    @Lob
    private String steps;
    @Lob
    @Column(name = "group_variables")
    private String groupVariables;

    @Column(name = "add_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    private String addTime;
    @Column(name = "add_user")
    private String addUser;
}
