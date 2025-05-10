import type {RouteRecordRaw} from 'vue-router'

function Layout() {
  return import('@/layouts/index.vue')
}

const routes: RouteRecordRaw = {
  path: '/admin',
  component: Layout,
  name: 'admin',
  redirect: '/admin/user',
  meta: {
    title: '管理',
    icon: 'ant-design:user-outline',
  },
  children: [
    {
      path: '/admin/user',
      component: () => import('@/views/admin/user/index.vue'),
      name: 'adminUser',
      meta: {
        title: '用户管理',
        icon: 'ant-design:user-outline',
      },
    },
    {
      path: '/admin/role',
        component: () => import('@/views/admin/role/index.vue'),
      name: 'adminRole',
      meta: {
      title: '角色管理',
        icon: 'ant-design:team-outlined',
    },
    }
  ]
}

export default routes
