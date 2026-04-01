import api from '../index'
import type {FastApi, FastSql} from '#/fast-api'

export default {
  // ==================== SQL模板管理 ====================

  /**
   * 查询SQL模板列表
   */
  sqlList: (rq: FastSql.SqlSearchRq) => api.post('/sql/list', rq),

  /**
   * 添加SQL模板
   */
  sqlAdd: (rq: FastSql.SqlCreateRq) => api.post('/sql/add', rq),

  /**
   * 编辑SQL模板
   */
  sqlEdit: (rq: FastSql.SqlEditRq) => api.put('/sql/edit', rq),

  /**
   * 删除SQL模板
   */
  sqlDelete: (id: number) => api.delete(`/sql/delete/${id}`),

  /**
   * 执行SQL模板
   */
  sqlExec: (sqlId: number, rq: FastSql.ExecParameterRq) => api.post(`/sql/exec/${sqlId}`, rq),

  // ==================== API管理 ====================

  /**
   * 查询API列表
   */
  apiList: (rq: FastApi.ApiSearchRq) => api.post('/api/list', rq),

  /**
   * 查询API详情列表（包含关联的SQL模板信息）
   */
  apiDetailsList: (rq: FastApi.ApiSearchRq) => api.post('/api/details/list', rq),

  /**
   * 添加API
   */
  apiAdd: (rq: FastApi.ApiCreateRq) => api.post('/api/add', rq),

  /**
   * 编辑API
   */
  apiEdit: (rq: FastApi.ApiEditRq) => api.put('/api/edit', rq),

  /**
   * 删除API
   */
  apiDelete: (id: number) => api.delete(`/api/delete/${id}`),

  // ==================== SQL执行 ====================

  /**
   * 执行SQL
   */
  exec: (rq: FastSql.ExecRq) => api.post('/exec', rq),

  // ==================== API执行 ====================

  /**
   * 执行API查询
   * @param apiPath API路径
   * @param rq 执行参数
   */
  execApi: (apiPath: string, rq: FastSql.ExecParameterRq) => api.post(`/query${apiPath}`, rq),
}
