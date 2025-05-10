import api from '../index'
import type {Role, User} from "#/api";

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
}
