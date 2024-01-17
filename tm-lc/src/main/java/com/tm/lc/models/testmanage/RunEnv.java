package com.tm.lc.models.testmanage;

import com.tm.lc.convert.EncryptDecrypt;
import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

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
    @Column(name = "http_ip")
    private String httpIp;
    @Column(name = "http_port")
    private String httpPort;
    @Column(name = "db_username")
    private String dbUsername;
    @Lob
    @Column(name = "db_password", columnDefinition = "TEXT")
    @Convert(converter = EncryptDecrypt.class)
    private String dbPassword;
    @Column(name = "db_ip")
    private String dbIp;
    @Column(name = "db_port")
    private String dbPort;
    @Column(name = "db_type")
    private Integer dbType;
    @Column(name = "db_schema_name")
    private String dbSchemaName;
}
