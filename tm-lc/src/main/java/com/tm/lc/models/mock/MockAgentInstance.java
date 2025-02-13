package com.tm.lc.models.mock;

import com.tm.lc.convert.DateAndString;
import com.yahoo.elide.annotation.CreatePermission;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.UpdatePermission;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mock_agent_instances")
@Include(name="mock_agent_instance")
@Data
@UpdatePermission(expression = "user is a root admin")
@CreatePermission(expression = "user is a root admin")
@DeletePermission(expression = "user is a root admin")
public class MockAgentInstance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "application_name")
    private String applicationName;
    private String ip;
    private Integer port;
    private String name;
    private String description;

    @Column(name = "first_register_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    private String firstRegisterTime;

    @Column(name = "last_register_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    private String lastRegisterTime;

    private Integer status;
}
