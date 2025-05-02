import api from '../index'

export default {
  // 登录
  login: (data: {
    username: string
    password: string
  }) => api.post('user/login', data),

  // 获取用户信息
  info: () => api.get('user/info'),

  // 退出登录
  logout: () => api.get('user/logout'),

  // 修改密码
  passwordEdit: (data: {
    password: string
    newPassword: string
  }) => api.post('user/password/edit', data, {
    baseURL: '/mock/',
  }),
}
