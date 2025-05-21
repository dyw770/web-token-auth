import useSettingsStore from '@/store/modules/settings'
import useMenuStore from '@/store/modules/menu.ts'

export default function useAuth() {
  function hasPermission(permission: string) {
    const settingsStore = useSettingsStore()
    const menuStore = useMenuStore()
    if (settingsStore.settings.app.enablePermission) {
      return menuStore.userPermissions.includes(permission)
    }
    else {
      return true
    }
  }

  function auth(value: string | string[]) {
    if (value === undefined) {
      return false
    }
    let auth
    if (typeof value === 'string') {
      auth = value !== '' ? hasPermission(value) : true
    }
    else {
      auth = value.length > 0 ? value.some(item => hasPermission(item)) : true
    }
    return auth
  }

  function authAll(value: string[]) {
    if (value === undefined) {
      return false
    }
    return value.length > 0 ? value.every(item => hasPermission(item)) : true
  }

  return {
    auth,
    authAll,
  }
}
