package com.tm.web.controller.common;

import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.model.DataNode;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.GetNodesTreeBody;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.DataNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/node")
public class DataNodeController extends BaseController {

    @Autowired
    private DataNodeMapper dataNodeMapper;

    @Autowired
    private DataNodeService dataNodeService;

    @PostMapping(value = "/getNodesTree", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getNodesTree(@RequestBody @Valid GetNodesTreeBody body) {
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        List<DataNode> nodesTree = dataNodeMapper.getNodesTree(body);
        for (DataNode dataNode : nodesTree) {
            if(dataNode.getIsFolder().equals(1)) {
                GetNodesTreeBody getNodesTreeBody = new GetNodesTreeBody();
                getNodesTreeBody.setProjectId(dataNode.getProjectId());
                getNodesTreeBody.setDataTypeId(dataNode.getDataTypeId());
                getNodesTreeBody.setLevel(dataNode.getLevel());
                getNodesTreeBody.setParentId(dataNode.getId());
                dataNode.setChildCasesCount(dataNodeMapper.countSubLeafNode(getNodesTreeBody));
            }
        }
        return ResultUtils.success(nodesTree);
    }

    @PostMapping(value = "/addNode", produces = {"application/json;charset=UTF-8"})
    public BaseResponse saveNode(@RequestBody @Valid SaveNodeBody body) {
        User user = this.getLoginUser();
        if(StringUtils.isBlank(body.getName())) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(body.getIsFolder() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(body.getParentId() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        return dataNodeService.saveNode(body, user);
    }

    @PostMapping(value = "/deleteNode", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteNode(@RequestBody @Valid SaveNodeBody body) {
        if(body.getId() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        User user = getLoginUser();
        log.info("user {} delete node {}", user.getUsername(), body.getId());
        return dataNodeService.deleteNode(body);
    }

    @PostMapping(value = "/updateNode", produces = {"application/json;charset=UTF-8"})
    public BaseResponse updateNode(@RequestBody @Valid SaveNodeBody body) {
        if(body.getId() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(StringUtils.isBlank(body.getName())) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        User user = this.getLoginUser();
        return dataNodeService.saveNode(body, user);
    }

    @PostMapping(value = "/moveNode", produces = {"application/json;charset=UTF-8"})
    public BaseResponse moveNode(@RequestBody @Valid SaveNodeBody body) {
        if(body.getId() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(body.getPrevId() == null || body.getPrevId() < 0) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(body.getParentId() == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        User user = this.getLoginUser();
        log.info("user {} move node {}, parentId: {}, prevId: {}", user.getUsername(), body.getId(), body.getParentId(), body.getPrevId());
        return dataNodeService.moveNode(body, user);
    }

    @PostMapping(value = "/copyNode", produces = {"application/json;charset=UTF-8"})
    public BaseResponse copyNode(@RequestBody @Valid SaveNodeBody body) {
        if(body.getId() == null || (body.getPrevId() == null && body.getParentId() == null)) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        User user = this.getLoginUser();
        log.info("user {} copy node {}", user.getUsername(), body.getId());
        return dataNodeService.copyNode(body, user);
    }

    @PostMapping(value = "/queryNodeList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryNodeList(@RequestBody @Valid CommonTableQueryBody body) {
        if(DataTypeEnum.PLATFORM_API.value() == body.getDataTypeId()) {
            body.setProjectId(57);
        }
        if(body.getParentId() != null) {
            if(body.getParentId() == 1) {
                body.setParentX("parent1");
            }else{
                DataNode dataNode = dataNodeMapper.selectByPrimaryKey(body.getParentId(), body.getDataTypeId());
                if(dataNode == null) {
                    body.setParentX("parent1");
                    body.setParentId(1);
                }else{
                    body.setParentX("parent" + dataNode.getLevel());
                }
            }
        }

        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        response.setRows(dataNodeMapper.queryList(body));
        response.setTotal(dataNodeMapper.countList(body));

        return ResultUtils.success(response);
    }
}
