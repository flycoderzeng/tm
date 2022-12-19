package com.tm.lc.checks;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.security.User;
import com.yahoo.elide.core.security.checks.UserCheck;

@SecurityCheck(IsRootAdmin.PRINCIPAL_IS_ROOT_ADMIN)
public class IsRootAdmin extends UserCheck {
    public static final String PRINCIPAL_IS_ROOT_ADMIN = "user is a root admin";

    @Override
    public boolean ok(User user) {
        return user.isInRole("ROLE_ROOT_ADMIN");
    }
}
