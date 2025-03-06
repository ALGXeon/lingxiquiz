<template>
  <div class="user-page-container">
    <a-card
      :title="`${isCurrentUserEditable ? '编辑' : '查看'}用户信息`"
      class="user-info-card"
    >
      <a-form
        :model="form"
        layout="vertical"
        @submit="handleFinish"
        v-if="currentUser"
      >
        <!-- 头像上传 -->
        <a-form-item label="头像">
          <div v-if="!isCurrentUserEditable">
            <a-image width="200" v-model:src="form.userAvatar" />
          </div>
          <div v-else>
            <PictureUploader
              biz="user_avatar"
              :value="form.userAvatar"
              @change="(value) => (form.userAvatar = value)"
              :disabled="!isCurrentUserEditable"
            />
          </div>
        </a-form-item>

        <!-- 用户名 -->
        <a-form-item label="用户名" required>
          <a-input
            v-model:model-value="form.userName"
            placeholder="请输入用户名"
            :disabled="!isCurrentUserEditable"
          />
        </a-form-item>

        <!-- 用户角色（只读） -->
        <a-form-item label="用户角色">
          <a-input v-bind:model-value="form.userRole" disabled />
        </a-form-item>

        <!-- 用户简介 -->
        <a-form-item label="个人简介">
          <a-textarea
            v-model:model-value="form.userProfile"
            placeholder="请输入个人简介"
            :disabled="!isCurrentUserEditable"
          />
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item v-if="isCurrentUserEditable">
          <a-button type="primary" html-type="submit">保存</a-button>
        </a-form-item>
      </a-form>
      <div v-else>
        <a-spin size="large" />
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watchEffect, withDefaults, defineProps } from "vue";
import { useRoute } from "vue-router";
import {
  getUserVoByIdUsingGet,
  updateMyUserUsingPost,
} from "@/api/userController";
import PictureUploader from "@/components/PictureUploader.vue";
import { useLoginUserStore } from "@/store/userStore";

// 定义 Props 接口
interface Props {
  id: string;
}

// 获取路由参数
const props = withDefaults(defineProps<Props>(), {
  id: "",
});

const route = useRoute();
const loginUserStore = useLoginUserStore();

// 直接获取登录用户信息
loginUserStore.fetchLoginUser();

const form = ref({
  createTime: undefined,
  id: undefined,
  updateTime: undefined,
  userAvatar: undefined,
  userName: undefined,
  userProfile: undefined,
  userRole: undefined,
} as API.LoginUserVO);

const currentUser = ref<API.LoginUserVO | null>(null);

// 计算属性：判断当前查看的用户是否是登录用户
const isCurrentUserEditable = computed(
  () =>
    currentUser.value && loginUserStore.loginUser.id === currentUser.value.id
);

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getUserVoByIdUsingGet({ id: props.id });
  console.log(res.data);
  if (res.data.code === 0 && res.data.data) {
    currentUser.value = res.data.data;
    form.value = currentUser.value;
    console.log(form);
  } else {
    console.error("Failed to fetch user by ID");
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

/**
 * 提交表单
 */
const handleFinish = async () => {
  if (!isCurrentUserEditable.value) {
    console.warn("Cannot update other users' information");
    return;
  }
  try {
    console.log(form.value);
    const response = await updateMyUserUsingPost(form.value);
    if (response.data) {
      // 更新本地缓存的用户信息
      loginUserStore.loginUser = form.value;
    }
  } catch (error) {
    console.error("更新失败", error);
  }
};

const onFinishFailed = (errorInfo: any) => {
  console.error("Validation failed:", errorInfo);
};
</script>

<style scoped>
.user-page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.user-info-card {
  width: 70%;
  max-width: 800px; /* 设置最大宽度以防止卡片过大 */
  margin: auto;
  padding: 20px;
}
</style>
