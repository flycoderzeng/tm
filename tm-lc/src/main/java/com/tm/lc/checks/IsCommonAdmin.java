package com.tm.lc.checks;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.security.User;
import com.yahoo.elide.core.security.checks.UserCheck;

@SecurityCheck(IsCommonAdmin.PRINCIPAL_IS_COMMON_ADMIN)
public class IsCommonAdmin extends UserCheck {
    public static final String PRINCIPAL_IS_COMMON_ADMIN = "user is a common admin";

    @Override
    public boolean ok(User user) {
        return user.isInRole("ROLE_ROOT_ADMIN");
    }
}
