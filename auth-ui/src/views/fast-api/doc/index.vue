<template>
  <FaPageMain>
    <div class="p-4">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-xl font-bold">API 文档</h2>
        <el-button type="primary" @click="exportOpenApi">导出 OpenAPI</el-button>
      </div>

      <el-input
        v-model="searchKeyword"
        placeholder="搜索API名称或路径"
        clearable
        class="mb-4 w-80"/>

      <el-collapse v-model="activeApiIds" v-if="filteredApiList.length > 0">
        <el-collapse-item
          v-for="api in filteredApiList"
          :key="api.id"
          :name="api.id">
          <template #title>
            <div class="flex items-center gap-3">
              <el-tag :type="getMethodTag(api.fastSql?.statementType)">
                {{ api.fastSql?.statementType?.toUpperCase() || 'UNKNOWN' }}
              </el-tag>
              <span class="font-mono text-sm">{{ api.apiPath }}</span>
              <span class="text-gray-500">{{ api.apiName }}</span>
            </div>
          </template>

          <div class="api-detail">
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="API名称">{{ api.apiName }}</el-descriptions-item>
              <el-descriptions-item label="API路径">
                <div class="flex items-center gap-2">
                  <span class="font-mono text-sm">{{ api.apiPath }}</span>
                  <el-button size="small" link type="primary" @click="copyPath(api.apiPath)">
                    <el-icon><CopyDocument/></el-icon>
                  </el-button>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="描述" :span="2">{{ api.apiDescribe }}</el-descriptions-item>
              <el-descriptions-item label="SQL模板">
                <template v-if="api.fastSql">
                  <el-tag type="info" size="small">{{ api.fastSql.sqlName }}</el-tag>
                </template>
                <span v-else class="text-gray-400">未关联</span>
              </el-descriptions-item>
              <el-descriptions-item label="语句类型">
                <el-tag :type="getMethodTag(api.fastSql?.statementType)" size="small">
                  {{ api.fastSql?.statementType?.toUpperCase() || 'UNKNOWN' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="开启分页">
                {{ api.fastSql?.dataPage?.needPage ? '是' : '否' }}
              </el-descriptions-item>
              <el-descriptions-item label="单行数据">
                {{ (api.fastSql?.extend as Record<string, unknown>)?.single ? '是' : '否' }}
              </el-descriptions-item>
            </el-descriptions>

            <div v-if="api.fastSql?.parameters && api.fastSql.parameters.length > 0" class="mt-4">
              <div class="font-bold mb-2">请求参数</div>
              <el-table :data="api.fastSql.parameters" border size="small">
                <el-table-column prop="parameterName" label="参数名" min-width="120"/>
                <el-table-column prop="parameterDesc" label="描述" min-width="150"/>
                <el-table-column prop="parameterType" label="类型" width="100">
                  <template #default="{ row }">
                    <el-tag size="small">{{ row.parameterType }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="必填" width="80" align="center">
                  <template #default>
                    <el-tag type="danger" size="small">是</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div v-if="api.fastSql?.dataFieldBinds && api.fastSql.dataFieldBinds.length > 0" class="mt-4">
              <div class="font-bold mb-2">响应字段映射</div>
              <el-table :data="api.fastSql.dataFieldBinds" border size="small">
                <el-table-column prop="headerField" label="原始字段名" min-width="120">
                  <template #default="{ row }">
                    <span class="font-mono text-sm">{{ row.headerField }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="headerName" label="显示名称" min-width="120"/>
                <el-table-column label="类型" width="100" align="center">
                  <template #default>
                    <el-tag size="small" type="info">string</el-tag>
                  </template>
                </el-table-column>
              </el-table>

              <div class="mt-3 p-3 bg-gray-50 rounded">
                <div class="text-sm font-bold mb-2">响应示例</div>
                <pre class="text-xs bg-gray-100 p-2 rounded overflow-auto max-h-48"><code>{{ getResponseExample(api) }}</code></pre>
              </div>
            </div>

            <div v-if="api.fastSql?.statementType === 'select' && (!api.fastSql?.dataFieldBinds || api.fastSql.dataFieldBinds.length === 0)" class="mt-4">
              <div class="font-bold mb-2">响应结构</div>
              <el-alert type="info" :closable="false">
                <template #title>
                  <span class="text-sm">此API未配置字段映射，返回字段由SQL查询结果决定</span>
                </template>
              </el-alert>
            </div>

            <div class="mt-4">
              <el-button type="success" @click="openExecDialog(api)" :disabled="!api.fastSql">
                执行
              </el-button>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>

      <el-empty v-else description="暂无API数据"/>
    </div>

    <el-dialog v-model="showExecDialog" title="执行API" width="80%" top="5vh" :close-on-click-modal="false">
      <div class="exec-dialog-container">
        <div class="exec-left-panel">
          <div class="mb-2 font-bold">参数配置</div>
          <el-form v-if="execApiData" :model="execParams" label-width="120px">
            <el-form-item
              v-for="param in execApiData.fastSql?.parameters"
              :key="param.parameterName"
              :label="param.parameterDesc || param.parameterName">
              <el-input
                v-if="param.parameterType === 'string'"
                v-model="execParams[param.parameterName || '']"
                :placeholder="`请输入${param.parameterDesc || param.parameterName}`"/>
              <el-input-number
                v-else-if="param.parameterType === 'integer'"
                v-model="execParams[param.parameterName || '']"
                :placeholder="`请输入${param.parameterDesc || param.parameterName}`"
                style="width: 100%"/>
              <el-input-number
                v-else-if="param.parameterType === 'decimal'"
                v-model="execParams[param.parameterName || '']"
                :precision="2"
                :placeholder="`请输入${param.parameterDesc || param.parameterName}`"
                style="width: 100%"/>
              <el-date-picker
                v-else-if="param.parameterType === 'date'"
                v-model="execParams[param.parameterName || '']"
                type="date"
                :placeholder="`请选择${param.parameterDesc || param.parameterName}`"
                style="width: 100%"/>
              <el-switch
                v-else-if="param.parameterType === 'boolean'"
                v-model="execParams[param.parameterName || '']"/>
              <el-input
                v-else
                v-model="execParams[param.parameterName || '']"
                :placeholder="`请输入${param.parameterDesc || param.parameterName}`"/>
            </el-form-item>
            <el-empty v-if="!execApiData.fastSql?.parameters || execApiData.fastSql.parameters.length === 0" description="无需配置参数"/>
          </el-form>
          <div class="mt-4">
            <el-button type="primary" @click="executeApi" :loading="executing">执行</el-button>
          </div>
        </div>

        <div class="exec-right-panel">
          <div class="flex items-center justify-between mb-2">
            <span class="font-bold">执行结果</span>
            <el-radio-group v-if="execResult" v-model="resultViewMode" size="small">
              <el-radio-button label="table">表格</el-radio-button>
              <el-radio-button label="json">JSON</el-radio-button>
            </el-radio-group>
          </div>
          <div class="exec-result-container">
            <template v-if="execResult">
              <template v-if="resultViewMode === 'json'">
                <pre class="json-viewer"><code>{{ JSON.stringify(execResult, null, 2) }}</code></pre>
              </template>
              <template v-else>
                <template v-if="isSelectResult">
                  <template v-if="isSingleRowResult">
                    <div class="mb-2 text-sm text-gray-500">单行数据结果</div>
                    <el-descriptions :column="1" border>
                      <el-descriptions-item
                        v-for="col in displayColumns"
                        :key="col.field"
                        :label="col.displayName">
                        {{ (selectResult?.data as Record<string, unknown>)?.[col.field] ?? '-' }}
                      </el-descriptions-item>
                    </el-descriptions>
                  </template>
                  <template v-else>
                    <div class="mb-2 text-sm text-gray-500">
                      共 {{ isPageResult ? pageResult?.total : (selectResult?.data as Record<string, unknown>[])?.length || 0 }} 条数据
                    </div>
                    <el-table :data="selectResult?.data as Record<string, unknown>[]" border max-height="400" table-layout="auto">
                      <el-table-column
                        v-for="col in displayColumns"
                        :key="col.field"
                        :prop="col.field"
                        :label="col.displayName"
                        align="center"
                        min-width="120"/>
                    </el-table>
                    <div v-if="isPageResult && pageResult" class="mt-4 flex justify-end">
                      <el-pagination
                        v-model:current-page="execCurrentPage"
                        v-model:page-size="execPageSize"
                        :page-sizes="[10, 20, 50, 100]"
                        :total="pageResult.total"
                        layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleExecPageSizeChange"
                        @current-change="handleExecPageChange"/>
                    </div>
                  </template>
                </template>
                <template v-else>
                  <el-alert type="success" :closable="false">
                    <template #title>
                      <span class="text-lg">执行成功，影响行数: {{ numberResult?.number || 0 }}</span>
                    </template>
                  </el-alert>
                </template>
              </template>
            </template>
            <template v-else>
              <el-empty description="暂无执行结果，请配置参数后点击执行"/>
            </template>
          </div>
        </div>
      </div>
    </el-dialog>
  </FaPageMain>
</template>

<script setup lang="ts">
import fastApi from '@/api/modules/fast-api'
import type {DynamicFilterParameter, FastApi, FastSql, StatementType} from '#/fast-api'
import {toast} from 'vue-sonner'
import {CopyDocument} from '@element-plus/icons-vue'

defineOptions({
  name: 'FastApiDocIndex',
})

const searchKeyword = ref('')
const apiList = ref<FastApi.FastApiDetail[]>([])
const activeApiIds = ref<number[]>([])

const showExecDialog = ref(false)
const execApiData = ref<FastApi.FastApiDetail | null>(null)
const execParams = ref<Record<string, unknown>>({})
const execResult = ref<FastSql.ExecResult | null>(null)
const executing = ref(false)
const execCurrentPage = ref(1)
const execPageSize = ref(20)
const resultViewMode = ref<'table' | 'json'>('table')

const filteredApiList = computed(() => {
  if (!searchKeyword.value) return apiList.value
  const keyword = searchKeyword.value.toLowerCase()
  return apiList.value.filter(api =>
    api.apiName.toLowerCase().includes(keyword) ||
    api.apiPath.toLowerCase().includes(keyword)
  )
})

const isSelectResult = computed(() => {
  return execResult.value && 'schema' in execResult.value
})

const isPageResult = computed(() => {
  return execResult.value && 'total' in execResult.value
})

const isSingleRowResult = computed(() => {
  if (!isSelectResult.value || !selectResult.value?.data) return false
  return !Array.isArray(selectResult.value.data)
})

const selectResult = computed(() => {
  if (isSelectResult.value) {
    return execResult.value as FastSql.SelectExecResult
  }
  return null
})

const pageResult = computed(() => {
  if (isPageResult.value) {
    return execResult.value as FastSql.PageSelectExecResult
  }
  return null
})

const numberResult = computed(() => {
  if (!isSelectResult.value && execResult.value) {
    return execResult.value as FastSql.ExecNumberResult
  }
  return null
})

const displayColumns = computed(() => {
  if (!selectResult.value?.schema) return []
  const binds = execApiData.value?.fastSql?.dataFieldBinds || []
  const bindMap = new Map(binds.map(b => [b.headerField, b.headerName]))
  return selectResult.value.schema.map(field => ({
    field,
    displayName: bindMap.get(field) || field
  }))
})

const getMethodTag = (type?: StatementType) => {
  const typeMap: Record<string, string> = {
    select: 'success',
    update: 'warning',
    delete: 'danger',
    insert: 'primary',
  }
  return typeMap[type || ''] || 'info'
}

const copyPath = async (path: string) => {
  try {
    await navigator.clipboard.writeText(path)
    toast.success('路径已复制到剪贴板')
  } catch {
    toast.error('复制失败')
  }
}

const getResponseExample = (api: FastApi.FastApiDetail): string => {
  if (!api.fastSql) return '{}'

  const isSingle = (api.fastSql.extend as Record<string, unknown>)?.single
  const needPage = api.fastSql.dataPage?.needPage

  if (api.fastSql.statementType !== 'select') {
    return JSON.stringify({ number: 1 }, null, 2)
  }

  const fieldBinds = api.fastSql.dataFieldBinds || []

  if (fieldBinds.length === 0) {
    if (isSingle) {
      return JSON.stringify({}, null, 2)
    }
    if (needPage) {
      return JSON.stringify({
        data: [{}],
        total: 0,
        page: 0,
        size: 20
      }, null, 2)
    }
    return JSON.stringify([{}], null, 2)
  }

  const exampleItem: Record<string, string> = {}
  fieldBinds.forEach(bind => {
    exampleItem[bind.headerField] = bind.headerName
  })

  if (isSingle) {
    return JSON.stringify(exampleItem, null, 2)
  }

  if (needPage) {
    return JSON.stringify({
      data: [exampleItem],
      total: 100,
      page: 0,
      size: 20
    }, null, 2)
  }

  return JSON.stringify([exampleItem], null, 2)
}

const loadApiList = async () => {
  const {data} = await fastApi.apiDetailsList({page: 1, size: 1000})
  apiList.value = data.records
}

const openExecDialog = (api: FastApi.FastApiDetail) => {
  execApiData.value = api
  execParams.value = {}
  execResult.value = null
  execCurrentPage.value = 1
  execPageSize.value = api.fastSql?.dataPage?.size || 20
  resultViewMode.value = 'table'
  showExecDialog.value = true
}

const executeApi = async () => {
  if (!execApiData.value) return

  executing.value = true
  try {
    const params: DynamicFilterParameter[] = Object.entries(execParams.value)
      .filter(([, value]) => value !== undefined && value !== '')
      .map(([name, value]) => ({
        parameterName: name,
        parameterValue: value
      }))

    const rq: FastSql.ExecParameterRq = {
      sortFields: execApiData.value.fastSql?.sortFields || [],
      parameters: params,
      dataPage: {
        needPage: execApiData.value.fastSql?.dataPage?.needPage || false,
        page: execApiData.value.fastSql?.dataPage?.needPage ? execCurrentPage.value - 1 : 0,
        size: execPageSize.value
      }
    }

    const {data} = await fastApi.execApi(execApiData.value.apiPath, rq)
    execResult.value = data
    toast.success('执行成功')
  } catch (error) {
    toast.error('执行失败')
  } finally {
    executing.value = false
  }
}

const handleExecPageChange = () => {
  executeApi()
}

const handleExecPageSizeChange = () => {
  execCurrentPage.value = 1
  executeApi()
}

const getOpenApiType = (paramType?: string): string => {
  const typeMap: Record<string, string> = {
    string: 'string',
    integer: 'integer',
    decimal: 'number',
    date: 'string',
    boolean: 'boolean',
  }
  return typeMap[paramType || ''] || 'string'
}

const exportOpenApi = () => {
  const openApi = {
    openapi: '3.0.0',
    info: {
      title: 'Fast API 文档',
      version: '1.0.0',
      description: '自动生成的API文档',
    },
    paths: {} as Record<string, unknown>,
  }

  apiList.value.forEach(api => {
    if (!api.fastSql) return

    const path = api.apiPath
    const method = api.fastSql.statementType === 'select' ? 'get' : 'post'

    const parameters = (api.fastSql.parameters || []).map(param => ({
      name: param.parameterName,
      in: 'query',
      description: param.parameterDesc,
      required: true,
      schema: {
        type: getOpenApiType(param.parameterType as string),
      },
    }))

    const responseSchema = api.fastSql.statementType === 'select'
      ? {
          type: 'object',
          properties: (api.fastSql.dataFieldBinds || []).reduce((acc, bind) => {
            acc[bind.headerField] = { type: 'string', description: bind.headerName }
            return acc
          }, {} as Record<string, unknown>),
        }
      : {
          type: 'object',
          properties: {
            number: { type: 'integer', description: '影响行数' },
          },
        }

    ;(openApi.paths as Record<string, unknown>)[path] = {
      [method]: {
        summary: api.apiName,
        description: api.apiDescribe,
        parameters,
        responses: {
          '200': {
            description: '成功',
            content: {
              'application/json': {
                schema: responseSchema,
              },
            },
          },
        },
      },
    }
  })

  const blob = new Blob([JSON.stringify(openApi, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'openapi.json'
  a.click()
  URL.revokeObjectURL(url)
  toast.success('导出成功')
}

onMounted(() => {
  loadApiList()
})
</script>

<style scoped>
.api-detail {
  padding: 16px;
}

.exec-dialog-container {
  display: flex;
  gap: 16px;
  height: 60vh;
  overflow: hidden;
}

.exec-left-panel {
  flex: 0 0 35%;
  display: flex;
  flex-direction: column;
  min-width: 0;
  overflow: auto;
}

.exec-right-panel {
  flex: 0 0 65%;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #e4e7ed;
  padding-left: 16px;
  min-width: 0;
}

.exec-result-container {
  flex: 1;
  overflow: auto;
  min-height: 0;
}

.font-mono {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
}

.w-80 {
  width: 20rem;
}

.json-viewer {
  background-color: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
  overflow: auto;
  max-height: 100%;
}
</style>
