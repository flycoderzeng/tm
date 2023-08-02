package com.tm.lc.models.testmanage;

import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.tm.lc.models.system.Project;
import com.yahoo.elide.annotation.ComputedAttribute;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.core.RequestScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "tags")
@Include(name="tags")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
public class Tag extends CommonSixItemsElideModel {
    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName="id")
    private Project project;

    @Transient
    @ComputedAttribute
    public String getProjectName(RequestScope requestScope) {
        return project == null ? null : project.getName();
    }

    @Transient
    @ComputedAttribute
    public Integer getProjectId(RequestScope requestScope) {
        return project == null ? null : project.getId();
    }

}
