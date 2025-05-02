import type {RecursiveRequired, Settings} from '#/global'
import settingsDefault from '@/settings.default'
import {merge} from '@/utils/object'
import {cloneDeep} from 'es-toolkit'

const globalSettings: Settings.all = {
  app: {
    enablePermission: true,
    enableDynamicTitle: true,
  },
  layout: {
    enableMobileAdaptation: true,
  },
  menu: {
    enableSubMenuCollapseButton: true,
    enableHotkeys: false,
  },
  topbar: {
    mode: 'fixed',
  },
  tabbar: {
    enable: true,
    enableIcon: true,
    enableHotkeys: false,
  },
  toolbar: {
    fullscreen: true,
    pageReload: true,
    colorScheme: true,
    navSearch: false,
  },
  navSearch: {
    enableHotkeys: false,
  },
}

export default merge(globalSettings, cloneDeep(settingsDefault)) as RecursiveRequired<Settings.all>
