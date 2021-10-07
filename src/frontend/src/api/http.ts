import axios, { AxiosRequestConfig } from 'axios';

const instance = axios.create({
    // baseURL: `http://localhost:8081`,
    responseType: 'json'
});

instance.interceptors.request.use(
(config: AxiosRequestConfig) =>
    new Promise((resolve, reject) => {
        config['headers'] = {
            'Content-Type': config.headers['Content-Type'] || 'application/json',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, OPTIONS'
        };
        resolve(config);
    })
);

export default instance;
