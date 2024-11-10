package com.tm.web.controller.system;


import com.tm.common.base.mapper.MenuMapper;
import com.tm.common.base.model.Menu;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {
    private final MenuMapper menuMapper;

    @Inject
    public MenuController(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @GetMapping(value = "/getAllMenuTree")
    public BaseResponse getAllMenuTree() {
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
}
