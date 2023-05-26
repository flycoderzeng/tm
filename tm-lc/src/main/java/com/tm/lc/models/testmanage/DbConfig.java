package com.tm.lc.models.testmanage;

import com.tm.lc.convert.EncryptDecrypt;
import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.ComputedAttribute;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.core.RequestScope;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "db_config")
@Include(name="db_config")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
public class DbConfig extends CommonSixItemsElideModel {
    @Column(name = "db_name")
    private String dbName;

    @Lob
    @Column(name = "username", columnDefinition = "TEXT")
    @Convert(converter = EncryptDecrypt.class)
    private String username;

    @Lob
    @Column(name = "password", columnDefinition = "TEXT")
    @Convert(converter = EncryptDecrypt.class)
    private String password;

    private String ip;
    private String port;
    private Integer type;
    @OneToOne
    @JoinColumn(name = "env_id", referencedColumnName="id")
    private RunEnv runEnv;

    @Transient
    @ComputedAttribute
    public String getEnvName(RequestScope requestScope) {
        return runEnv == null ? null : runEnv.getName();
    }

    @Transient
    @ComputedAttribute
    public Integer getEnvId(RequestScope requestScope) {
        return runEnv == null ? null : runEnv.getId();
    }
}
