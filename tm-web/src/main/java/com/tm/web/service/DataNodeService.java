package com.tm.web.service;

import com.tm.common.base.mapper.*;
import com.tm.common.base.model.*;
import com.tm.common.entities.autotest.request.GetNodesTreeBody;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Stack;


@Slf4j
@Service("dataNodeService")
public class DataNodeService {

    @Autowired
    private DataNodeMapper dataNodeMapper;
    @Autowired
    private AutoCaseMapper autoCaseMapper;
    @Autowired
    private PlatformApiMapper platformApiMapper;
    @Autowired
    private GlobalVariableMapper globalVariableMapper;
    @Autowired
    private AutoScriptMapper autoScriptMapper;
    @Autowired
    private AutoPlanMapper autoPlanMapper;
    @Autowired
    private HttpApiMapper httpApiMapper;

    @Autowired
    private AutoCaseService autoCaseService;
    @Autowired
    private HttpApiService httpApiService;
    @Autowired
    private AutoShellService autoShellService;
    @Autowired
    private GlobalVariableService globalVariableService;
    @Autowired
    private PlatformApiService platformApiService;
    @Autowired
    private AutoPlanService autoPlanService;

    public BaseResponse deleteNode(SaveNodeBody body) {
        if(body.getId().equals(1)) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        DataNode dataNode = dataNodeMapper.selectByPrimaryKey(body.getId(), body.getDataTypeId());
        if(dataNode == null) {
            return ResultUtils.success();
        }
        int r = dataNodeMapper.deleteByPrimaryKey(body.getId(), body.getDataTypeId(), dataNode.getLevel());

        return ResultUtils.success(r);
    }

    public BaseResponse saveNode(SaveNodeBody body, User loginUser) {
        DataNode dataNode = null;
        DataNode parentDataNode = null;
        DataNode prevNode = null;
        boolean isNew = false;
        if(body.getParentId().equals(1)) {
            parentDataNode = dataNodeMapper.selectByPrimaryKey(1, 1);
        }else{
            parentDataNode = dataNodeMapper.selectByPrimaryKey(body.getParentId(), body.getDataTypeId());
        }
        if(parentDataNode == null) {
            log.error("父亲节点没有找到");
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(parentDataNode.getLevel().equals(10)) {
            return ResultUtils.error(ResultCodeEnum.NODE_TREE_LEVEL_OVERFLOW_ERROR);
        }

        // -1 代表是修改节点 不移动位置 0 代表排在首位
        if(body.getPrevId() != null && body.getPrevId() > 0) {
            prevNode = dataNodeMapper.selectByPrimaryKey(body.getPrevId(), body.getDataTypeId());
            if(prevNode == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        }
        if(body.getId() == null || body.getId() < 1) {
            dataNode = new DataNode();
        }else{
            dataNode = dataNodeMapper.selectByPrimaryKey(body.getId(), body.getDataTypeId());
            if(dataNode == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        }

        if(body.getDataTypeId().equals(DataTypeEnum.AUTO_CASE.value())) {
            AutoCase autoCase = null;
            if(body.getId() != null && body.getId() > 0) {
                autoCase = autoCaseMapper.selectByPrimaryId(body.getId());
                if(autoCase == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                autoCase = new AutoCase();
                autoCase.setType(0);
                autoCaseMapper.insertBySelective(autoCase);
                dataNode.setId(autoCase.getId());
                isNew = true;
            }
        }else if(body.getDataTypeId().equals(DataTypeEnum.PLATFORM_API.value())) {
            PlatformApi platformApi = null;
            if(body.getId() != null && body.getId() > 0) {
                platformApi = platformApiMapper.selectByPrimaryId(body.getId());
                if(platformApi == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                platformApi = new PlatformApi();
                platformApiMapper.insertBySelective(platformApi);
                dataNode.setId(platformApi.getId());
                isNew = true;
            }
        }else if(body.getDataTypeId().equals(DataTypeEnum.GLOBAL_VARIABLE.value())) {
            GlobalVariable globalVariable = null;
            if(body.getId() != null && body.getId() > 0) {
                globalVariable = globalVariableMapper.selectByPrimaryId(body.getId());
                if(globalVariable == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                globalVariable = new GlobalVariable();
                globalVariableMapper.insertBySelective(globalVariable);
                dataNode.setId(globalVariable.getId());
                isNew = true;
            }
        }else if(body.getDataTypeId().equals(DataTypeEnum.AUTO_SHELL.value())) {
            AutoScript autoScript = null;
            if(body.getId() != null && body.getId() > 0) {
                autoScript = autoScriptMapper.selectByPrimaryId(body.getId());
                if(autoScript == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                autoScript = new AutoScript();
                autoScript.setType(AutoScript.AutoScriptTypeShell);
                autoScriptMapper.insertBySelective(autoScript);
                dataNode.setId(autoScript.getId());
                isNew = true;
            }
        }else if (body.getDataTypeId().equals(DataTypeEnum.AUTO_PLAN.value())) {
            AutoPlan autoPlan = null;
            if(body.getId() != null && body.getId() > 0) {
                autoPlan = autoPlanMapper.selectByPrimaryId(body.getId());
                if(autoPlan == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                autoPlan = new AutoPlan();
                autoPlan.setType(1);
                autoPlanMapper.insertBySelective(autoPlan);
                dataNode.setId(autoPlan.getId());
                isNew = true;
            }
        }else if (body.getDataTypeId().equals(DataTypeEnum.APP_API.value())) {
            HttpApi httpApi = null;
            if(body.getId() != null && body.getId() > 0) {
                httpApi = httpApiMapper.selectByPrimaryId(body.getId());
                if(httpApi == null) {
                    return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
                }
            } else {
                httpApi = new HttpApi();
                httpApiMapper.insertBySelective(httpApi);
                dataNode.setId(httpApi.getId());
                isNew = true;
            }
        }

        dataNode.setDataTypeId(body.getDataTypeId());
        dataNode.setName(body.getName());
        dataNode.setDescription(body.getDescription());
        dataNode.setProjectId(body.getProjectId());
        dataNode.setIsFolder(body.getIsFolder());
        dataNode.setParentId(body.getParentId());
        dataNode.setLevel(parentDataNode.getLevel()+1);
        if(body.getId() != null && body.getId() > 0) {
            dataNode.setLastModifyUserId(loginUser.getId());
            dataNode.setLastModifyTime(new Date());
        }else{
            dataNode.setAddUserId(loginUser.getId());
            dataNode.setAddTime(new Date());
        }
        if(body.getPrevId() != null && !body.getPrevId().equals(-1)) {
            if(prevNode == null) {
                dataNode.setSeq(1);
            }else if(prevNode != null){
                dataNode.setSeq(prevNode.getSeq()+1);
            }
        }
        if(body.getPrevId() != null && body.getPrevId().equals(0)) {
            dataNode.setSeq(1);
        }
        BaseResponse response = setParentI(dataNode, parentDataNode);
        if(!response.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            return response;
        }
        if(isNew && dataNode.getSeq() == null) {
            int seq = dataNodeMapper.getMaxSeq(parentDataNode.getId(), body.getDataTypeId());
            dataNode.setSeq(seq+1);
        }
        if(isNew) {
            dataNodeMapper.insertBySelective(dataNode);
        }else{
            dataNodeMapper.updateBySelective(dataNode);
        }
        //刷新seq
        if(body.getPrevId() != null && !body.getPrevId().equals(-1)) {
            dataNodeMapper.refreshSeq(dataNode);
        }
        return ResultUtils.success(dataNode.getId());
    }

    private BaseResponse setParentI(DataNode dataNode, DataNode parentDataNode) {
        Class dataNodeClass = dataNode.getClass();
        int i = 1;
        for(;i < 11; i++) {
            String setMethodName = "setParent" + i;
            String getMethodName = "getParent" + i;
            try {
                Method setMethod = dataNodeClass.getMethod(setMethodName, new Class[]{Integer.class});
                Method getMethod = dataNodeClass.getMethod(getMethodName, new Class[]{});
                Integer value = (Integer) getMethod.invoke(parentDataNode, new Object[]{});
                Integer parentValue = value == null ? parentDataNode.getId() : value;
                if(i == 1) {
                    parentValue = 1;
                }else if(i >= dataNode.getLevel()) {
                    parentValue = 0;
                }
                setMethod.invoke(dataNode, parentValue);
            } catch (NoSuchMethodException e) {
                log.error("get method error: {}", e);
                return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
            } catch (IllegalAccessException e) {
                log.error("set method error: {}", e);
                return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
            } catch (InvocationTargetException e) {
                log.error("set method error: {}", e);
                return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
            }
        }
        String setMethodName = "setParent" + parentDataNode.getLevel();
        Method setMethod = null;
        try {
            setMethod = dataNodeClass.getMethod(setMethodName, new Class[]{Integer.class});
            setMethod.invoke(dataNode, parentDataNode.getId());
        } catch (NoSuchMethodException e) {
            log.error("get method error: {}", e);
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
        } catch (InvocationTargetException e) {
            log.error("set method error: {}", e);
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
        } catch (IllegalAccessException e) {
            log.error("set method error: {}", e);
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
        }

        return ResultUtils.success();
    }

    public BaseResponse moveNode(SaveNodeBody body, User loginUser) {
        DataNode dataNode = null;
        DataNode parentDataNode = null;
        DataNode prevNode = null;
        dataNode = dataNodeMapper.selectByPrimaryKey(body.getId(), body.getDataTypeId());
        if(dataNode == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(body.getParentId().equals(1)) {
            parentDataNode = dataNodeMapper.selectByPrimaryKey(1, 1);
        }else{
            parentDataNode = dataNodeMapper.selectByPrimaryKey(body.getParentId(), body.getDataTypeId());
        }
        if(parentDataNode == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        if(parentDataNode.getLevel().equals(11)) {
            return ResultUtils.error(ResultCodeEnum.NODE_TREE_LEVEL_OVERFLOW_ERROR);
        }

        // -1 代表是修改节点 不移动位置 0 代表排在首位
        if(body.getPrevId() != null && body.getPrevId() > 0) {
            prevNode = dataNodeMapper.selectByPrimaryKey(body.getPrevId(), body.getDataTypeId());
            if(prevNode == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        }
        dataNode.setLevel(parentDataNode.getLevel()+1);
        BaseResponse response = setParentI(dataNode, parentDataNode);
        if(!response.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            return response;
        }
        if(body.getPrevId() != null && body.getPrevId().equals(0)) {
            dataNode.setSeq(1);
        }
        if(prevNode != null) {
            dataNode.setSeq(prevNode.getSeq()+1);
        }
        dataNode.setParentId(parentDataNode.getId());
        dataNodeMapper.updateBySelective(dataNode);
        dataNodeMapper.refreshSeq(dataNode);

        // 如果移动的是目录，需要更新目录下子节点的level和parenti
        if(dataNode.getIsFolder().equals(1)) {
            GetNodesTreeBody getNodesTreeBody = new GetNodesTreeBody();
            getNodesTreeBody.setProjectId(body.getProjectId());
            getNodesTreeBody.setDataTypeId(body.getDataTypeId());
            parentDataNode = dataNode;

            Stack<Integer> stack = new Stack<>();
            stack.add(dataNode.getId());
            while (!stack.isEmpty()) {
                Integer id = stack.pop();
                getNodesTreeBody.setParentId(id);
                List<DataNode> nodeList = dataNodeMapper.getNodesTree(getNodesTreeBody);
                if(nodeList != null && !nodeList.isEmpty()) {
                    for (DataNode node : nodeList) {
                        node.setLevel(parentDataNode.getLevel()+1);
                        response = setParentI(node, parentDataNode);
                        if(!response.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
                            return response;
                        }
                        dataNodeMapper.updateBySelective(node);
                        if(node.getIsFolder().equals(1)) {
                            stack.add(node.getId());
                        }
                    }
                }
            }
        }

        return ResultUtils.success();
    }

    public BaseResponse copyNode(SaveNodeBody body, User loginUser) {
        DataNode copyNode = dataNodeMapper.selectByPrimaryKey(body.getId(), body.getDataTypeId());
        if(copyNode == null || (body.getPrevId() == null && body.getParentId() == null)) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }

        GetNodesTreeBody getNodesTreeBody = new GetNodesTreeBody();
        getNodesTreeBody.setProjectId(body.getProjectId());
        getNodesTreeBody.setDataTypeId(body.getDataTypeId());
        if(copyNode.getIsFolder().equals(1)) {
            getNodesTreeBody.setParentId(copyNode.getId());
            getNodesTreeBody.setLevel(copyNode.getLevel());
            int count = dataNodeMapper.countSubLeafNode(getNodesTreeBody);
            if(count > 500) {
                return ResultUtils.error(ResultCodeEnum.COPY_NODE_OVERFLOW_ERROR);
            }
        }
        int currParentId = 1;
        if(body.getParentId() != null) {
            currParentId = body.getParentId();
        }else if(body.getPrevId() != null) {
            DataNode prevNode = dataNodeMapper.selectByPrimaryKey(body.getPrevId(), body.getDataTypeId());
            if(prevNode == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
            currParentId = prevNode.getParentId();
        }

        Stack<Integer> stack = new Stack<>();
        stack.add(body.getId());
        boolean isFirst = true;
        while (!stack.isEmpty()) {
            Integer id = stack.pop();
            copyNode = dataNodeMapper.selectByPrimaryKey(id, body.getDataTypeId());
            SaveNodeBody copyBody = new SaveNodeBody();
            copyBody.setDataTypeId(body.getDataTypeId());
            copyBody.setProjectId(body.getProjectId());
            copyBody.setIsFolder(copyNode.getIsFolder());
            copyBody.setParentId(currParentId);
            copyBody.setName(copyNode.getName());
            copyBody.setDescription(copyNode.getDescription());
            copyBody.setId(null);
            copyBody.setCopyId(copyNode.getId());
            if(isFirst) {
                copyBody.setPrevId(body.getPrevId());
                copyBody.setName("copy_" + copyBody.getName());
                isFirst = false;
            }

            BaseResponse response = saveNode(copyBody, loginUser);
            copyBody.setId((Integer) response.getData());
            if(!response.getCode().equals(0)) {
                return response;
            }

            switch (DataTypeEnum.get(body.getDataTypeId())) {
                case APP_API:
                    httpApiService.copy(copyBody);
                    break;
                case AUTO_SHELL:
                    autoShellService.copy(copyBody);
                    break;
                case GLOBAL_VARIABLE:
                    globalVariableService.copy(copyBody);
                    break;
                case PLATFORM_API:
                    platformApiService.copy(copyBody);
                    break;
                case AUTO_CASE:
                    autoCaseService.copy(copyBody);
                    break;
                case AUTO_PLAN:
                    autoPlanService.copy(copyBody);
                    break;
                default:
                    return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
            }
            if(copyNode.getIsFolder().equals(1)) {
                currParentId = copyBody.getId();
                getNodesTreeBody.setParentId(copyNode.getId());
                List<DataNode> nodeList = dataNodeMapper.getNodesTree(getNodesTreeBody);
                if(nodeList != null && !nodeList.isEmpty()) {
                    nodeList.forEach(node -> {
                        stack.add(node.getId());
                    });
                }
            }
        }

        return ResultUtils.success();
    }

}
