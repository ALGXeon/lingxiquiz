<template>
  <a-row class="globalHeader" align="center" justify="space-between">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="titleBar">
            <!--素材版权https://www.iconfont.cn/user/detail?spm=a313x.search_index.0.d214f71f6.72ec3a81EADk86&uid=3865146&nid=pMERe0emZ2Tj-->
            <img class="logo" src="../assets/logo.png" alt="" />
            <div class="title">灵犀问答</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visivleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div v-if="loginUserStore.loginUser.id" class="userInfo">
        <Avatar
          :userId="loginUserStore.loginUser.id"
          :userAvatar="loginUserStore.loginUser.userAvatar"
        />
        <span :style="{ marginRight: '10px', marginLeft: '8px' }">
          {{ loginUserStore.loginUser.userName ?? "无名" }}
        </span>
        <a-button type="primary" @click="handleLogout">登出</a-button>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { userLogoutUsingPost } from "@/api/userController";
import Avatar from "@/components/Avatar.vue";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 当前选中的菜单
const selectedKeys = ref(["/"]);
//路由跳转时更新选中的菜单
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const visivleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

// 定义 handleLogout 函数
const handleLogout = async () => {
  // 清除登录状态
  await userLogoutUsingPost();
  // 重定向到登录页面
  router.push("/user/login");
};
</script>

<style scoped>
#globalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
}

.userInfo {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
</style>
