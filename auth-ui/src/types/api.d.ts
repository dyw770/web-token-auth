import {ApiMethod, AuthType, MatchType} from './enums.ts'

export interface OrderRq {
  /**
   * 排序的字段名
   */
  column: string,

  /**
   * 是否是升序
   */
  asc: boolean,
}

export interface PageRq {
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

interface TokenAuthenticationToken {
  token: string;
  createTime: LocalDateTime;
  loginUserAgent: string;
}

declare namespace User {

  interface UserSearchRq extends PageRq {
    username?: string | undefined;
    nickname?: string | undefined;
    enabled?: boolean | undefined;
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

  interface TokenWrapperRs {
    token: TokenAuthenticationToken;
    expireTime: string;
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

  /**
   * 新增菜单权限池请求参数
   *
   * @author dyw770
   * @since 2025-05-19
   */
  interface MenuPermissionSaveRq {
    /**
     * 菜单ID，必须为大于等于1的整数
     */
    menuId: number;

    /**
     * 权限ID，长度2~32的非空字符串
     */
    permissionId: string;

    /**
     * 权限说明，长度2~128的字符串（可为空）
     */
    permissionDesc?: string;
  }

  /**
   * 菜单权限池配置
   *
   * @author dyw770
   * @since 2025-05-19
   */
  interface SysMenuPermissionRs {
    /**
     * 菜单ID
     */
    menuId: number;

    /**
     * 权限ID
     */
    permissionId: string;

    /**
     * 权限说明
     */
    permissionDesc: string;

    /**
     * 创建时间（ISO 格式字符串）
     */
    createTime?: string;

    /**
     * 更新时间（ISO 格式字符串）
     */
    updateTime?: string;
  }

  interface MenuPermissionSaveRq {
    menuId: number;
    permissionId: string;
    permissionDesc: string;
  }

  /**
   * @author dyw770
   * @since 2025-05-19
   */
  interface SysRoleMenuPermissionRs {
    /**
     * 授权ID
     */
    authId: number;

    /**
     * 角色ID
     */
    roleCode: string;

    /**
     * 菜单ID
     */
    menuId: number;

    /**
     * 权限ID
     */
    permissionId: string;

    /**
     * 授权时间（ISO 格式字符串）
     */
    authTime: string;
  }

  interface RoleMenuPermissionRs {
    menuPermissions: SysMenuPermissionRs[]
    rolePermissions: SysRoleMenuPermissionRs[]
  }

  /**
   * 角色菜单授权请求参数
   *
   * @author dyw770
   * @since 2025-05-20
   */
  interface RoleMenuPermissionSaveRq {
    /**
     * 角色ID，长度 2~32 的非空字符串
     */
    roleCode: string;

    /**
     * 菜单ID，必须为非空且大于等于 1 的整数
     */
    menuId: number;

    /**
     * 权限ID，长度 2~128 的非空字符串
     */
    permissionId: string;
  }
}
namespace Resource {

  interface ResourceSearchRq extends PageRq {
    apiPath?: string
    apiMethod?: ApiMethod
    description?: string
    matchType?: MatchType
  }

  interface ResourceListRs {
    /**
     * 主键ID
     */
    id: number;

    /**
     * api路径
     */
    apiPath: string;

    /**
     * 匹配类型
     */
    matchType: MatchType;

    /**
     * api方法
     */
    apiMethod: string;

    /**
     * 描述
     */
    description: string;

    /**
     * 是否启用
     */
    enable: boolean;

    /**
     * 创建时间
     */
    createTime: string;

    /**
     * 更新时间
     */
    updateTime: string;
  }

  interface ResourceSaveRq {
    /**
     * API路径（必填，长度限制：1~128个字符）
     */
    apiPath: string;

    /**
     * 匹配类型
     */
    matchType: MatchType;

    /**
     * API方法（必填，长度限制：1~20个字符）
     */
    apiMethod: ApiMethod;

    /**
     * 描述（必填，长度限制：1~128个字符）
     */
    description: string;
  }

  interface ResourceUpdateRq {

    /**
     * 主键ID
     */
    id: number;

    /**
     * API路径（必填，长度限制：1~128个字符）
     */
    apiPath: string;

    /**
     * 匹配类型
     */
    matchType: MatchType;

    /**
     * API方法（必填，长度限制：1~20个字符）
     */
    apiMethod: ApiMethod;

    /**
     * 描述（必填，长度限制：1~128个字符）
     */
    description: string;
  }

  interface ResourceAuthRs {

    /**
     * 授权ID，对应数据库中的 id 字段。
     */
    authId: number

    /**
     * API资源ID，对应数据库中的 apiResourceId 字段。
     * 可为 null。
     */
    apiResourceId: number;

    /**
     * 授权类型，参考 AuthType 枚举。
     */
    authType: AuthType;

    /**
     * 授权对象，例如角色名、IP地址等。
     * 可为 null。
     */
    authObject: string;

    /**
     * 授权时间，ISO 8601 格式字符串。
     * 示例：2025-04-24T10:00:00
     * 可为 null。
     */
    authTime: string;

    /**
     * 授权过期时间，ISO 8601 格式字符串。
     * 示例：2025-04-24T18:00:00
     * 可为 null。
     */
    expiredTime: string;
  }

  interface ResourceAuth {

    /**
     * 授权类型
     */
    authType: AuthType;

    /**
     * 授权对象
     */
    authObject: string;

  }

  interface ResourceAuthAddRq extends ResourceAuth {

    /**
     * API资源ID，对应数据库中的 apiResourceId 字段。
     */
    apiResourceId: number;
  }

  interface ResourceAuthUpdateRq extends ResourceAuthAddRq {
    /**
     * 授权ID，对应数据库中的 id 字段。
     */
    authId: number
  }
}

/**
 * API访问日志类型
 */
export interface SysApiAccessLogRs {
  id: number;
  username: string;
  apiPath: string;
  apiMethod: string;
  apiAccessTime: string;
  apiAccessDuration: number;
  apiAccessIp: string;
  apiAccessUa: string;
  apiAccessResultType: string;
  apiAccessResultCode: number;
  apiAccessResponseCode: number;
}

/**
 * 系统操作日志类型
 */
export interface SysSystemOperationLogRs {
  id: number;
  username: string;
  operationTime: LocalDateTime;
  operationIp: string;
  operationUa: string;
  operationResultType: string;
  operationResultCode: number;
  operationContent: string;
  operationEvent: string;
}

export interface ApiAccessLogSearchRq extends PageRq {
  username?: string;
  apiPath?: string;
  startTime?: string;
  endTime?: string;
  apiAccessIp?: string;
}

export interface SystemOperationSearchRq extends PageRq {
  username?: string;
  startTime?: string;
  endTime?: string;
  operationEvent?: string;
}

export {User, Role, Menu, Resource}
