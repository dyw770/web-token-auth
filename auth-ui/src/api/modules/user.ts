import api from '../index'

export default {
  // 登录
  login: (data: {
    username: string
    password: string
  }) => api.post('user/login', data),

  // 获取权限
  permission: () => api.get('user/permission'),

  // 获取菜单
  menu: () => api.get('user/menu'),

  // 退出登录
  logout: () => api.get('user/logout'),

  // 获取用户信息
  info: () => api.get('user/info'),
}
