import api from '../index'
import type {Menu, PageRq, Resource, Role, User} from "#/api";

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

  menuRoleList: () => api.get('/admin/menu/list/and/role'),

  menuAdd: (menu: Menu.MenuSaveRq, parentMenuId?: number) => {
    if (parentMenuId) {
      return api.post(`/admin/menu/save?parentMenuId=${parentMenuId}`, menu)
    } else {
      return api.post('/admin/menu/save', menu)
    }
  },

  menuDelete: (menuId: number) => api.delete(`/admin/menu/delete/${menuId}`),

  menuUpdate: (menuInfo: Menu.MenuUpdateRq) => api.post('/admin/menu/update/info', menuInfo),

  menuUpdateHierarchy: (menuId: number, parentMenuId: number) => {
    if (parentMenuId) {
      return  api.get(`/admin/menu/update/hierarchy?menuId=${menuId}&parentMenuId=${parentMenuId}`)
    } else {
      return  api.get(`/admin/menu/update/hierarchy?menuId=${menuId}`)
    }
  },

  menuAddRoleAuth: (menuIds: number[], roleCode: string) => api.post(`/admin/menu/add/role?roleCode=${roleCode}`, menuIds),

  menuDeleteRoleAuth: (menuIds: number[], roleCode: string) => api.post(`/admin/menu/delete/role?roleCode=${roleCode}`, menuIds),

  resourceList: (searchRq: Resource.ResourceSearchRq) => api.post('/admin/resource/list', searchRq),

  resourceGet: (resourceId: number) => api.get(`/admin/resource/${resourceId}`),

  resourceEnable: (enable: boolean, resourceId: number) => api.get(`/admin/resource/enable/${resourceId}/${enable}`),

  resourceSava: (saveRq: Resource.ResourceSaveRq) => api.post('/admin/resource/save', saveRq),

  resourceDelete: (resourceId: number) => api.delete(`/admin/resource/delete/${resourceId}`),

  resourceUpdate: (updateRq: Resource.ResourceUpdateRq) => api.post('/admin/resource/update', updateRq),

  resourceAuthList: (resourceId: number) => api.get(`/admin/resource/auth/${resourceId}`),

  resourceAddAuth: (authRq: Resource.ResourceAuthAddRq) => api.post(`/admin/resource/auth/add`, authRq),

  resourceUpdateAuth: (authRq: Resource.ResourceAuthUpdateRq) => api.post(`/admin/resource/auth/update`, authRq),

  resourceDeleteAuth: (authId: number) => api.delete(`/admin/resource/auth/delete/${authId}`),

  resourceAuthRefresh: () => api.get('/admin/resource/auth/refresh'),

  userLoginInfo: (username: string) => api.get(`/admin/user/tokens?username=${username}`),

  userOffline: (token: string) => api.get(`/admin/user/offline?token=${token}`),

  apiAccessLogList: (pageRq: PageRq) => api.post('/admin/logs/access', pageRq),

  systemEventLogList: (pageRq: PageRq) => api.post('/admin/logs/event', pageRq),

  menuPermissionList: (menuId: number) => api.get(`/admin/menu/permission/list/${menuId}`),

  updateMenuPermission: (Permission: Menu.MenuPermissionSaveRq) => api.post('/admin/menu/permission/update', Permission),

  saveMenuPermission: (permission: Menu.MenuPermissionSaveRq) => api.post('/admin/menu/permission/save', permission),

  deleteMenuPermission: (menuId: number, permissionId: string) => api.delete(`/admin/menu/permission/delete/${menuId}/${permissionId}`),

  getRoleMenuPermissions: (roleCode: string, menuId: number) => api.get(`/admin/menu/permission/role?roleCode=${roleCode}&menuId=${menuId}`),

  addRoleMenuPermissions: (saveRq: Menu.RoleMenuPermissionSaveRq) => api.post(`/admin/menu/permission/role/save`, saveRq),

  deleteRoleMenuPermissions: (authId: number) => api.delete(`/admin/menu/permission/role/delete?authId=${authId}`),

  testPublicApi: () => api.get('/test/static'),

  testPrivateApi: () => api.get('/test/info'),
}
