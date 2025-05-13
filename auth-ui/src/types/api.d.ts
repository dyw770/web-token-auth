interface OrderRq {
  /**
   * 排序的字段名
   */
  column: string,

  /**
   * 是否是升序
   */
  asc: boolean,
}

interface PageRq {
  /**
   * 页码
   */
  page: number,

  /**
   * size
   */
  size: number,

  /**
   * 排序
   */
  orders?: Array<OrderRq>
}

declare namespace User {

  interface UserSearchRq extends PageRq {
    username?: string | undefined
    nickname?: string | undefined,
    enabled?: boolean | undefined
  }

  interface UserRs {
    username: string
    nickname: string
    avatar?: string
    enabled: boolean
    accountNonExpired: boolean
    credentialsNonExpired: boolean
    accountNonLocked: boolean
    createTime: string
    roles: Array<UserRoleRs>
  }

  interface UserRoleRs {

    /**
     * 角色ID
     */
    roleCode: string

    /**
     * 角色名
     */
    roleName: string
  }

  /**
   * 创建用户请求参数
   */
  interface UserCreateRq {
    /**
     * 用户名，必填，长度 3-12 字符
     */
    username: string;

    /**
     * 昵称，必填，最大长度 12 字符
     */
    nickname: string;

    /**
     * 密码，必填，长度 6-16 字符
     */
    password: string;

    /**
     * 头像 URL，非必填
     */
    avatar?: string;

    /**
     * 是否启用，必填
     */
    enabled: boolean;
  }

  /**
   * 创建用户请求参数
   */
  interface UserEditRq {
    /**
     * 用户名，必填，长度 3-12 字符
     */
    username: string;

    /**
     * 昵称，必填，最大长度 12 字符
     */
    nickname: string;

    /**
     * 密码，必填，长度 6-16 字符
     */
    password?: string;

    /**
     * 头像 URL，非必填
     */
    avatar?: string;

    /**
     * 是否启用，必填
     */
    enabled: boolean;
  }
}

declare namespace Role {
  interface RoleListRs {
    /**
     * 角色编码，作为主键唯一标识一个角色。
     */
    roleCode: string;

    /**
     * 角色名称，用于展示和权限配置。
     */
    roleName: string;

    /**
     * 角色描述，可为空。
     */
    description?: string;

    /**
     * 创建时间，通常为 ISO 格式字符串。
     */
    createTime: string;

    /**
     * 更新时间，通常为 ISO 格式字符串。
     */
    updateTime: string;


    /**
     * 父角色的编码，用于构建角色树结构。
     */
    parentRoleCode?: string;

    /**
     * 子角色列表，表示当前角色的直接子角色集合。
     */
    children: RoleListRs[];
  }

  /**
   * 新增角色请求参数
   */
  interface RoleSaveRq {
    /**
     * 角色编码（2-12个字符，非空）
     * @required
     * @minLength 2
     * @maxLength 12
     */
    roleCode: string;

    /**
     * 角色名称（2-12个字符，非空）
     * @required
     * @minLength 2
     * @maxLength 12
     */
    roleName: string;

    /**
     * 角色描述（最多128个字符）
     * @maxLength 128
     */
    description?: string;
  }

  /**
   * 修改角色请求参数
   */
  interface RoleUpdateRq {
    /**
     * 角色编码（非空）
     */
    roleCode: string;

    /**
     * 角色名称（长度限制：2~12个字符）
     */
    roleName: string;

    /**
     * 角色描述（最大长度：128个字符）
     */
    description?: string;

    /**
     * 是否删除
     */
    del?: boolean;
  }
}

namespace Menu {
  interface MenuListRs {
    /**
     * 主键ID
     */
    id: number;

    /**
     * 菜单名
     */
    menuName: string;

    /**
     * 菜单路由
     */
    menuRouter: string;

    /**
     * 菜单图标
     */
    menuIcon: string;

    /**
     * 菜单顺序
     */
    menuOrder: number;

    /**
     * 是否显示在导航栏
     */
    navShow: boolean;

    /**
     * 创建时间
     */
    createTime: string; // 使用 string 表示 ISO 时间格式，如 "2025-04-18T12:00:00"

    /**
     * 更新时间
     */
    updateTime: string;

    /**
     * 父菜单ID
     */
    parentMenuId?: number;

    /**
     * 子菜单列表
     */
    children: MenuListRs[];
  }

  interface MenuRoleListRs extends MenuListRs {
    roles: string[]
  }

  interface MenuSaveRq {
    /**
     * 菜单名 - 必填且长度在2到12之间
     */
    menuName: string;

    /**
     * 菜单路由 - 可选
     */
    menuRouter?: string;

    /**
     * 菜单图标 - 必填且不能为空
     */
    menuIcon: string;

    /**
     * 菜单顺序 - 必填且不能为 null
     */
    menuOrder: number;

    /**
     * 是否显示在导航栏 - 必填且不能为 null
     */
    navShow: boolean;
  }

  interface MenuUpdateRq {
    /**
     * 菜单ID - 必填且最小值为1
     */
    id: number;

    /**
     * 菜单名 - 必填且长度在2到12之间
     */
    menuName: string;

    /**
     * 菜单路由 - 可选
     */
    menuRouter?: string;

    /**
     * 菜单图标 - 必填且不能为空
     */
    menuIcon: string;

    /**
     * 菜单顺序 - 必填且不能为 null
     */
    menuOrder: number;

    /**
     * 是否显示在导航栏 - 必填且不能为 null
     */
    navShow: boolean;
  }
}

export {PageRq, User, Role, Menu}
