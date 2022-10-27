package com.tm.common.entities.base;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

import static com.tm.common.entities.base.FilterCondition.LIKE_OPERATOR;


@Data
public class CommonTableQueryBody extends PagingBody {
    List<FilterCondition> filterConditionList;
    @Pattern(regexp = "or|and", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String linkOperator = "or";
    private Integer dataTypeId;
    private Integer projectId;
    // 0-所有 1-我的
    private Integer area;
    private Integer loginUserId;
    private String loginUsername;
    private Integer planId;
    private Integer planResultId;
    private Integer caseId;
    private Integer groupNo;
    private String stepKey;
    private String tableSuffix;
    private Integer parentId;
    private Integer planOrCaseId;
    private Integer fromType;

    public List<FilterCondition> getFilterConditionList() {
        return filterConditionList;
    }

    public void setFilterConditionList(List<FilterCondition> filterConditionList) {
        this.filterConditionList = filterConditionList;
        for (FilterCondition filterCondition : this.filterConditionList) {
            if(filterCondition.getOperator().equals(LIKE_OPERATOR)) {
                filterCondition.setValue("%" + filterCondition.getValue() + "%");
            }
        }
    }
}
