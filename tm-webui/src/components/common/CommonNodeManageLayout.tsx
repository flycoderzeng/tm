import React from "react";
import axios from "axios";
import {message, Select} from 'antd';
import {RouteComponentProps, withRouter} from "react-router-dom";
import {CommonNodeTree} from "../common/CommonNodeTree";
import {ProjectModel} from "../../entities/ProjectModel";
import {AntDataNode} from "../../entities/AntDataNode";
import {DataTypeEnumDescription} from "../../entities/DataTypeEnumDescription";
import {CommonNodeListPage} from "./CommonNodeListPage";
import ShellEdit from "../autotest/shell/ShellEdit";
import {DataTypeEnum} from "../../entities/DataTypeEnum";
import GlobalVariableEdit from "../autotest/globalvariable/GlobalVariableEdit";
import {PlatformApiEdit} from "../autotest/platformapi/PlatformApiEdit";
import HttpApiEdit from "../autotest/httpapi/HttpApiEdit";
import {AutoCaseEditor} from "../autotest/case-editor/editor/AutoCaseEditor";
import {ApiUrlConfig} from "../../config/api.url";
import {AutoPlanEdit} from "../autotest/autoplan/AutoPlanEdit";
import {Resizable} from "re-resizable";

interface IProps {}
const {Option} = Select;
type CommonNodeListProps = IProps & RouteComponentProps;


interface IState {
    route: any;
    nodeId ?: number|null;
    dataTypeId ?: number|null;
    projectId ?: number|null;
    prevProjectId ?: number|null;
    renderRightFlag: number;
    treeParentId?: number;
    myProjects: ProjectModel[];
    initTreeData: AntDataNode[];
}
const initTreeData = [{ title: '自动化用例', key: '1-1' }];

const rootTitle = {
    2: DataTypeEnumDescription.APP_API,
    3: DataTypeEnumDescription.AUTO_SHELL,
    4: DataTypeEnumDescription.GLOBAL_VARIABLE,
    5: DataTypeEnumDescription.PLATFORM_API,
    6: DataTypeEnumDescription.AUTO_CASE,
    7: DataTypeEnumDescription.AUTO_PLAN
}

class CommonNodeManageLayout extends React.Component<CommonNodeListProps, IState> {
    constructor(props) {
        super(props);
        let dataTypeId;
        if(props.match.params.dataTypeId) {
            dataTypeId = props.match.params.dataTypeId;
        }else{
            dataTypeId = props.dataTypeId;
        }
        initTreeData[0].title = rootTitle[dataTypeId];
        this.state = {
            route: props.route,
            dataTypeId: dataTypeId,
            prevProjectId: undefined,
            projectId: undefined,
            nodeId: null,
            renderRightFlag: 0,
            myProjects: [],
            initTreeData: initTreeData,
        }
    }

    componentDidMount() {
        this.getMyProjects();
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (prevState.dataTypeId !== nextProps.match.params.dataTypeId) {
            prevState.initTreeData[0].title = rootTitle[nextProps.match.params.dataTypeId];
            return {
                ...prevState,
                dataTypeId: nextProps.match.params.dataTypeId,
                renderRightFlag: 0,
            };
        }
        return null;
    }

    getMyProjects() {
        axios.get(ApiUrlConfig.GET_MY_PROJECTS_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('获取数据失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    let myProjects :ProjectModel[] = [];
                    if(resp.data.data && resp.data.data.length > 0) {
                        myProjects = resp.data.data;
                    } else {
                        myProjects = [];
                    }
                    this.setState({
                        myProjects: myProjects
                    });
                    if(myProjects.length > 0) {
                        const lastUsedProjectId = window.localStorage.getItem('_LAST_USED_PROJECT_ID');
                        if(lastUsedProjectId) {
                            const lastUsedProjectIdInt = parseInt(lastUsedProjectId);
                            if(lastUsedProjectIdInt && myProjects.filter(v => {return v.id === lastUsedProjectIdInt;}).length > 0) {
                                this.setState({
                                    projectId: lastUsedProjectIdInt
                                });
                            }
                        } else {
                            this.setState({
                                projectId: myProjects[0].id
                            });
                        }
                    }
                }
            }
        }).catch(resp => {
            message.error(resp.message);
        });
    }

    getMyProjectOptions() {
        return this.state.myProjects.map(function (v) {
            return <Option key={v.id} value={v.id}>{v.name}</Option>
        });
    }

    handleProjectChange = (e) => {
        const projectId = this.state.projectId;
        const rows = [{ title: rootTitle[this.state.dataTypeId || 6], key: '1-1' }];
        this.setState({
            prevProjectId: projectId,
            projectId: e,
            initTreeData: [...rows],
        });
        window.localStorage.setItem("_LAST_USED_PROJECT_ID", e);
    }

    setRenderRightFlag = (flag: number, treeParentId?: number) => {
        this.setState({
            renderRightFlag: flag,
            treeParentId: treeParentId,
        });
    }

    setNodeId = (id: number) => {
        this.setState({
            nodeId: id,
        });
    }

    renderNodeTree() {
        if(this.state.projectId) {
            return <CommonNodeTree setNodeId={this.setNodeId} setRenderRightFlag={this.setRenderRightFlag} initTreeData={this.state.initTreeData} dataTypeId={this.props.match.params.dataTypeId} projectId={this.state.projectId}></CommonNodeTree>
        } else {
            return <span>暂无数据，请加入项目</span>
        }
    }

    renderRight() {
        if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.ALL) {
            return <CommonNodeListPage setNodeId={this.setNodeId} setRenderRightFlag={this.setRenderRightFlag} treeParentId={this.state.treeParentId} projectId={this.state.projectId} dataTypeId={this.props.match.params.dataTypeId}></CommonNodeListPage>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.AUTO_SHELL) {
            return <ShellEdit setRenderRightFlag={this.setRenderRightFlag} id={this.state.nodeId}></ShellEdit>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.GLOBAL_VARIABLE) {
            return <GlobalVariableEdit setRenderRightFlag={this.setRenderRightFlag} id={this.state.nodeId}></GlobalVariableEdit>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.PLATFORM_API) {
            return <PlatformApiEdit setRenderRightFlag={this.setRenderRightFlag} id={this.state.nodeId}></PlatformApiEdit>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.APP_API) {
            return <HttpApiEdit setRenderRightFlag={this.setRenderRightFlag} id={this.state.nodeId}></HttpApiEdit>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.AUTO_CASE) {
            return <AutoCaseEditor id={this.state.nodeId}></AutoCaseEditor>
        }else if(this.state.projectId && this.state.renderRightFlag === DataTypeEnum.AUTO_PLAN) {
            return <AutoPlanEdit projectId={this.state.projectId} id={this.state.nodeId} setRenderRightFlag={this.setRenderRightFlag}></AutoPlanEdit>
        }else{
            return <span>暂无数据，请加入项目</span>
        }
    }

    render() {
        const route = this.state.route || {children: []};
        return (
        <div className="tree-main-div">
            <Resizable
                defaultSize={{
                    width: 310,
                    height: '100%',
                }}
                minWidth={310}
                maxWidth={550}
                className="left-tree-panel"
                enable={{top:false, right:true, bottom:false, left:false, topRight:false, bottomRight:false, bottomLeft:false, topLeft:false}}
            >
                <div className="myprojects">
                    <Select
                        showSearch
                        value={this.state.projectId || undefined}
                        style={{ width: '100%' }}
                        onChange={this.handleProjectChange}
                        placeholder="选择你的项目"
                        optionFilterProp="children"
                    >
                        {this.getMyProjectOptions()}
                    </Select>
                </div>
                <div className="node-tree-content">
                {this.state.projectId && this.renderNodeTree()}
                </div>
            </Resizable>
            <div className="right-panel">
                {this.state.projectId && this.renderRight()}
            </div>
        </div>
        );
    }
}

export default withRouter(CommonNodeManageLayout);
