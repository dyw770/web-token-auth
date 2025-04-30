import apiUser from '@/api/modules/user'
import router from '@/router'
import {computed, ref} from "vue";
import {defineStore} from "pinia";
import {toast} from "vue-sonner";


const useUserStore = defineStore(
  // 唯一ID
  'user',
  () => {
    const username = ref(localStorage.username ?? '')
    const token = ref(localStorage.token ?? '')
    const avatar = ref(localStorage.avatar ?? '')
    const permissions = ref<string[]>([])
    const isLogin = computed(() => {
      return !!token.value;

    })

    // 登录
    async function login(data: {
      username: string
      password: string
    }) {
      const res = await apiUser.login(data)
      toast.info('登录成功')
      localStorage.setItem('username', data.username)
      localStorage.setItem('token', res.data)
      localStorage.setItem('avatar', '')
      username.value = data.username
      token.value = res.data
      avatar.value = ''
    }

    // 手动登出
    async function logout(redirect = router.currentRoute.value.fullPath) {
      await apiUser.logout()
      // 此处仅清除计算属性 isLogin 中判断登录状态过期的变量，以保证在弹出登录窗口模式下页面展示依旧正常
      localStorage.removeItem('token')
      token.value = ''
      router.push({
        name: 'login',
      }).then(logoutCleanStatus)
    }

    // 请求登出
    function requestLogout() {
      // 此处仅清除计算属性 isLogin 中判断登录状态过期的变量，以保证在弹出登录窗口模式下页面展示依旧正常
      localStorage.removeItem('token')
      token.value = ''
      router.push({
        name: 'login'
      }).then(logoutCleanStatus)
    }
    // 登出后清除状态
    function logoutCleanStatus() {
      localStorage.removeItem('account')
      localStorage.removeItem('avatar')
      username.value = ''
      avatar.value = ''
      permissions.value = []

    }

    return {
      username,
      token,
      avatar,
      permissions,
      isLogin,
      login,
      logout,
      requestLogout,
    }
  },
)

export default useUserStore
