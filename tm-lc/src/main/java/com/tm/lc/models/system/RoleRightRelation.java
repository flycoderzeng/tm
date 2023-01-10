package com.tm.lc.models.system;

import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonFiveItemsElideModel;
import com.yahoo.elide.annotation.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "role_right")
@Include(name="role_right")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
@UpdatePermission(expression = "user is a root admin")
@CreatePermission(expression = "user is a root admin")
public class RoleRightRelation extends CommonFiveItemsElideModel {

    @ManyToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "right_id",referencedColumnName = "id")
    private Right right;
}
