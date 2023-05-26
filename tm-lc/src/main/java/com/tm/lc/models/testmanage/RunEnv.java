package com.tm.lc.models.testmanage;

import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.*;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "run_env")
@Include(name="run_env")
@Entity
@Data
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@UpdatePermission(expression = "user is a common admin")
@CreatePermission(expression = "user is a common admin")
@DeletePermission(expression = "user is a root admin")
public class RunEnv extends CommonSixItemsElideModel {
    private String name;
    private String description;
}
