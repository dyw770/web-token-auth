import apiUser from '@/api/modules/user'
import router from '@/router'
import useMenuStore from './menu'
import useRouteStore from './route'
import useSettingsStore from './settings'
import useTabbarStore from './tabbar'

const useUserStore = defineStore(
  // 唯一ID
  'user',
  () => {
    const settingsStore = useSettingsStore()
    const routeStore = useRouteStore()
    const menuStore = useMenuStore()
    const tabbarStore = useTabbarStore()

    const account = ref(localStorage.account ?? '')
    const nickname = ref(localStorage.nickname ?? '')
    const token = ref(localStorage.token ?? '')
    const avatar = ref(localStorage.avatar ?? '')
    const permissions = ref<string[]>([])
    const isLogin = computed(() => {
      if (token.value) {
        userInfo().then(() => {})
        return true
      }
      return false
    })

    // 登录
    async function login(data: {
      account: string
      password: string
    }) {
      const res = await apiUser.login({
        username: data.account,
        password: data.password,
      })
      localStorage.setItem('token', res.data)
      token.value = res.data
    }

    async function userInfo() {
      const info = await apiUser.info()
      localStorage.setItem('username', info.data.username)
      localStorage.setItem('nickname', info.data.nickname)
      localStorage.setItem('avatar', info.data.avatar)

      account.value = info.data.username
      nickname.value = info.data.nickname
      avatar.value = info.data.avatar
    }

    // 手动登出
    async function logout(redirect = router.currentRoute.value.fullPath) {
      await apiUser.logout()
      // 此处仅清除计算属性 isLogin 中判断登录状态过期的变量，以保证在弹出登录窗口模式下页面展示依旧正常
      localStorage.removeItem('token')
      token.value = ''
      router.push({
        name: 'login',
        query: {
          ...(redirect !== settingsStore.settings.home.fullPath && router.currentRoute.value.name !== 'login' && { redirect }),
        },
      }).then(logoutCleanStatus)
    }
    // 请求登出
    function requestLogout() {
      // 此处仅清除计算属性 isLogin 中判断登录状态过期的变量，以保证在弹出登录窗口模式下页面展示依旧正常
      localStorage.removeItem('token')
      token.value = ''
      router.push({
        name: 'login',
        query: {
          ...(
            router.currentRoute.value.fullPath !== settingsStore.settings.home.fullPath
            && router.currentRoute.value.name !== 'login'
            && {
              redirect: router.currentRoute.value.fullPath,
            }
          ),
        },
      }).then(logoutCleanStatus)
    }

    // 登出后清除状态
    function logoutCleanStatus() {
      localStorage.removeItem('username')
      localStorage.removeItem('avatar')
      localStorage.removeItem('nickname')
      account.value = ''
      avatar.value = ''
      nickname.value = ''
      permissions.value = []
      settingsStore.updateSettings({}, true)
      tabbarStore.clean()
      routeStore.removeRoutes()
      menuStore.setActived(0)
    }

    // 获取权限
    async function getPermissions() {
      permissions.value = []
    }

    // 修改密码
    async function editPassword(data: {
      password: string
      newPassword: string
    }) {
      await apiUser.passwordEdit(data)
    }

    return {
      account,
      token,
      avatar,
      permissions,
      isLogin,
      login,
      userInfo,
      logout,
      requestLogout,
      getPermissions,
      editPassword,
    }
  },
)

export default useUserStore
