import axios from 'axios'
// 引入axios
const http=axios.create({
  baseURL:'/api/data',
  // baseURL通用请求地址(地址前缀)，这个地址根据实际项目中更改成自己的即可
  timeout:10000,
  // timeout 请求超时时间 单位是毫秒
  // 其余配置如果需要可参考axios文档进行配置=>http://www.axios-js.com/zh-cn/docs/vue-axios.html
})
// 拦截器
// 添加请求拦截器
http.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器
http.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});

export default http
