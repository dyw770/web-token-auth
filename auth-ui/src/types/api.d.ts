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
     * 是否已删除的标志。
     */
    del: boolean;

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
    parentRoleCode: string;

    /**
     * 子角色列表，表示当前角色的直接子角色集合。
     */
    children: RoleListRs[];
  }
}

export {PageRq, User, Role}
