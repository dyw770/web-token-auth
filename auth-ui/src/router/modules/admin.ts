import type {RouteRecordRaw} from 'vue-router'

function Layout() {
  return import('@/layouts/index.vue')
}

export const adminRoutes: RouteRecordRaw = {
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
            icon: 'ri:menu-add-line',
          },
        },
        {
          path: '/admin/menu/auth',
          component: () => import('@/views/admin/auth/index.vue'),
          name: 'adminMenuAuth',
          meta: {
            title: '菜单授权',
            icon: 'ri:menu-unfold-line',
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
            icon: 'icon-park:api-app',
            cache: 'adminResourceAuth'
          },
        },
        {
          path: '/admin/resource/auth/:id(\\d+)',
          component: () => import('@/views/admin/resource/auth.vue'),
          name: 'adminResourceAuth',
          meta: {
            title: 'API资源授权',
            activeMenu: '/admin/resource',
          }
        }
      ]
    },
    {
      path: '/admin/logs',
      component: Layout,
      name: 'adminLogs',
      meta: {
        title: '系统日志',
        breadcrumb: false,
        icon: 'icon-park:log',
      },
      children: [
        {
          path: '/admin/logs/access',
          component: () => import('@/views/admin/AccessLog/index.vue'),
          name: 'adminAccessLogs',
          meta: {
            title: '访问日志',
            icon: 'icon-park:upload-logs',
          },
        },
        {
          path: '/admin/logs/event',
          component: () => import('@/views/admin/SystemEvent/index.vue'),
          name: 'adminEventLogs',
          meta: {
            title: '事件日志',
            icon: 'icon-park:upload-logs',
          },
        },
      ]
    },
  ]
}

export const simpleRoutes: RouteRecordRaw = {
  path: '/simple',
  component: Layout,
  meta: {
    breadcrumb: false,
  },
  children: [
    {
      path: '/simple/test',
      component: () => import('@/views/simple/index.vue'),
      name: 'adminTest',
      meta: {
        title: '资源授权测试',
        icon: 'ri:test-tube-line',
      },
    }
  ]
}
