import React from 'react';
import type {MenuProps} from 'antd';
import {Button, Dropdown, Layout, Menu, message} from 'antd';
import axios from 'axios';
import {Link, RouteComponentProps} from 'react-router-dom';
import {renderRoutes} from 'react-router-config';
import {DownOutlined,} from '@ant-design/icons';
import {ApiUrlConfig} from "../../config/api.url";
import {LocalStorageUtils} from "../../utils/LocalStorageUtils";

const { Content, Header } = Layout;

interface IProps {}

type IndexProps = IProps & RouteComponentProps;
type MenuItem = Required<MenuProps>['items'][number];
interface IState {
    collapsed: boolean;
    route: any;
    menuTreeList: any[];
}

class Index extends React.Component <IndexProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            collapsed: false,
            route: props.route,
            menuTreeList: [
            ],
        };
    }

    onCollapse = () => {
        this.setState({collapsed: !this.state.collapsed});
    }

    componentDidMount() {
        window["__ROUTER__"] = this.props.history;
        axios.get(ApiUrlConfig.GET_ALL_MENU_TREE_URL).then(response => {
            if (response.status !== 200) {
                message.error('加载菜单失败');
            } else {
                const ret = response.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    this.setState({menuTreeList: ret.data});
                    LocalStorageUtils.setItem(LocalStorageUtils.__ALL_MENU_TREE, JSON.stringify(ret.data));
                }
            }
        }).catch(resp => {
            const item = LocalStorageUtils.getItem(LocalStorageUtils.__ALL_MENU_TREE);
            if(item) {
                this.setState({menuTreeList: JSON.parse(item)});
            }
        });
    }
    logout = () => {
        axios.get(ApiUrlConfig.LOGOUT_URL).then(response => {
            if (response.status !== 200) {
                message.error('退出失败');
            } else {
                this.props.history.push('/login');
            }
        });
    }

    render() {
        const menuTreeList = this.state.menuTreeList;
        const items:MenuItem [] = [];
        menuTreeList.map(row => {
            if(row.children && row.children.length > 0) {
                const m1: any = {label: row.menuName, key: row.id.toString(), children: []};
                row.children.map(r => {
                    const m2: any = {label: <Link exact="true" to={r.url}>{r.menuName}</Link>, key: r.id.toString()}
                    m1.children.push(m2);
                });
                items.push(m1);
            }else{
                const m1: any = {label: <Link exact="true" to={row.url}>{row.menuName}</Link>, key: row.id.toString()};
                items.push(m1);
            }
            return true;
        });
        const route = this.state.route || {children: []};
        const loginUsername = localStorage.getItem('_LOGIN_USERNAME');

        const rightItems: MenuProps = {items: [{label: (<Button type="link" onClick={() => this.logout()}>退出</Button>), key: '1'}]};
        return (<Layout style={{ minHeight: '100vh' }}>
                <Header style={{display: 'flex', background: '#FFFFFF'}}>
                    <div className="logo-text">testman</div>
                    <Menu theme="light" defaultSelectedKeys={['1']} mode="horizontal" style={{flex: 1, marginLeft: '200px'}} items={items}/>
                    <div style={{float: 'right'}}>
                        <Dropdown menu={rightItems}>
                            <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                                {loginUsername} <DownOutlined />
                            </a>
                        </Dropdown>
                    </div>
                </Header>
                <Layout className="site-layout">
                    <Content style={{ margin: '0 0px', padding: '5px' }}>
                        {renderRoutes(route.children)}
                    </Content>
                </Layout>
            </Layout>
        );
    }
}

export default Index;
