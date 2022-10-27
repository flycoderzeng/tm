package com.tm.web.service;

import com.google.gson.Gson;
import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.model.DataNode;
import com.tm.common.entities.common.BaseIdNameModel;
import com.tm.common.entities.autotest.request.GetNodesTreeBody;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.base.mapper.PlatformApiMapper;
import com.tm.common.base.model.PlatformApi;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.ParameterDefineRow;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.utils.ResultUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("platformApiService")
public class PlatformApiService extends BaseService {
    @Autowired
    private PlatformApiMapper platformApiMapper;
    @Autowired
    private DataNodeMapper dataNodeMapper;

    public BaseResponse copy(SaveNodeBody body) {
        return ResultUtils.success();
    }

    public BaseResponse update(PlatformApi platformApi, User user) {
        platformApiMapper.updateBySelective(platformApi);
        return updateNode4CommonFields(user, platformApi, DataTypeEnum.PLATFORM_API);
    }

    public PlatformApi load(Integer id) {
        Gson gson = new Gson();
        PlatformApi platformApi = platformApiMapper.selectByPrimaryId(id);
        if(StringUtils.isNotBlank(platformApi.getDefineJson())) {
            ParameterDefineRow[] rows = gson.fromJson(platformApi.getDefineJson(), ParameterDefineRow[].class);
            platformApi.setRows(Arrays.asList(rows));
        }
        return platformApi;
    }

    public List getTree() {
        GetNodesTreeBody body = new GetNodesTreeBody();
        body.setDataTypeId(DataTypeEnum.PLATFORM_API.value());
        body.setParentId(1);
        List<DataNode> list = dataNodeMapper.getNodesTree(body);
        List<PlatformApiMenuTreeNode> treeNodes = new ArrayList<>();
        list.forEach(item -> {
            PlatformApiMenuTreeNode treeNode = new PlatformApiMenuTreeNode(item.getId(), item.getName(), new ArrayList<PlatformApiMenuTreeNode>());
            treeNodes.add(treeNode);
            body.setParentId(item.getId());
            List<DataNode> children = dataNodeMapper.getNodesTree(body);
            children.forEach(child -> {
                PlatformApiMenuTreeNode subNode = new PlatformApiMenuTreeNode(child.getId(), child.getName(), new ArrayList<PlatformApiMenuTreeNode>());
                treeNode.getChildren().add(subNode);
            });
        });
        return treeNodes;
    }

    @Data
    public static class PlatformApiMenuTreeNode extends BaseIdNameModel {
        private List<PlatformApiMenuTreeNode> children;
        public PlatformApiMenuTreeNode(Integer id, String name, List children) {
            this.setId(id);
            this.setName(name);
            this.setChildren(children);
        }
    }
}
