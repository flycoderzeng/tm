package com.tm.common.base.mapper;

import com.tm.common.base.model.DataNode;
import com.tm.common.entities.autotest.request.GetNodesTreeBody;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataNodeMapper {
    List<DataNode> getNodesTree(GetNodesTreeBody body);
    List<DataNode> getChildNodesWithParentI(@Param("dataTypeId") Integer dataTypeId, @Param("parentId") Integer parentId, @Param("parentI") Integer parentI);
    int insertBySelective(DataNode record);
    int updateBySelective(DataNode record);
    DataNode selectByPrimaryKey(@Param("id") Integer id, @Param("dataTypeId") Integer dataTypeId);
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("dataTypeId") Integer dataTypeId, @Param("level") Integer level);
    int refreshSeq(DataNode record);
    int getMaxSeq(@Param("id") Integer id, @Param("dataTypeId") Integer dataTypeId);
    int countSubLeafNode(GetNodesTreeBody body);
    List<DataNode> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    List<DataNode> selectByDataTypeIdAndName(@Param("dataTypeId") Integer dataTypeId, @Param("name") String name);
}
