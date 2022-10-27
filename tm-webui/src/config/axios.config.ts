import axios from 'axios';
import {BaseUrl} from "./api.url";

const initAxios = () =>
{
    axios.defaults.baseURL = BaseUrl;
    axios.defaults.headers.post['Content-Type'] = 'application/json';
    axios.interceptors.request.use(function (config) {
        config.withCredentials = true
        config.headers = {

        }
        return config;
    }, function (error) {
        console.log(error);
        return Promise.reject(error);
    });
    axios.interceptors.response.use(
        response => {
            return Promise.resolve(response)
        },
        error => {
            if(!error.response) {
                return Promise.reject({code: -1, message: ''});
            }
            if (error.response.status === 401) {
                const data = {
                    code: -1,
                    message: '未授权'
                }
                window["__ROUTER__"].push('/login');
                return Promise.reject(data)
            }
            return Promise.reject(error)
        }
    )
}

export default {
    initAxios
}
