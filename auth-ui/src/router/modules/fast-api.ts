import type {RouteRecordRaw} from 'vue-router'

function Layout() {
  return import('@/layouts/index.vue')
}

const routes: RouteRecordRaw = {
  path: '/fast',
  component: Layout,
  redirect: '/fast/sql',
  name: 'fast',
  meta: {
    title: 'Fast API',
    icon: 'i-heroicons-solid:menu-alt-3',
  },
  children: [
    {
      path: '/fast/sql',
      name: 'fastApiSql',
      component: () => import('@/views/fast-api/sql/index.vue'),
      meta: {
        title: 'SQL模版',
        icon: 'i-heroicons-solid:menu-alt-3',
      },
    },
    {
      path: '/fast/api',
      name: 'fastApi',
      component: () => import('@/views/fast-api/api/index.vue'),
      meta: {
        title: 'API配置',
        icon: 'i-heroicons-solid:menu-alt-3',
      },
    },
    {
      path: '/fast/doc',
      name: 'fastDoc',
      component: () => import('@/views/fast-api/doc/index.vue'),
      meta: {
        title: 'API文档',
        icon: 'i-heroicons-solid:menu-alt-3',
      },
    },
    {
      path: '/fast/data/source',
      name: 'fastDataSource',
      component: () => import('@/views/fast-api/data-source/index.vue'),
      meta: {
        title: '数据源',
        icon: 'i-heroicons-solid:menu-alt-3',
      },
    },
  ],
}

export default routes
