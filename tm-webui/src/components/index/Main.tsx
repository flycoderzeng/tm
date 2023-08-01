import React from "react";
import {Card, message} from 'antd';
import axios from "axios";
import { withRouter } from "react-router-dom";
import { RouteComponentProps } from "react-router-dom";
import {ApiUrlConfig} from "../../config/api.url";
import {RandomUtils} from "../../utils/RandomUtils";
interface IProps {}
type MainProps = IProps & RouteComponentProps;

interface ProjectModel {
    id: number;
    name: string;
    description: string;
    addTime: string;
    addUser: string;
    lastModifyUser: string;
    lastModifyTime: string;
    status: number;
}

interface IState {
    myProjects: ProjectModel[];
}

class Main extends React.Component<MainProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            myProjects: []
        }
    }
    componentDidMount() {
        this.loadMyProjects();
    }

    loadMyProjects() {
        axios.get(ApiUrlConfig.GET_PERSONAL_PROJECTS_URL).then(resp => {
            if (resp.status !== 200) {
                message.error('加载列表失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    this.setState({
                        myProjects: ret.data
                    })
                }
            }
        });
    }

    onClickProject(id: number) {
        window.localStorage.setItem("_LAST_USED_PROJECT_ID", id+"");
        window["__ROUTER__"].push('/nodemanage/6');
    }

    renderMyProjects() {
        const cards = this.state.myProjects.map((v: any) => {
            const k = RandomUtils.getKey();
            return (<Card onClick={() => this.onClickProject(v.id)} className="project-card" title={v.name} bordered={false} style={{ width: 300 }} key={k}>
                <p><span>自动化用例总数: </span><span style={{color: '#1890ff'}}>{v.projectStatisticsInfo.totalCase}</span></p>
                <p><span>自动化计划总数: </span><span style={{color: '#1890ff'}}>{v.projectStatisticsInfo.totalPlan}</span></p>
            </Card>)
        });
        return cards;
    }

    render() {
        return (<div className="card">
            <div className="card-header card-header-divider">我的项目<span className="card-subtitle">我参与的项目</span>
            </div>
            <div className="card-body">
                <div className="my-projects-row">
                    {this.renderMyProjects()}
                </div>
            </div>
        </div>
        )
    }
}

export default withRouter(Main);
