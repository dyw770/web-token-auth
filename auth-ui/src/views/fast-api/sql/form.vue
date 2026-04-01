<template>
  <el-dialog v-model="show" :title="isEdit ? '编辑SQL模板' : '新增SQL模板'" width="90%" top="5vh" :close-on-click-modal="false">
    <div class="dialog-container">
      <div class="left-panel">
        <div class="mb-2 flex items-center justify-between">
          <span class="font-bold">SQL编辑器</span>
          <div class="flex items-center gap-2">
            <el-select v-model="form.statementType" placeholder="语句类型" class="w-32">
              <el-option label="SELECT" value="select"/>
              <el-option label="INSERT" value="insert"/>
              <el-option label="UPDATE" value="update"/>
              <el-option label="DELETE" value="delete"/>
            </el-select>
            <el-button type="primary" @click="executeSql" :loading="executing">执行</el-button>
          </div>
        </div>
        <el-input
          v-model="form.sql"
          type="textarea"
          :rows="10"
          placeholder="请输入SQL语句，使用 ${参数名} 格式定义参数，例如：SELECT * FROM user WHERE name = ${name}"
          class="sql-editor"/>

        <div class="params-section">
          <div class="flex items-center justify-between mb-2">
            <span class="font-bold text-sm">参数配置</span>
            <el-button size="small" type="primary" @click="addParameter">添加参数</el-button>
          </div>
          <el-table v-if="parameters.length > 0" :data="parameters" border size="small" max-height="150">
            <el-table-column prop="parameterName" label="参数名" min-width="100">
              <template #default="{ row }">
                <el-input v-model="row.parameterName" size="small" placeholder="参数名"/>
              </template>
            </el-table-column>
            <el-table-column prop="parameterDesc" label="描述" min-width="120">
              <template #default="{ row }">
                <el-input v-model="row.parameterDesc" size="small" placeholder="参数描述"/>
              </template>
            </el-table-column>
            <el-table-column prop="parameterType" label="类型" width="100">
              <template #default="{ row }">
                <el-select v-model="row.parameterType" size="small" placeholder="类型">
                  <el-option label="字符串" value="string"/>
                  <el-option label="整数" value="integer"/>
                  <el-option label="小数" value="decimal"/>
                  <el-option label="日期" value="date"/>
                  <el-option label="布尔" value="boolean"/>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="parameterValue" label="测试值" min-width="120">
              <template #default="{ row }">
                <el-input v-model="row.parameterValue" size="small" placeholder="测试值"/>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="60" align="center">
              <template #default="{ $index }">
                <el-button size="small" type="danger" link @click="removeParameter($index)">
                  <el-icon><Delete/></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="暂无参数" :image-size="40"/>
        </div>

        <div class="mt-4">
          <el-form :model="form" label-width="100px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="模板名称">
                  <el-input v-model="form.sqlName" placeholder="请输入模板名称"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="模板描述">
                  <el-input v-model="form.sqlDescribe" placeholder="请输入模板描述"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20" v-if="form.statementType === 'select'">
              <el-col :span="8">
                <el-form-item label="开启分页">
                  <el-switch v-model="pageEnabled"/>
                </el-form-item>
              </el-col>
              <el-col :span="8" v-if="pageEnabled">
                <el-form-item label="每页大小">
                  <el-input-number v-model="pageSize" :min="1" :max="100"/>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单行数据">
                  <el-switch v-model="singleRow"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>

      <div class="right-panel">
        <div class="mb-2 font-bold">执行结果</div>
        <div class="result-container">
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
                    min-width="120">
                    <template #header>
                      <div class="flex items-center justify-center gap-1">
                        <span>{{ col.displayName }}</span>
                        <el-icon class="cursor-pointer hover:text-primary" @click="openRenameDialog(col)">
                          <Edit/>
                        </el-icon>
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
                <div v-if="isPageResult && pageResult" class="mt-4 flex justify-end">
                  <el-pagination
                    v-model:current-page="currentPage"
                    v-model:page-size="pageSize"
                    :page-sizes="[10, 20, 50, 100]"
                    :total="pageResult.total"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handlePageSizeChange"
                    @current-change="handlePageChange"/>
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
            <el-empty description="暂无执行结果"/>
          </template>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="hideDialog">取消</el-button>
      <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="renameDialogVisible" title="重命名列" width="400px">
    <el-form label-width="100px">
      <el-form-item label="原字段名">
        <el-input v-model="renamingColumn.field" disabled/>
      </el-form-item>
      <el-form-item label="显示名称">
        <el-input v-model="renamingColumn.displayName" placeholder="请输入显示名称"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="renameDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmRename">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import type {DataFieldBind, DynamicFilterParameter, FastSql, StatementType} from '#/fast-api'
import fastApi from '@/api/modules/fast-api'
import {toast} from 'vue-sonner'
import {Delete, Edit} from '@element-plus/icons-vue'

const show = defineModel({required: true, type: Boolean})

const props = defineProps<{
  sqlData?: FastSql.SysFastSql
}>()

const emit = defineEmits({
  success: () => true
})

const isEdit = computed(() => !!props.sqlData?.id)

const executing = ref(false)
const submitting = ref(false)
const execResult = ref<FastSql.ExecResult | null>(null)

const form = ref({
  sql: '',
  sqlName: '',
  sqlDescribe: '',
  statementType: 'select' as StatementType
})

const pageEnabled = ref(false)
const pageSize = ref(20)
const currentPage = ref(1)
const singleRow = ref(false)

const columnRenames = ref<Map<string, string>>(new Map())

const parameters = ref<DynamicFilterParameter[]>([])

const initForm = () => {
  if (props.sqlData) {
    form.value = {
      sql: props.sqlData.sqlTemplate || '',
      sqlName: props.sqlData.sqlName || '',
      sqlDescribe: props.sqlData.sqlDescribe || '',
      statementType: props.sqlData.statementType || 'select'
    }
    pageEnabled.value = props.sqlData.dataPage?.needPage || false
    pageSize.value = props.sqlData.dataPage?.size || 20
    singleRow.value = (props.sqlData.extend as Record<string, unknown>)?.single as boolean || false

    if (props.sqlData.parameters) {
      parameters.value = props.sqlData.parameters.map(p => ({
        parameterName: p.parameterName || '',
        parameterDesc: p.parameterDesc || '',
        parameterType: p.parameterType || 'string',
        parameterValue: p.parameterValue || ''
      }))
    }

    if (props.sqlData.dataFieldBinds) {
      columnRenames.value.clear()
      props.sqlData.dataFieldBinds.forEach(bind => {
        if (bind.headerField && bind.headerName) {
          columnRenames.value.set(bind.headerField, bind.headerName)
        }
      })
    }
  }
}

watch(() => props.sqlData, () => {
  if (show.value) {
    initForm()
  }
}, {immediate: true})

watch(show, (val) => {
  if (val) {
    initForm()
  }
})

const addParameter = () => {
  parameters.value.push({
    parameterName: '',
    parameterDesc: '',
    parameterType: 'string',
    parameterValue: ''
  })
}

const removeParameter = (index: number) => {
  parameters.value.splice(index, 1)
}

const isSelectResult = computed(() => {
  return execResult.value && 'schema' in execResult.value
})

const isPageResult = computed(() => {
  return execResult.value && 'total' in execResult.value
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

const isSingleRowResult = computed(() => {
  if (!isSelectResult.value || !selectResult.value?.data) return false
  return !Array.isArray(selectResult.value.data)
})

const displayColumns = computed(() => {
  if (!selectResult.value?.schema) return []
  return selectResult.value.schema.map(field => ({
    field,
    displayName: columnRenames.value.get(field) || field
  }))
})

const executeSql = async () => {
  if (!form.value.sql.trim()) {
    toast.error('请输入SQL语句')
    return
  }

  executing.value = true
  try {
    const execParams: DynamicFilterParameter[] = parameters.value
      .filter(p => p.parameterName && p.parameterValue !== undefined && p.parameterValue !== '')
      .map(p => ({
        parameterName: p.parameterName,
        parameterDesc: p.parameterDesc,
        parameterType: p.parameterType,
        parameterValue: p.parameterValue
      }))

    const rq: FastSql.ExecRq = {
      sql: form.value.sql,
      statementType: form.value.statementType,
      sortFields: [],
      parameters: execParams,
      dataFieldBinds: [],
      dataPage: {
        needPage: pageEnabled.value,
        page: pageEnabled.value ? currentPage.value - 1 : 0,
        size: pageSize.value
      },
      extend: {
        single: singleRow.value
      }
    }

    const {data} = await fastApi.exec(rq)
    execResult.value = data
    toast.success('执行成功')
  } catch (error) {
    toast.error('执行失败')
  } finally {
    executing.value = false
  }
}

const handlePageChange = () => {
  executeSql()
}

const handlePageSizeChange = () => {
  currentPage.value = 1
  executeSql()
}

const renamingColumn = ref({
  field: '',
  displayName: ''
})
const renameDialogVisible = ref(false)

const openRenameDialog = (col: { field: string, displayName: string }) => {
  renamingColumn.value = {...col}
  renameDialogVisible.value = true
}

const confirmRename = () => {
  if (renamingColumn.value.displayName.trim()) {
    columnRenames.value.set(renamingColumn.value.field, renamingColumn.value.displayName)
  }
  renameDialogVisible.value = false
}

const hideDialog = () => {
  form.value = {
    sql: '',
    sqlName: '',
    sqlDescribe: '',
    statementType: 'select'
  }
  execResult.value = null
  pageEnabled.value = false
  pageSize.value = 20
  currentPage.value = 1
  singleRow.value = false
  columnRenames.value.clear()
  parameters.value = []
  show.value = false
}

const submitForm = async () => {
  if (!form.value.sqlName.trim()) {
    toast.error('请输入模板名称')
    return
  }
  if (!form.value.sqlDescribe.trim()) {
    toast.error('请输入模板描述')
    return
  }
  if (!form.value.sql.trim()) {
    toast.error('请输入SQL语句')
    return
  }

  submitting.value = true
  try {
    const dataFieldBinds: DataFieldBind[] = []
    columnRenames.value.forEach((displayName, headerField) => {
      dataFieldBinds.push({
        headerField,
        headerName: displayName
      })
    })

    const saveParams: DynamicFilterParameter[] = parameters.value
      .filter(p => p.parameterName)
      .map(p => ({
        parameterName: p.parameterName,
        parameterDesc: p.parameterDesc,
        parameterType: p.parameterType,
        parameterValue: p.parameterValue
      }))

    if (isEdit.value) {
      const rq: FastSql.SqlEditRq = {
        id: props.sqlData!.id,
        sqlName: form.value.sqlName,
        sqlDescribe: form.value.sqlDescribe,
        sqlTemplate: form.value.sql,
        statementType: form.value.statementType,
        sortFields: [],
        parameters: saveParams,
        dataPage: {
          needPage: pageEnabled.value,
          size: pageSize.value
        },
        extend: {
          single: singleRow.value
        },
        dataFieldBinds
      }
      await fastApi.sqlEdit(rq)
      toast.success('SQL模板更新成功')
    } else {
      const rq: FastSql.SqlCreateRq = {
        sqlName: form.value.sqlName,
        sqlDescribe: form.value.sqlDescribe,
        sqlTemplate: form.value.sql,
        statementType: form.value.statementType,
        sortFields: [],
        parameters: saveParams,
        dataPage: {
          needPage: pageEnabled.value,
          size: pageSize.value
        },
        extend: {
          single: singleRow.value
        },
        dataFieldBinds
      }
      await fastApi.sqlAdd(rq)
      toast.success('SQL模板创建成功')
    }

    hideDialog()
    emit('success')
  } catch (error) {
    toast.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.dialog-container {
  display: flex;
  gap: 16px;
  height: 70vh;
  overflow: hidden;
}

.left-panel {
  flex: 0 0 50%;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.sql-editor {
  flex: 0 0 auto;
}

.params-section {
  margin-top: 12px;
  flex: 0 0 auto;
  max-height: 200px;
  overflow: auto;
}

.right-panel {
  flex: 0 0 50%;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #e4e7ed;
  padding-left: 16px;
  min-width: 0;
}

.result-container {
  flex: 1;
  overflow: auto;
  min-height: 0;
}
</style>
