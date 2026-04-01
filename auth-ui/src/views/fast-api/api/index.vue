<template>
  <FaPageMain>
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="名称" prop="apiName">
          <el-input v-model="searchRq.apiName" placeholder="请输入名称" clearable/>
        </el-form-item>

        <el-form-item label="描述" prop="apiDescribe">
          <el-input v-model="searchRq.apiDescribe" placeholder="请输入描述" clearable/>
        </el-form-item>

        <el-form-item label="路径" prop="apiPath">
          <el-input v-model="searchRq.apiPath" placeholder="请输入路径" clearable/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="openAddDialog">新增API</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="apiList" border style="width: 100%" table-layout="auto">
      <el-table-column prop="id" label="ID" align="center" width="80"/>
      <el-table-column prop="apiName" label="名称" align="center"/>
      <el-table-column prop="apiDescribe" label="描述" align="center"/>
      <el-table-column prop="apiPath" label="路径" align="center"/>
      <el-table-column label="SQL模板" align="center" min-width="150">
        <template #default="{ row }">
          <template v-if="row.fastSql">
            <el-tag type="info">{{ row.fastSql.sqlName }}</el-tag>
            <el-tag :type="getStatementTypeTag(row.fastSql.statementType)" class="ml-1">
              {{ row.fastSql.statementType }}
            </el-tag>
          </template>
          <span v-else class="text-gray-400">未关联</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="180"/>
      <el-table-column prop="updateTime" label="更新时间" align="center" width="180"/>
      <el-table-column label="操作" align="center" width="280">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="success" @click="openExecDialog(row)" :disabled="!row.fastSql">执行</el-button>
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteApi(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :total="total"
      v-model:current-page="searchRq.page"
      v-model:page-size="searchRq.size"
      :page-sizes="[10, 50, 100]"
      @change="refresh"
      class="mt-5"/>

    <el-dialog v-model="showFormDialog" :title="isEdit ? '编辑API' : '新增API'" width="600px" :close-on-click-modal="false">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="名称" prop="apiName">
          <el-input v-model="form.apiName" placeholder="请输入名称" maxlength="32" show-word-limit/>
        </el-form-item>

        <el-form-item label="描述" prop="apiDescribe">
          <el-input v-model="form.apiDescribe" placeholder="请输入描述" maxlength="128" show-word-limit/>
        </el-form-item>

        <el-form-item label="路径" prop="apiPath">
          <el-input v-model="form.apiPath" placeholder="请输入路径，如 /api/user/list" maxlength="128" show-word-limit/>
        </el-form-item>

        <el-form-item label="SQL模板" prop="sysSql">
          <el-select v-model="form.sysSql" placeholder="请选择SQL模板" filterable class="w-full">
            <el-option
              v-for="sql in sqlList"
              :key="sql.id"
              :label="`${sql.sqlName} (${sql.statementType})`"
              :value="sql.id"/>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showFormDialog = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>

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
          <div class="mb-2 font-bold">执行结果</div>
          <div class="exec-result-container">
            <template v-if="execResult">
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
import {ElMessageBox, type FormInstance, type FormRules} from 'element-plus'
import {toast} from 'vue-sonner'

defineOptions({
  name: 'FastApiApiIndex',
})

const formRef = ref<FormInstance>()

const showFormDialog = ref(false)
const submitting = ref(false)
const editData = ref<FastApi.FastApiDetail | null>(null)

const showExecDialog = ref(false)
const execApiData = ref<FastApi.FastApiDetail | null>(null)
const execParams = ref<Record<string, unknown>>({})
const execResult = ref<FastSql.ExecResult | null>(null)
const executing = ref(false)
const execCurrentPage = ref(1)
const execPageSize = ref(20)

const searchRq = ref<FastApi.ApiSearchRq>({
  page: 1,
  size: 10,
  apiName: '',
  apiDescribe: '',
  apiPath: '',
})

const total = ref(0)
const apiList = ref<FastApi.FastApiDetail[]>([])
const sqlList = ref<FastSql.SysFastSql[]>([])

const form = ref({
  apiName: '',
  apiDescribe: '',
  apiPath: '',
  sysSql: undefined as number | undefined,
})

const formRules: FormRules = {
  apiName: [
    {required: true, message: '请输入名称', trigger: 'blur'},
    {min: 1, max: 32, message: '名称长度为1-32个字符', trigger: 'blur'},
  ],
  apiDescribe: [
    {required: true, message: '请输入描述', trigger: 'blur'},
    {min: 1, max: 128, message: '描述长度为1-128个字符', trigger: 'blur'},
  ],
  apiPath: [
    {required: true, message: '请输入路径', trigger: 'blur'},
    {min: 1, max: 128, message: '路径长度为1-128个字符', trigger: 'blur'},
  ],
  sysSql: [
    {required: true, message: '请选择SQL模板', trigger: 'change'},
  ],
}

const isEdit = computed(() => !!editData.value)

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

const getStatementTypeTag = (type: StatementType) => {
  const typeMap: Record<StatementType, string> = {
    select: 'success',
    update: 'warning',
    delete: 'danger',
    insert: 'primary',
  }
  return typeMap[type] || 'info'
}

const resetSearch = () => {
  searchRq.value.apiName = ''
  searchRq.value.apiDescribe = ''
  searchRq.value.apiPath = ''
  refresh()
}

const refresh = async () => {
  const {data} = await fastApi.apiDetailsList(searchRq.value)
  apiList.value = data.records
  total.value = data.total
}

const loadSqlList = async () => {
  const {data} = await fastApi.sqlList({page: 1, size: 1000})
  sqlList.value = data.records
}

const resetForm = () => {
  form.value = {
    apiName: '',
    apiDescribe: '',
    apiPath: '',
    sysSql: undefined,
  }
  editData.value = null
  formRef.value?.resetFields()
}

const openAddDialog = () => {
  resetForm()
  showFormDialog.value = true
}

const openEditDialog = (row: FastApi.FastApiDetail) => {
  editData.value = row
  form.value = {
    apiName: row.apiName,
    apiDescribe: row.apiDescribe,
    apiPath: row.apiPath,
    sysSql: row.sysSql,
  }
  showFormDialog.value = true
}

const openExecDialog = async (row: FastApi.FastApiDetail) => {
  await ElMessageBox.confirm(
    `确认执行API"${row.apiName}"?`,
    '执行API',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )

  execApiData.value = row
  execParams.value = {}
  execResult.value = null
  execCurrentPage.value = 1
  execPageSize.value = row.fastSql?.dataPage?.size || 20
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

const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (isEdit.value) {
        const rq: FastApi.ApiEditRq = {
          id: editData.value!.id,
          apiName: form.value.apiName,
          apiDescribe: form.value.apiDescribe,
          apiPath: form.value.apiPath,
          sysSql: form.value.sysSql!,
        }
        await fastApi.apiEdit(rq)
        toast.success('API更新成功')
      } else {
        const rq: FastApi.ApiCreateRq = {
          apiName: form.value.apiName,
          apiDescribe: form.value.apiDescribe,
          apiPath: form.value.apiPath,
          sysSql: form.value.sysSql!,
        }
        await fastApi.apiAdd(rq)
        toast.success('API创建成功')
      }

      showFormDialog.value = false
      await refresh()
    } catch (error) {
      toast.error(isEdit.value ? '更新失败' : '创建失败')
    } finally {
      submitting.value = false
    }
  })
}

const deleteApi = async (row: FastApi.FastApiDetail) => {
  await ElMessageBox.confirm(
    `确认删除API"${row.apiName}"?`,
    '删除API',
    {
      distinguishCancelAndClose: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )
  await fastApi.apiDelete(row.id)
  toast.success('API删除成功')
  await refresh()
}

onMounted(async () => {
  await Promise.all([refresh(), loadSqlList()])
})
</script>

<style scoped>
.w-full {
  width: 100%;
}

.ml-1 {
  margin-left: 4px;
}

.text-gray-400 {
  color: #9ca3af;
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
</style>
