import type {RouteRecordRaw} from 'vue-router'

function Layout() {
  return import('@/layouts/index.vue')
}

const routes: RouteRecordRaw = {
  path: '/admin',
  component: Layout,
  meta: {
    breadcrumb: false,
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
    },
    {
      path: '/admin/menu',
      component: Layout,
      redirect: '/admin/menu/edit',
      name: 'adminMenu',
      meta: {
        title: '菜单管理',
        icon: 'ant-design:menu-outlined',
      },
      children: [
        {
          path: '/admin/menu/edit',
          component: () => import('@/views/admin/menu/index.vue'),
          name: 'adminMenuEdit',
          meta: {
            title: '菜单编辑',
            icon: 'ant-design:menu-outlined',
          },
        },
        {
          path: '/admin/menu/auth',
          component: () => import('@/views/admin/auth/index.vue'),
          name: 'adminMenuAuth',
          meta: {
            title: '菜单授权',
            icon: 'ant-design:menu-outlined',
          },
        }
      ]
    },
    {
      path: '/admin/resource',
      component: Layout,
      meta: {
        breadcrumb: false,
      },
      children: [
        {
          path: '',
          component: () => import('@/views/admin/resource/index.vue'),
          name: 'adminResource',
          meta: {
            title: 'API资源管理',
            icon: 'ant-design:api-outlined',
            cache: 'adminResourceAuth'
          },
        },
        {
          path: '/admin/resource/auth/:id(\\d+)',
          component: () => import('@/views/admin/resource/auth.vue'),
          name: 'adminResourceAuth',
          meta: {
            title: 'API资源授权',
            icon: 'ant-design:api-outlined',
            activeMenu: '/admin/resource',
          }
        }
      ]
    },
  ]
}

export default routes
