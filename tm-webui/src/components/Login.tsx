import {Input, Button, message} from 'antd';
import React from 'react';
import axios from 'axios';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import {ApiUrlConfig} from "../config/api.url";
import {LocalStorageUtils} from "../utils/LocalStorageUtils";

class Login extends React.Component {
    state = {
        username: '',
        password: '',
        loading: false,
    };

    onFinish = () => {
        this.setState({loading: true});
        const formData = new FormData();
        formData.append("username", this.state.username);
        formData.append("password", this.state.password);

        axios.post(ApiUrlConfig.LOGIN_URL,
            formData,  {headers: {"Content-Type": "multipart/form-data"}}).then(resp => {
            if (resp.status !== 200) {
                message.error('登录失败');
            } else {
                const ret = resp.data;
                if (ret.code !== 0) {
                    message.error(ret.message);
                } else {
                    localStorage.setItem(LocalStorageUtils._LOGIN_USERNAME, this.state.username);
                    window.location.href = '/'
                }
            }
        }).finally(() => {
            this.setState({loading: false});
        });
    };

    onChangeUsername = value => {
        this.setState({
            username: value.target.value
        });
    }

    onChangePassword = value => {
        this.setState({
            password: value.target.value
        });
    }

    render() {
        return (
            <div className="login-main">
                <div className="login-parent">
                    <div className="login-left">
                        <span className="login-words">test man, your best partner!</span>
                    </div>
                    <div className="login-div">
                        <Input className="username-input" placeholder="请输入用户名" onChange={this.onChangeUsername} prefix={<UserOutlined />} />
                        <Input.Password className="password-input" placeholder="请输入密码" onChange={this.onChangePassword} prefix={<LockOutlined />} />
                        <div>
                            <Button style={{width: '100%'}} type="primary" loading={this.state.loading} onClick={this.onFinish}>登录</Button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
};

export default Login;
