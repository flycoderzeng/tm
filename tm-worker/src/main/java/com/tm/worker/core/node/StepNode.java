package com.tm.worker.core.node;

import com.tm.common.entities.autotest.enumerate.StepNodeTypeDefineEnum;
import com.tm.worker.core.control.GenericController;
import com.tm.worker.core.exception.TMException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Data
public class StepNode {
    private String type;
    private Integer level;
    private String title;
    private String key;
    private boolean isLeaf;

    // 执行一次就加1
    private int runCount = 0;

    private List<StepNode> children;

    private StepNodeBase define;

    private StepNode parent;

    private boolean ended = false;

    public void run() throws Exception {
        runCount++;
        if(define == null) {
            throw new TMException(getTitle() + " 没有对应的处理逻辑代码");
        }
        if(!define.isEnabled()) {
            log.info("{} 被禁用了，不执行", getTitle());
        }
        define.run();
    }

    public StepNode next() {
        StepNode nextNode = getNext();
        return nextNode;
    }

    private StepNode getNext() {
        // 如果节点被禁用了
        if(!define.isEnabled()) {
            setEnded(true);
        }
        // 如果节点已经执行结束
        if(isEnded()) {
            return getNextBrotherNode();
        }

        if(type.equals(StepNodeTypeDefineEnum.WHILE.value()) || type.equals(StepNodeTypeDefineEnum.IF.value()) ||
            type.equals(StepNodeTypeDefineEnum.LOOP.value())) {
            GenericController controller = (GenericController) define;
            if(controller.isBreakLoop()) {
                setEnded(true);
                return getNextBrotherNode();
            }else{
                if(children != null && children.size() > 0) {
                    return children.get(0);
                }else{
                    log.info("{} 没有子步骤", define.getName());
                    setEnded(true);
                    return getNextBrotherNode();
                }
            }
        }else{
            if (children != null && children.size() > 0) {
                return children.get(0);
            }
            setEnded(true);
            return getNextBrotherNode();
        }
    }

    private StepNode getNextBrotherNode() {
        if(parent == null) {
            return null;
        }
        for (int i = 0; i < parent.getChildren().size(); i++) {
            if(parent.getChildren().get(i).getKey().contentEquals(key) && (i+1) < parent.getChildren().size()) {
                return parent.getChildren().get(i+1);
            }
        }
        if(!parent.getType().equals(StepNodeTypeDefineEnum.WHILE.value()) &&
                !parent.getType().equals(StepNodeTypeDefineEnum.LOOP.value())) {
            if(!parent.isEnded()) {
                parent.setEnded(true);
            }
        }
        return getParentNext();
    }

    private StepNode getParentNext() {
        if(parent == null) {
            return null;
        }
        if(parent.getType().equals(StepNodeTypeDefineEnum.WHILE.value()) ||
                parent.getType().equals(StepNodeTypeDefineEnum.LOOP.value())) {
            return parent;
        }
        if(!parent.isEnded()) {
            parent.setEnded(true);
        }
        return parent.next();
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
        if(ended) {
            log.info("{} 所有子步骤执行完毕, 步骤key 是 {}", type, getKey());
        }
    }
}
