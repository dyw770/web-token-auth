import api from '../index'
import type {Menu, Role, User} from "#/api";

export default {
  // 获取用户列表
  userList: (rq: User.UserSearchRq) => api.post('/admin/user/list', rq),

  userAdd: (rq: User.UserCreateRq) => api.post('/admin/user/add', rq),

  userEdit: (rq: User.UserEditRq) => api.put('/admin/user/edit', rq),

  userLock: (username: string) => api.get(`/admin/user/lock/${username}`),

  userEnable: (username: string) => api.get(`/admin/user/enable/${username}`),

  roleList: () => api.get('/admin/role/list'),

  addRoleForUser: (username: string, roleCode: string[]) => api.post(`/admin/user/role/${username}`, roleCode),

  roleAdd: (role: Role.RoleSaveRq, parentRoleCode?: string) => {
    if (parentRoleCode) {
      return api.post(`/admin/role/save?parentRoleCode=${parentRoleCode}`, role)
    } else {
      return api.post('/admin/role/save', role)
    }
  },

  roleDelete: (roleCode: string) => api.get(`/admin/role/delete/${roleCode}`),

  roleUpdate: (roleInfo: Role.RoleUpdateRq) => api.post('/admin/role/update/info', roleInfo),

  roleUpdateHierarchy: (roleCode: string, parentRoleCode: string) => {
    if (parentRoleCode) {
      return  api.get(`/admin/role/update/hierarchy?roleCode=${roleCode}&parentRoleCode=${parentRoleCode}`)
    } else {
      return  api.get(`/admin/role/update/hierarchy?roleCode=${roleCode}`)
    }
  },

  menuList: () => api.get('/admin/menu/list'),

  menuAdd: (menu: Menu.MenuSaveRq, parentMenuId?: number) => {
    if (parentMenuId) {
      return api.post(`/admin/menu/save?parentMenuId=${parentMenuId}`, menu)
    } else {
      return api.post('/admin/menu/save', menu)
    }
  },

  menuDelete: (menuId: number) => api.delete(`/admin/menu/delete/${menuId}`),

  menuUpdate: (menuInfo: Menu.MenuUpdateRq) => api.post('/admin/menu/update/info', menuInfo),
}
