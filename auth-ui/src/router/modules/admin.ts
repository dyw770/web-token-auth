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
    title: '用户',
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
    }
  ]
}

export default routes
