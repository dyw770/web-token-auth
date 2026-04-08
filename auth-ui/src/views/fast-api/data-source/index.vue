<template>
  <FaPageMain>
    <div class="p-4">
      <el-form inline>
        <el-form-item>
          <el-button type="primary" @click="openAddDialog">新增数据源</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="dataSourceList" border style="width: 100%" table-layout="auto">
      <el-table-column prop="sourceName" label="名称" align="center" min-width="120"/>
      <el-table-column prop="dbType" label="数据库类型" align="center" width="120"/>
      <el-table-column prop="jdbcUrl" label="JDBC URL" align="center" min-width="250">
        <template #default="{ row }">
          <span class="font-mono text-sm">{{ row.jdbcUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" align="center" width="120"/>
      <el-table-column prop="driverName" label="驱动" align="center" min-width="200">
        <template #default="{ row }">
          <span class="font-mono text-sm">{{ row.driverName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" width="180"/>
      <el-table-column prop="updateTime" label="更新时间" align="center" width="180"/>
      <el-table-column label="操作" align="center" width="280">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" type="success" @click="refreshDataSource(row.sourceName)">刷新</el-button>
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteDataSource(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showFormDialog" :title="isEdit ? '编辑数据源' : '新增数据源'" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称" prop="sourceName">
              <el-input
                v-model="form.sourceName"
                placeholder="请输入数据源名称"
                maxlength="32"
                show-word-limit
                :disabled="isEdit"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="dbType">
              <el-select v-model="form.dbType" placeholder="请选择数据库类型" class="w-full" @change="onDbTypeChange">
                <el-option label="MySQL" value="mysql"/>
                <el-option label="PostgreSQL" value="postgresql"/>
                <el-option label="Oracle" value="oracle"/>
                <el-option label="SQL Server" value="sqlserver"/>
                <el-option label="SQLite" value="sqlite"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="JDBC URL" prop="jdbcUrl">
          <el-input
            v-model="form.jdbcUrl"
            placeholder="请输入JDBC URL，如：jdbc:mysql://localhost:3306/db"
            maxlength="256"
            show-word-limit/>
        </el-form-item>

        <el-form-item label="驱动类名" prop="driverName">
          <el-input
            v-model="form.driverName"
            placeholder="请输入驱动类名"
            maxlength="256"
            show-word-limit/>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="form.username"
                placeholder="请输入用户名"
                maxlength="64"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                maxlength="64"
                show-password/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="连接属性" prop="properties">
          <el-input
            v-model="form.properties"
            type="textarea"
            :rows="4"
            placeholder="请输入连接属性，properties格式"
            maxlength="4096"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showFormDialog = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>
  </FaPageMain>
</template>

<script setup lang="ts">
import fastApi from '@/api/modules/fast-api'
import type {DataSource} from '#/fast-api'
import {ElMessageBox, type FormInstance, type FormRules} from 'element-plus'
import {toast} from 'vue-sonner'

defineOptions({
  name: 'FastApiDataSourceIndex',
})

const formRef = ref<FormInstance>()

const showFormDialog = ref(false)
const submitting = ref(false)
const editData = ref<DataSource.SysFastDataSource | null>(null)

const dataSourceList = ref<DataSource.SysFastDataSource[]>([])

const form = ref({
  sourceName: '',
  jdbcUrl: '',
  username: '',
  password: '',
  driverName: '',
  properties: '',
  dbType: '',
})

const formRules: FormRules = {
  sourceName: [
    {required: true, message: '请输入数据源名称', trigger: 'blur'},
    {min: 1, max: 32, message: '名称长度为1-32个字符', trigger: 'blur'},
  ],
  jdbcUrl: [
    {required: true, message: '请输入JDBC URL', trigger: 'blur'},
    {min: 1, max: 256, message: 'JDBC URL长度为1-256个字符', trigger: 'blur'},
  ],
  driverName: [
    {required: true, message: '请输入驱动类名', trigger: 'blur'},
    {min: 1, max: 256, message: '驱动类名长度为1-256个字符', trigger: 'blur'},
  ],
  dbType: [
    {required: true, message: '请选择数据库类型', trigger: 'change'},
  ],
}

const isEdit = computed(() => !!editData.value)

const dbTypeConfig: Record<string, { driver: string, urlTemplate: string }> = {
  mysql: {
    driver: 'com.mysql.cj.jdbc.Driver',
    urlTemplate: 'jdbc:mysql://localhost:3306/database'
  },
  postgresql: {
    driver: 'org.postgresql.Driver',
    urlTemplate: 'jdbc:postgresql://localhost:5432/database'
  },
  oracle: {
    driver: 'oracle.jdbc.OracleDriver',
    urlTemplate: 'jdbc:oracle:thin:@localhost:1521:orcl'
  },
  sqlserver: {
    driver: 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
    urlTemplate: 'jdbc:sqlserver://localhost:1433;databaseName=database'
  },
  sqlite: {
    driver: 'org.sqlite.JDBC',
    urlTemplate: 'jdbc:sqlite:/path/to/database.db'
  }
}

const onDbTypeChange = (dbType: string) => {
  const config = dbTypeConfig[dbType]
  if (config) {
    if (!form.value.driverName) {
      form.value.driverName = config.driver
    }
    if (!form.value.jdbcUrl) {
      form.value.jdbcUrl = config.urlTemplate
    }
  }
}

const refresh = async () => {
  const {data} = await fastApi.dataSourceList()
  dataSourceList.value = data
}

const resetForm = () => {
  form.value = {
    sourceName: '',
    jdbcUrl: '',
    username: '',
    password: '',
    driverName: '',
    properties: '',
    dbType: '',
  }
  editData.value = null
  formRef.value?.resetFields()
}

const openAddDialog = () => {
  resetForm()
  showFormDialog.value = true
}

const openEditDialog = (row: DataSource.SysFastDataSource) => {
  editData.value = row
  form.value = {
    sourceName: row.sourceName,
    jdbcUrl: row.jdbcUrl,
    username: row.username || '',
    password: '',
    driverName: row.driverName,
    properties: row.properties || '',
    dbType: row.dbType,
  }
  showFormDialog.value = true
}

const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (isEdit.value) {
        const rq: DataSource.DataSourceEditRq = {
          sourceName: form.value.sourceName,
          jdbcUrl: form.value.jdbcUrl,
          username: form.value.username || undefined,
          password: form.value.password || undefined,
          driverName: form.value.driverName,
          properties: form.value.properties || undefined,
          dbType: form.value.dbType,
        }
        await fastApi.dataSourceEdit(rq)
        toast.success('数据源更新成功')
      } else {
        const rq: DataSource.DataSourceCreateRq = {
          sourceName: form.value.sourceName,
          jdbcUrl: form.value.jdbcUrl,
          username: form.value.username || undefined,
          password: form.value.password || undefined,
          driverName: form.value.driverName,
          properties: form.value.properties || undefined,
          dbType: form.value.dbType,
        }
        await fastApi.dataSourceAdd(rq)
        toast.success('数据源创建成功')
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

const refreshDataSource = async (name: string) => {
  await fastApi.dataSourceRefresh(name)
  toast.success('数据源刷新成功')
}

const deleteDataSource = async (row: DataSource.SysFastDataSource) => {
  await ElMessageBox.confirm(
    `确认删除数据源"${row.sourceName}"?`,
    '删除数据源',
    {
      distinguishCancelAndClose: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )
  await fastApi.dataSourceDelete(row.sourceName)
  toast.success('数据源删除成功')
  await refresh()
}

onMounted(() => {
  refresh()
})
</script>

<style scoped>
.w-full {
  width: 100%;
}

.font-mono {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
}
</style>
