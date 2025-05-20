import type {Menu} from '#/global'
import apiApp from '@/api/modules/app'
import menu from '@/menu'
import {resolveRoutePath} from '@/utils'
import {cloneDeep} from 'es-toolkit'
import useSettingsStore from './settings'
import useUserStore from './user'
import {createRouterMatcher, type RouterMatcher, useRoute} from "vue-router";

const useMenuStore = defineStore(
  // 唯一ID
  'menu',
  () => {
    const settingsStore = useSettingsStore()
    const userStore = useUserStore()


    const filesystemMenusRaw = ref<Menu.recordMainRaw[]>([])
    const actived = ref(0)

    // 路由匹配器
    const routesMatcher = ref<RouterMatcher>()
    // 根据路径获取匹配的路由

    const route = useRoute()

    const currentMenu = computed(() => {
      if (!routesMatcher.value) {
        return undefined
      }
      const result = routesMatcher.value?.resolve({path: route.path}, undefined as any)?.matched
      if (!result || result.length === 0) {
        return undefined
      }
      return result.at(-1)
    })

    const userPermissions = computed((): string[] => {
      return currentMenu.value?.meta?.permissions as string[] || [] as string[]
    })

    // 完整导航数据
    const allMenus = computed(() => {
      let returnMenus: Menu.recordMainRaw[] = filesystemMenusRaw.value
      // 如果权限功能开启，则需要对导航数据进行筛选过滤
      if (settingsStore.settings.app.enablePermission) {
        returnMenus = filterAsyncMenus(returnMenus, userStore.permissions)
      }
      return returnMenus
    })
    // 次导航数据
    const sidebarMenus = computed<Menu.recordMainRaw['children']>(() => {
      return allMenus.value.length > 0
        ? allMenus.value.length > 1
          ? allMenus.value[actived.value].children
          : allMenus.value[0].children
        : []
    })
    // 次导航第一层最深路径
    const sidebarMenusFirstDeepestPath = computed(() => {
      return sidebarMenus.value.length > 0
        ? getDeepestPath(sidebarMenus.value[0])
        : settingsStore.settings.home.fullPath
    })

    function getDeepestPath(menu: Menu.recordRaw, rootPath = '') {
      let retnPath = ''
      if (menu.children) {
        const item = menu.children.find(item => item.meta?.menu !== false)
        if (item) {
          retnPath = getDeepestPath(item, resolveRoutePath(rootPath, menu.path))
        } else {
          retnPath = getDeepestPath(menu.children[0], resolveRoutePath(rootPath, menu.path))
        }
      } else {
        retnPath = resolveRoutePath(rootPath, menu.path)
      }
      return retnPath
    }

    // 次导航是否有且只有一个可访问的菜单
    const sidebarMenusHasOnlyMenu = computed(() => {
      return isSidebarMenusHasOnlyMenu(sidebarMenus.value)
    })

    function isSidebarMenusHasOnlyMenu(menus: Menu.recordRaw[]) {
      let count = 0
      let isOnly = true
      menus.forEach((menu) => {
        if (menu.meta?.menu !== false) {
          count++
        }
        if (menu.children) {
          isOnly = isSidebarMenusHasOnlyMenu(menu.children)
        }
      })
      return count <= 1 && isOnly
    }

    // 默认展开的导航路径
    const defaultOpenedPaths = computed(() => {
      const defaultOpenedPaths: string[] = []
      if (settingsStore.settings.app.routeBaseOn !== 'filesystem') {
        allMenus.value.forEach((item) => {
          defaultOpenedPaths.push(...getDefaultOpenedPaths(item.children))
        })
      }
      return defaultOpenedPaths
    })

    function getDefaultOpenedPaths(menus: Menu.recordRaw[], rootPath = '') {
      const defaultOpenedPaths: string[] = []
      menus.forEach((item) => {
        if (item.meta?.defaultOpened && item.children) {
          defaultOpenedPaths.push(resolveRoutePath(rootPath, item.path))
          const childrenDefaultOpenedPaths = getDefaultOpenedPaths(item.children, resolveRoutePath(rootPath, item.path))
          if (childrenDefaultOpenedPaths.length > 0) {
            defaultOpenedPaths.push(...childrenDefaultOpenedPaths)
          }
        }
      })
      return defaultOpenedPaths
    }

    // 判断是否有权限
    function hasPermission(permissions: string[], menu: Menu.recordMainRaw | Menu.recordRaw) {
      let isAuth = false
      if (menu.meta?.auth) {
        isAuth = permissions.some((auth) => {
          if (typeof menu.meta?.auth === 'string') {
            return menu.meta.auth !== '' ? menu.meta.auth === auth : true
          } else if (typeof menu.meta?.auth === 'object') {
            return menu.meta.auth.length > 0 ? menu.meta.auth.includes(auth) : true
          } else {
            return false
          }
        })
      } else {
        isAuth = true
      }
      return isAuth
    }

    // 根据权限过滤导航
    function filterAsyncMenus<T extends Menu.recordMainRaw[] | Menu.recordRaw[]>(menus: T, permissions: string[]): T {
      const res: any = []
      menus.forEach((menu) => {
        if (hasPermission(permissions, menu)) {
          const tmpMenu = cloneDeep(menu)
          if (tmpMenu.children && tmpMenu.children.length > 0) {
            tmpMenu.children = filterAsyncMenus(tmpMenu.children, permissions) as Menu.recordRaw[]
            tmpMenu.children.length > 0 && res.push(tmpMenu)
          } else {
            delete tmpMenu.children
            res.push(tmpMenu)
          }
        }
      })
      return res
    }

    // 生成导航（前端生成）
    async function generateMenusAtFront() {
      filesystemMenusRaw.value = menu.filter(item => item.children.length !== 0)
    }

    // 生成导航（后端生成）
    async function generateMenusAtBack() {
      const {data} = await apiApp.menuList()

      filesystemMenusRaw.value = (data as Menu.recordMainRaw[])
        .filter(item => item.children.length !== 0)
        .map(item => {
          item.children = sortMenuTree(item.children)
          return item
        })
        .sort(sort)

      treeMenuToList()
    }

    function sortMenuTree(menus: Menu.recordRaw[]): Menu.recordRaw[] {
      return menus
        .map(menu => {
          const sortedMenu = {...menu}
          if (sortedMenu.children && sortedMenu.children.length > 0) {
            sortedMenu.children = sortMenuTree(sortedMenu.children)
          }
          return sortedMenu
        })
        .sort((a, b) => {
          const orderA = a.meta?.order ?? Number.MAX_SAFE_INTEGER
          const orderB = b.meta?.order ?? Number.MAX_SAFE_INTEGER
          return orderA - orderB
        })
    }

    function sort(a: Menu.recordMainRaw, b: Menu.recordMainRaw): number {
      if (a.meta?.order && b.meta?.order) {
        return a.meta.order - b.meta.order
      } else if (a.meta?.order) {
        return -1
      } else  {
        return 1
      }
    }

    // 将菜单的路径存储在数组中，用于后续校验是否允许访问
    function treeMenuToList() {
      const list: any[] = []
      filesystemMenusRaw.value.forEach((route) => {
        if (route.children) {
          list.push(...route.children)
        }
      })

      routesMatcher.value = createRouterMatcher(list, {})
    }

    function hasAuthMenu(path: string): boolean {
      const result = routesMatcher.value?.resolve({path}, undefined as any)?.matched
      return !!(result && result.length > 0);
    }

    // 设置主导航
    function isPathInMenus(menus: Menu.recordRaw[], path: string) {
      let flag = false
      flag = menus.some((item) => {
        if (item.children) {
          return isPathInMenus(item.children, path)
        }
        return path.indexOf(`${item.path}/`) === 0 || path === item.path
      })
      return flag
    }

    function setActived(indexOrPath: number | string) {
      if (typeof indexOrPath === 'number') {
        // 如果是 number 类型，则认为是主导航的索引
        actived.value = indexOrPath
      } else {
        // 如果是 string 类型，则认为是路由，需要查找对应的主导航索引
        const findIndex = allMenus.value.findIndex(item => isPathInMenus(item.children, indexOrPath))
        if (findIndex >= 0) {
          actived.value = findIndex
        }
      }
    }

    return {
      actived,
      allMenus,
      sidebarMenus,
      sidebarMenusFirstDeepestPath,
      sidebarMenusHasOnlyMenu,
      defaultOpenedPaths,
      generateMenusAtFront,
      generateMenusAtBack,
      setActived,
      hasAuthMenu,
      currentMenu,
      userPermissions
    }
  },
)

export default useMenuStore
