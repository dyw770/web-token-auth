import {defineStore} from "pinia";

const useMenuStore = defineStore(
  // 唯一ID
  'menu',
  () => {

    function allMenus(): any {
      console.log("请求全部菜单")
    }

    return {
      allMenus
    }
  },
)

export default useMenuStore
