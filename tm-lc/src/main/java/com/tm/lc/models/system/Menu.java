package com.tm.lc.models.system;

import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.*;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "menu")
@Include(name="menu")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
@UpdatePermission(expression = "user is a common admin")
@CreatePermission(expression = "user is a common admin")
public class Menu extends CommonSixItemsElideModel {
    @Column(name = "menu_name")
    private String menuName;
    private String url;
    @Column(name = "parent_id")
    private Integer parentId;
    @Exclude
    private String icon;
    private Integer seq;
    @Exclude
    private Integer level;
}
