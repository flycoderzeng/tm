package com.tm.lc.hooks;

import com.tm.common.utils.DateUtils;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.core.lifecycle.LifeCycleHook;
import com.yahoo.elide.core.security.RequestScope;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
public class EntityPublicModifyHook implements LifeCycleHook<CommonSixItemsElideModel> {
    @Override
    public void execute(LifeCycleHookBinding.Operation operation, LifeCycleHookBinding.TransactionPhase transactionPhase,
                        CommonSixItemsElideModel model, RequestScope requestScope, Optional optional) {
        model.setLastModifyTime(DateFormatUtils.format(new Date(), DateUtils.DATE_PATTERN_DEFAULT));
        model.setLastModifyUser(requestScope.getUser().getName());
    }
}
