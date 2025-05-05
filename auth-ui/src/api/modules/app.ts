import api from '../index'

export default {
  // 基于文件系统路由模式下，后端获取导航菜单数据
  menuList: () => api.get('user/menu'),
}
