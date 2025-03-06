<template>
  <router-link :to="`/user/info/${userId}`" class="avatar-link">
    <a-avatar>
      <img
        alt="avatar"
        width="32"
        height="32"
        :src="userAvatar"
        style="border-radius: 50%; margin-right: 10px"
      />
    </a-avatar>
  </router-link>
</template>

<script setup lang="ts">
import { defineProps } from "vue";

// 定义 Props 接口
interface Props {
  userId: string; // 用户ID
  userAvatar: string; // 用户头像URL
}

// 获取父组件传递的属性
const props = defineProps<Props>();
</script>

<style scoped>
.avatar-link {
  display: inline-block;
  text-decoration: none;
}
</style>
