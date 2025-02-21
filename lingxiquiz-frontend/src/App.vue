<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
  <!--  <router-view />-->
</template>

<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRoute } from "vue-router";
import { getLoginUserUsingGet } from "@/api/userController";
import { useLoginUserStore } from "@/store/userStore";
import { onMounted } from "vue";

const route = useRoute();

getLoginUserUsingGet().then((res) => {
  console.log(res);
});

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();

const doInit = () => {
  console.log("doInit， 欢迎来到我的项目");
};

// 页面加载的时候执行一次onMounted
onMounted(() => {
  doInit();
});
</script>

<style>
#app {
}
</style>
