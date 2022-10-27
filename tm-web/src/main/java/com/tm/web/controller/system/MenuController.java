package com.tm.web.controller.system;


import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.MenuMapper;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuMapper menuMapper;


    @GetMapping(value = "/getAllMenuTree")
    public BaseResponse getAllMenuTree() {
        logger.info("get all menu tree");
        List<Menu> list = menuMapper.selectMenuTree(0);
        for (Menu mockMenu : list) {
            List<Menu> children = menuMapper.selectMenuTree(mockMenu.getId());
            mockMenu.setChildren(children);
            for (Menu child : children) {
                List<Menu> children1 = menuMapper.selectMenuTree(child.getId());
                child.setChildren(children1);
            }
        }
        return ResultUtils.success(list);
    }

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(menuMapper.deleteByPrimaryKey(body.getId()));
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        Menu mockMenu = menuMapper.findById(body.getId());
        return ResultUtils.success(mockMenu);
    }

    @PostMapping(value = "/getChildren", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getChildren(@RequestBody @Valid IdBody body) {
        List<Menu> children = menuMapper.selectMenuTree(body.getId());
        return ResultUtils.success(children);
    }

    @PostMapping(value = "/queryMenuList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryMenuList(@RequestBody @Valid CommonTableQueryBody body) {
        CommonTableQueryResponse<Menu> response = new CommonTableQueryResponse<>();
        response.setRows(menuMapper.queryList(body));
        response.setTotal(menuMapper.countList(body));

        return ResultUtils.success(response);
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody Menu body) {
        Menu mockMenu;
        User user = this.getLoginUser();
        if(body.getId() != null) {
            mockMenu = menuMapper.findById(body.getId());
            if(mockMenu == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        } else {
            mockMenu = new Menu();
            mockMenu.setAddTime(new Date());
            mockMenu.setAddUser(user.getUsername());
        }
        mockMenu.setLastModifyTime(new Date());
        mockMenu.setLastModifyUser(user.getUsername());
        mockMenu.setMenuName(body.getMenuName());
        mockMenu.setSeq(body.getSeq());
        mockMenu.setUrl(body.getUrl());
        if(body.getParentId() != null && body.getParentId() > 0) {
            mockMenu.setParentId(body.getParentId());
            Menu parent = menuMapper.findById(body.getParentId());
            if(parent == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
            mockMenu.setLevel(parent.getLevel()+1);
        }else{
            mockMenu.setParentId(0);
            mockMenu.setLevel(1);
        }
        if(body.getId() != null) {
            menuMapper.updateBySelective(mockMenu);
        } else {
            menuMapper.insertBySelective(mockMenu);
        }
        return ResultUtils.success(mockMenu.getId());
    }
}
