package com.tm.common.entities.base;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PagingBody {
    @Min(1)
    @Max(1000)
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum = 1;

    @Min(10)
    @Max(1000)
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize = 10;

    private Integer offset = 0;

    @Pattern(regexp = "[a-zA-Z_]{0,30}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String order;

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String sort = "desc";

    public void setSort(String sort) {
        if(sort != null && sort.equals("ascend")) {
            sort = "asc";
        }else if(sort != null && sort.equals("descend")) {
            sort = "desc";
        }else if(StringUtils.isBlank(sort)) {
            sort = "desc";
        }
        this.sort = sort;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        this.offset = (this.getPageNum()-1) * this.pageSize;
    }
}
