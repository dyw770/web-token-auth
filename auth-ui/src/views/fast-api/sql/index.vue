<template>
  <FaPageMain>
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="名称" prop="sqlName">
          <el-input v-model="searchRq.sqlName" placeholder="请输入名称" clearable/>
        </el-form-item>

        <el-form-item label="描述" prop="sqlDescribe">
          <el-input v-model="searchRq.sqlDescribe" placeholder="请输入描述" clearable/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="openAddDialog">新增SQL模板</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="sqlList" border style="width: 100%" table-layout="auto">
      <el-table-column prop="id" label="ID" align="center" width="80"/>
      <el-table-column prop="sqlName" label="名称" align="center"/>
      <el-table-column prop="sqlDescribe" label="描述" align="center"/>
      <el-table-column prop="statementType" label="语句类型" align="center" width="100"/>
      <el-table-column prop="createTime" label="创建时间" align="center" width="180"/>
      <el-table-column prop="updateTime" label="更新时间" align="center" width="180"/>
      <el-table-column label="操作" align="center" width="320">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="success" @click="openExecDialog(row)">执行</el-button>
            <el-button size="small" type="info" @click="openViewDrawer(row)">详情</el-button>
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteSql(row)">删除</el-button>
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

    <SqlForm v-model="showFormDialog" :sql-data="editSql" @success="refresh"/>

    <el-drawer v-model="showSqlDrawer" title="详情" size="50%">
      <template v-if="viewSqlData">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="模板名称">{{ viewSqlData.sqlName }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ viewSqlData.sqlDescribe }}</el-descriptions-item>
          <el-descriptions-item label="语句类型">{{ viewSqlData.statementType }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ viewSqlData.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ viewSqlData.updateTime }}</el-descriptions-item>
        </el-descriptions>

        <div class="mt-4">
          <div class="mb-2 font-bold">SQL模板</div>
          <el-input
            v-model="viewSqlData.sqlTemplate"
            type="textarea"
            :rows="15"
            readonly
            class="sql-viewer"/>
        </div>

        <div v-if="viewSqlData.customCountSql" class="mt-4">
          <div class="mb-2 font-bold">自定义计数SQL</div>
          <el-input
            v-model="viewSqlData.customCountSql"
            type="textarea"
            :rows="5"
            readonly/>
        </div>

        <div v-if="viewSqlData.parameters && viewSqlData.parameters.length > 0" class="mt-4">
          <div class="mb-2 font-bold">参数配置</div>
          <el-table :data="viewSqlData.parameters" border size="small">
            <el-table-column prop="parameterName" label="参数名" min-width="100"/>
            <el-table-column prop="parameterDesc" label="描述" min-width="120"/>
            <el-table-column prop="parameterType" label="类型" width="100"/>
          </el-table>
        </div>

        <div v-if="viewSqlData.dataFieldBinds && viewSqlData.dataFieldBinds.length > 0" class="mt-4">
          <div class="mb-2 font-bold">字段映射</div>
          <el-table :data="viewSqlData.dataFieldBinds" border size="small">
            <el-table-column prop="headerField" label="原字段名" min-width="120"/>
            <el-table-column prop="headerName" label="显示名称" min-width="120"/>
          </el-table>
        </div>

        <div class="mt-4">
          <div class="mb-2 font-bold">其他配置</div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="开启分页">{{ viewSqlData.dataPage?.needPage ? '是' : '否' }}</el-descriptions-item>
            <el-descriptions-item v-if="viewSqlData.dataPage?.needPage" label="每页大小">{{ viewSqlData.dataPage?.size || 20 }}</el-descriptions-item>
            <el-descriptions-item label="单行数据">{{ (viewSqlData.extend as Record<string, unknown>)?.single ? '是' : '否' }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </template>
    </el-drawer>

    <el-dialog v-model="showExecDialog" title="执行SQL" width="80%" top="5vh" :close-on-click-modal="false">
      <div class="exec-dialog-container">
        <div class="exec-left-panel">
          <div class="mb-2 font-bold">参数配置</div>
          <el-form v-if="execSqlData" :model="execParams" label-width="120px">
            <el-form-item
              v-for="param in execSqlData.parameters"
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
            <el-empty v-if="!execSqlData.parameters || execSqlData.parameters.length === 0" description="无需配置参数"/>
          </el-form>
          <div class="mt-4">
            <el-button type="primary" @click="executeSql" :loading="executing">执行</el-button>
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
import type {DynamicFilterParameter, FastSql} from '#/fast-api'
import {ElMessageBox} from 'element-plus'
import {toast} from 'vue-sonner'
import SqlForm from './form.vue'

defineOptions({
  name: 'FastApiSqlIndex',
})

const showFormDialog = ref(false)
const editSql = ref<FastSql.SysFastSql | undefined>()

const showSqlDrawer = ref(false)
const viewSqlData = ref<FastSql.SysFastSql | null>(null)

const showExecDialog = ref(false)
const execSqlData = ref<FastSql.SysFastSql | null>(null)
const execParams = ref<Record<string, unknown>>({})
const execResult = ref<FastSql.ExecResult | null>(null)
const executing = ref(false)
const execCurrentPage = ref(1)
const execPageSize = ref(20)

const searchRq = ref<FastSql.SqlSearchRq>({
  page: 1,
  size: 10,
  sqlName: '',
  sqlDescribe: '',
})

const total = ref(0)
const sqlList = ref<FastSql.SysFastSql[]>([])

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
  const binds = execSqlData.value?.dataFieldBinds || []
  const bindMap = new Map(binds.map(b => [b.headerField, b.headerName]))
  return selectResult.value.schema.map(field => ({
    field,
    displayName: bindMap.get(field) || field
  }))
})

const resetSearch = () => {
  searchRq.value.sqlName = ''
  searchRq.value.sqlDescribe = ''
  refresh()
}

const refresh = async () => {
  const {data} = await fastApi.sqlList(searchRq.value)
  sqlList.value = data.records
  total.value = data.total
}

const openAddDialog = () => {
  editSql.value = undefined
  showFormDialog.value = true
}

const openEditDialog = (row: FastSql.SysFastSql) => {
  editSql.value = row
  showFormDialog.value = true
}

const openViewDrawer = (row: FastSql.SysFastSql) => {
  viewSqlData.value = row
  showSqlDrawer.value = true
}

const openExecDialog = (row: FastSql.SysFastSql) => {
  execSqlData.value = row
  execParams.value = {}
  execResult.value = null
  execCurrentPage.value = 1
  execPageSize.value = row.dataPage?.size || 20
  showExecDialog.value = true
}

const executeSql = async () => {
  if (!execSqlData.value) return

  executing.value = true
  try {
    const params: DynamicFilterParameter[] = Object.entries(execParams.value)
      .filter(([, value]) => value !== undefined && value !== '')
      .map(([name, value]) => ({
        parameterName: name,
        parameterValue: value
      }))

    const rq: FastSql.ExecParameterRq = {
      sortFields: execSqlData.value.sortFields || [],
      parameters: params,
      dataPage: {
        needPage: execSqlData.value.dataPage?.needPage || false,
        page: execSqlData.value.dataPage?.needPage ? execCurrentPage.value - 1 : 0,
        size: execPageSize.value
      }
    }

    const {data} = await fastApi.sqlExec(execSqlData.value.id, rq)
    execResult.value = data
    toast.success('执行成功')
  } catch (error) {
    toast.error('执行失败')
  } finally {
    executing.value = false
  }
}

const handleExecPageChange = () => {
  executeSql()
}

const handleExecPageSizeChange = () => {
  execCurrentPage.value = 1
  executeSql()
}

const deleteSql = async (row: FastSql.SysFastSql) => {
  await ElMessageBox.confirm(
    `确认删除SQL模板"${row.sqlName}"?`,
    '删除SQL模板',
    {
      distinguishCancelAndClose: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )
  await fastApi.sqlDelete(row.id)
  toast.success('SQL模板删除成功')
  await refresh()
}

onMounted(async () => {
  await refresh()
})
</script>

<style scoped>
.sql-viewer :deep(textarea) {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
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
