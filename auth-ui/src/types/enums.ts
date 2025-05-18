export enum ApiMethod {
  ALL = 'ALL',
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
  PATCH = 'PATCH',
  OPTIONS = 'OPTIONS',
  HEAD = 'HEAD',
  TRACE = 'TRACE',
}

export enum MatchType {

  /**
   * ant
   */
  ANT = 'ANT',

  /**
   * 正则
   */
  REGEX = 'REGEX',
}

export enum AuthType {
  /**
   * 角色授权
   */
  ROLE = 'ROLE',

  /**
   * IP 地址授权
   */
  IP = 'IP',

  /**
   * 静态授权（无条件访问）
   */
  STATIC = 'STATIC',
}

export const staticAuth : {
  [key: string]: string
} = {
  "public": "公开访问",
  "deny": "禁止访问"
}

export const authType = [
  {
    type: AuthType.ROLE,
    label: "角色"
  },
  {
    type: AuthType.IP,
    label: "IP"
  },
  {
    type: AuthType.STATIC,
    label: "静态授权"
  }
]
