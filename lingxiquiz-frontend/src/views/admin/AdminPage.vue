<template>
  <div class="admin-dashboard">
    <div class="buttons">
      <!-- 用户管理 -->
      <router-link :to="{ name: '用户管理' }">
        <a-button type="primary" style="margin-right: 10px">
          用户管理
        </a-button>
      </router-link>

      <!-- 应用管理 -->
      <router-link :to="{ name: '应用管理' }">
        <a-button type="primary" style="margin-right: 10px">
          应用管理
        </a-button>
      </router-link>

      <!-- 题目管理 -->
      <router-link :to="{ name: '题目管理' }">
        <a-button type="primary" style="margin-right: 10px">
          题目管理
        </a-button>
      </router-link>

      <!-- 评分管理 -->
      <router-link :to="{ name: '评分管理' }">
        <a-button type="primary"> 评分管理</a-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { defineComponent } from "vue";
// 如果你需要使用 Arco Design 的 Button 组件，确保已经安装并导入了相关依赖
// import { AButton } from '@arco-design/web-vue';
// import '@arco-design/web-vue/dist/arco.css';

defineComponent({
  name: "AdminDashboardPage",
});
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.buttons {
  margin-top: 20px;
}

.buttons .a-button {
  margin-bottom: 10px;
}
</style>
