import api from '../index'
import type {User} from "#/api";

export default {
  // 获取用户列表
  userList: (rq: User.UserSearchRq) => api.post('/admin/user/list', rq),

  userAdd: (rq: User.UserCreateRq) => api.post('/admin/user/add', rq),

  userEdit: (rq: User.UserEditRq) => api.put('/admin/user/edit', rq),

  userLock: (username: string) => api.get(`/admin/user/lock/${username}`),

  userEnable: (username: string) => api.get(`/admin/user/enable/${username}`)
}
