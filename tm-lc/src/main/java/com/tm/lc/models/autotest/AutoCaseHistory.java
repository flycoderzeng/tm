package com.tm.lc.models.autotest;

import com.tm.lc.convert.DateAndString;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.UpdatePermission;
import lombok.Data;

import javax.persistence.*;

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
    private Integer autoCaseId;
    @Lob
    private String steps;
    @Lob
    private String groupVariables;

    @Column(name = "add_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    private String addTime;

    private String addUser;
}
