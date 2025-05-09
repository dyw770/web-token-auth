<template>
  <FaPageMain>

    <!-- 搜索区域 -->
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="searchRq.username" placeholder="请输入用户名" clearable/>
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="searchRq.nickname" placeholder="请输入昵称" clearable/>
        </el-form-item>

        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="searchRq.enabled" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="启用" :value="true"/>
            <el-option label="禁用" :value="false"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="showAddUserDialog">新增用户</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="userList" border style="width: 100%">
      <el-table-column prop="username" label="用户名" align="center"/>
      <el-table-column prop="nickname" label="昵称" align="center"/>
      <el-table-column prop="avatar" label="头像" align="center">
        <template #default="{ row }">
          <ImagePreview :src="row.avatar" width="100px" height="100px" v-if="row.avatar"/>
          <span v-else>无头像</span>
        </template>
      </el-table-column>
      <el-table-column prop="enabled" label="是否启用" align="center">
        <template #default="{ row }">
          <el-tag :type="row.enabled ? 'success' : 'danger'">
            {{ row.enabled ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="accountNonExpired" label="账号是否过期" align="center">
        <template #default="{ row }">
          {{ row.accountNonExpired ? '未过期' : '已过期' }}
        </template>
      </el-table-column>
      <el-table-column prop="credentialsNonExpired" label="密码是否过期" align="center">
        <template #default="{ row }">
          {{ row.credentialsNonExpired ? '未过期' : '已过期' }}
        </template>
      </el-table-column>
      <el-table-column prop="accountNonLocked" label="账号是否锁定" align="center">
        <template #default="{ row }">
          {{ row.accountNonLocked ? '未锁定' : '已锁定' }}
        </template>
      </el-table-column>
      <el-table-column prop="roles" label="角色" align="center">
        <template #default="{ row }">
          <el-tag v-for="role in row.roles" type="primary">{{role.roleName}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center" width="320px">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" size="small" @click="showEditUserDialog(row)">编辑</el-button>
            <el-button size="small" @click="showAuthDialog(row)">授权</el-button>
            <el-button type="danger" size="small" @click="enableUser(row)">{{ row.enabled ? '禁用' : '启用' }}</el-button>
            <el-button type="warning" size="small" @click="lockUser(row)">{{ row.accountNonLocked ? '锁定' : '解锁' }}</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :total="total"
                   :default-current-page="searchRq.page"
                   :page-size="searchRq.size"
                   :page-sizes="[10, 50, 100]"
                   @change="refresh"
                   class="mt-5"/>

    <Add v-model="addUserDialog" @success="refresh"/>
    <Edit v-model="editUserDialog" :user="editUser" @success="refresh" v-if="editUserDialog"/>
    <Auth :user="authUser" v-model="authDialog" @success="refresh" v-if="authDialog" />
  </FaPageMain>
</template>

<script setup lang="ts">

import adminApi from '@/api/modules/admin.ts'
import type {User} from '#/api'
import Add from "@/views/admin/user/add.vue";
import Edit from "@/views/admin/user/edit.vue";
import {toast} from "vue-sonner";
import Auth from "@/views/admin/user/auth.vue";

const addUserDialog = ref(false)
const editUserDialog = ref(false)
const authDialog = ref(false)

const showAddUserDialog = () => {
  addUserDialog.value = true
}

const editUser = ref<User.UserRs>()
const showEditUserDialog = (user: User.UserRs) => {
  editUser.value = user
  editUserDialog.value = true
}

const authUser = ref<User.UserRs>()
const showAuthDialog = (user: User.UserRs) => {
  authUser.value = user
  authDialog.value = true
}

const searchRq = ref<User.UserSearchRq>({
  page: 1,
  size: 10
})

const total = ref(0)

const userList = ref<User.UserRs[]>([])

const resetSearch = () => {
  searchRq.value.enabled = undefined
  searchRq.value.nickname = undefined
  searchRq.value.username = undefined
  refresh()
}

onMounted(async () => {
  await refresh()
})

const refresh = async () => {
  const {data} = await adminApi.userList(searchRq.value)
  userList.value = data.records
  total.value = data.total
}

const enableUser = async (row: User.UserRs) => {
  await adminApi.userEnable(row.username)
  if (row.enabled) {
    toast.success('用户禁用成功')
  } else {
    toast.success('用户启用成功')
  }
  await refresh()
}

const lockUser = async (row: User.UserRs) => {
  await adminApi.userLock(row.username)
  if (row.accountNonLocked) {
    toast.success('用户锁定成功')
  } else {
    toast.success('用户解锁成功')
  }
  await refresh()
}

</script>

<style scoped>


</style>
