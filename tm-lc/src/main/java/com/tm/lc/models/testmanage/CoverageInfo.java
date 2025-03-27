package com.tm.lc.models.testmanage;

import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "coverage_info")
@Include(name="coverage_info")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
public class CoverageInfo extends CommonSixItemsElideModel {
    @Column(name = "name")
    private String name;
    @Column(name = "git_url")
    private String gitUrl;
    @Column(name = "branch")
    private String branch;
    @Column(name = "branch2")
    private String branch2;
    @Column(name = "git_commit_id1")
    private String gitCommitId1;
    @Column(name = "git_commit_id2")
    private String gitCommitId2;
    @Column(name = "missed_instructions")
    private Integer missedInstructions;
    @Column(name = "covered_instructions")
    private Integer coveredInstructions;
    @Column(name = "missed_branches")
    private Integer missedBranches;
    @Column(name = "covered_branches")
    private Integer coveredBranches;
    @Column(name = "missed_cxty")
    private Integer missedCxty;
    @Column(name = "covered_cxty")
    private Integer coveredCxty;
    @Column(name = "missed_methods")
    private Integer missedMethods;
    @Column(name = "covered_methods")
    private Integer coveredMethods;
    @Column(name = "missed_classes")
    private Integer missedClasses;
    @Column(name = "covered_classes")
    private Integer coveredClasses;
    @Column(name = "missed_lines")
    private Integer missedLines;
    @Column(name = "covered_lines")
    private Integer coveredLines;
}
