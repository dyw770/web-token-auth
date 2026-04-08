import type {PageRq} from './api'

/**
 * SQL语句类型
 */
export type StatementType = 'select' | 'update' | 'delete' | 'insert'

/**
 * 分页类型
 */
export type PageType = 'simple' | 'fullCustom'

/**
 * 排序字段
 */
export interface DataSortField {
  /**
   * 排序字段名
   */
  fieldName: string

  /**
   * 是否升序，默认true
   */
  asc?: boolean
}

/**
 * 分页配置
 */
export interface DataPageOption {
  /**
   * 分页类型，默认simple
   */
  pageType?: PageType

  /**
   * 页码，从0开始
   */
  page?: number

  /**
   * 每页大小，默认20
   */
  size?: number

  /**
   * 是否需要分页，默认false
   */
  needPage?: boolean
}

/**
 * 字段绑定
 */
export interface DataFieldBind {
  /**
   * 真实字段名
   */
  headerField: string

  /**
   * 映射字段名
   */
  headerName: string
}

/**
 * 动态过滤参数
 */
export interface DynamicFilterParameter {
  /**
   * 参数名
   */
  parameterName?: string

  /**
   * 参数描述
   */
  parameterDesc?: string

  /**
   * 参数类型
   */
  parameterType?: unknown

  /**
   * 参数值
   */
  parameterValue?: unknown
}

declare namespace FastSql {
  /**
   * SQL模板搜索请求
   */
  interface SqlSearchRq extends PageRq {
    /**
     * 名称
     */
    sqlName?: string

    /**
     * 描述
     */
    sqlDescribe?: string
  }

  /**
   * SQL模板创建请求
   */
  interface SqlCreateRq {
    /**
     * 名称，必填，1-32字符
     */
    sqlName: string

    /**
     * 描述，必填，1-128字符
     */
    sqlDescribe: string

    /**
     * SQL模板，必填，1-2000字符
     */
    sqlTemplate: string

    /**
     * 自定义计数SQL，最大2000字符
     */
    customCountSql?: string

    /**
     * 语句类型，必填
     */
    statementType: StatementType

    /**
     * 排序字段，必填
     */
    sortFields: DataSortField[]

    /**
     * 参数，必填
     */
    parameters: DynamicFilterParameter[]

    /**
     * 分页参数，必填
     */
    dataPage: DataPageOption

    /**
     * 扩展字段，必填
     */
    extend: Record<string, unknown>

    /**
     * 数据字段绑定，必填
     */
    dataFieldBinds: DataFieldBind[]

    /**
     * 数据源名称
     */
    dataSource?: string
  }

  /**
   * SQL模板编辑请求
   */
  interface SqlEditRq {
    /**
     * ID，必填
     */
    id: number

    /**
     * 名称，必填，1-32字符
     */
    sqlName: string

    /**
     * 描述，必填，1-128字符
     */
    sqlDescribe: string

    /**
     * SQL模板，必填，1-2000字符
     */
    sqlTemplate: string

    /**
     * 自定义计数SQL，最大2000字符
     */
    customCountSql?: string

    /**
     * 语句类型，必填
     */
    statementType: StatementType

    /**
     * 排序字段，必填
     */
    sortFields: DataSortField[]

    /**
     * 参数，必填
     */
    parameters: DynamicFilterParameter[]

    /**
     * 分页参数，必填
     */
    dataPage: DataPageOption

    /**
     * 扩展字段，必填
     */
    extend: Record<string, unknown>

    /**
     * 数据字段绑定，必填
     */
    dataFieldBinds: DataFieldBind[]

    /**
     * 数据源名称
     */
    dataSource?: string
  }

  /**
   * SQL模板响应
   */
  interface SysFastSql {
    /**
     * ID
     */
    id: number

    /**
     * 名称
     */
    sqlName: string

    /**
     * 描述
     */
    sqlDescribe: string

    /**
     * SQL模板
     */
    sqlTemplate: string

    /**
     * 自定义计数SQL
     */
    customCountSql?: string

    /**
     * 创建时间
     */
    createTime: string

    /**
     * 更新时间
     */
    updateTime: string

    /**
     * 语句类型
     */
    statementType: StatementType

    /**
     * 排序字段
     */
    sortFields?: DataSortField[]

    /**
     * 参数
     */
    parameters?: DynamicFilterParameter[]

    /**
     * 分页参数
     */
    dataPage?: DataPageOption

    /**
     * 扩展字段
     */
    extend?: Record<string, unknown>

    /**
     * 数据字段绑定
     */
    dataFieldBinds?: DataFieldBind[]

    /**
     * 数据源名称
     */
    dataSource?: string
  }

  /**
   * 执行参数请求
   */
  interface ExecParameterRq {
    /**
     * 排序字段，必填
     */
    sortFields: DataSortField[]

    /**
     * 参数，必填
     */
    parameters: DynamicFilterParameter[]

    /**
     * 分页配置，必填
     */
    dataPage: DataPageOption
  }

  /**
   * 执行SQL请求
   */
  interface ExecRq {
    /**
     * SQL，必填，最大2000字符
     */
    sql: string

    /**
     * 排序字段，必填
     */
    sortFields: DataSortField[]

    /**
     * 参数，必填
     */
    parameters: DynamicFilterParameter[]

    /**
     * 结果表头配置，必填
     */
    dataFieldBinds: DataFieldBind[]

    /**
     * 分页配置，必填
     */
    dataPage: DataPageOption

    /**
     * 语句类型，必填
     */
    statementType: StatementType

    /**
     * 自定义统计SQL，最大2000字符
     */
    customCountSql?: string

    /**
     * 扩展参数，必填
     */
    extend: Record<string, unknown>

    /**
     * 数据源名称
     */
    dataSource?: string
  }

  /**
   * 执行结果 - 查询结果
   */
  interface SelectExecResult {
    /**
     * 字段列表
     */
    schema: string[]

    /**
     * 数据列表
     */
    data: Record<string, unknown>[]

    /**
     * 字段映射
     */
    headerNames?: Record<string, DataFieldBind>

    /**
     * 排序方式
     */
    sorts?: DataSortField[]

    /**
     * 扩展参数
     */
    extend?: Record<string, unknown>

    /**
     * 参数
     */
    parameters?: DynamicFilterParameter[]
  }

  /**
   * 执行结果 - 分页查询结果
   */
  interface PageSelectExecResult extends SelectExecResult {
    /**
     * 总数
     */
    total: number

    /**
     * 当前页码
     */
    page: number

    /**
     * 每页大小
     */
    size: number
  }

  /**
   * 执行结果 - 影响行数
   */
  interface ExecNumberResult {
    /**
     * 影响行数
     */
    number: number
  }

  /**
   * 执行结果
   */
  type ExecResult = SelectExecResult | PageSelectExecResult | ExecNumberResult
}

declare namespace FastApi {
  /**
   * API搜索请求
   */
  interface ApiSearchRq extends PageRq {
    /**
     * 名称
     */
    apiName?: string

    /**
     * 描述
     */
    apiDescribe?: string

    /**
     * 路径
     */
    apiPath?: string
  }

  /**
   * API创建请求
   */
  interface ApiCreateRq {
    /**
     * 名称，必填，1-32字符
     */
    apiName: string

    /**
     * 描述，必填，1-128字符
     */
    apiDescribe: string

    /**
     * 路径，必填，1-128字符
     */
    apiPath: string

    /**
     * SQL ID，必填
     */
    sysSql: number
  }

  /**
   * API编辑请求
   */
  interface ApiEditRq {
    /**
     * ID，必填
     */
    id: number

    /**
     * 名称，必填，1-32字符
     */
    apiName: string

    /**
     * 描述，必填，1-128字符
     */
    apiDescribe: string

    /**
     * 路径，必填，1-128字符
     */
    apiPath: string

    /**
     * SQL ID，必填
     */
    sysSql: number
  }

  /**
   * API响应
   */
  interface SysFastApi {
    /**
     * ID
     */
    id: number

    /**
     * 名称
     */
    apiName: string

    /**
     * 描述
     */
    apiDescribe: string

    /**
     * 路径
     */
    apiPath: string

    /**
     * SQL ID
     */
    sysSql: number

    /**
     * 创建时间
     */
    createTime: string

    /**
     * 更新时间
     */
    updateTime: string
  }

  /**
   * API详情响应（包含关联的SQL模板信息）
   */
  interface FastApiDetail extends SysFastApi {
    /**
     * 关联的SQL模板
     */
    fastSql: FastSql.SysFastSql | null
  }
}

declare namespace DataSource {
  /**
   * 数据源响应
   */
  interface SysFastDataSource {
    /**
     * 数据源名称
     */
    sourceName: string

    /**
     * jdbc url
     */
    jdbcUrl: string

    /**
     * 用户名
     */
    username?: string

    /**
     * 密码
     */
    password?: string

    /**
     * 驱动名称
     */
    driverName: string

    /**
     * 属性
     */
    properties?: string

    /**
     * 数据库类型
     */
    dbType: string

    /**
     * 创建时间
     */
    createTime: string

    /**
     * 更新时间
     */
    updateTime: string
  }

  /**
   * 数据源创建请求
   */
  interface DataSourceCreateRq {
    /**
     * 数据源名称
     */
    sourceName: string

    /**
     * jdbc url
     */
    jdbcUrl: string

    /**
     * 用户名
     */
    username?: string

    /**
     * 密码
     */
    password?: string

    /**
     * 驱动名称
     */
    driverName: string

    /**
     * 属性
     */
    properties?: string

    /**
     * 数据库类型
     */
    dbType: string
  }

  /**
   * 数据源编辑请求
   */
  interface DataSourceEditRq {
    /**
     * 数据源名称
     */
    sourceName: string

    /**
     * jdbc url
     */
    jdbcUrl: string

    /**
     * 用户名
     */
    username?: string

    /**
     * 密码
     */
    password?: string

    /**
     * 驱动名称
     */
    driverName: string

    /**
     * 属性
     */
    properties?: string

    /**
     * 数据库类型
     */
    dbType: string
  }
}

export {FastSql, FastApi, DataSource}


