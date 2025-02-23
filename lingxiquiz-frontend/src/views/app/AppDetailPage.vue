<template>
  <div id="appDetailPage">
    <a-card>
      <a-row style="margin-bottom: 16px">
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ data.appName }}</h2>
          <p>{{ data.appDesc }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data.scoringStrategy] }}</p>
          <p>
            <a-space>
              作者：
              <div :style="{ display: 'flex', alignItems: 'center' }">
                <a-avatar
                  :size="24"
                  :image-url="data.user?.userAvatar"
                  :style="{ marginRight: '8px' }"
                />
                <a-typography-text
                  >{{ data.user?.userName ?? "无名" }}
                </a-typography-text>
              </div>
            </a-space>
          </p>
          <p>
            创建时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button type="primary" :href="`/answer/do/${id}`"
              >开始答题
            </a-button>
            <a-button>分享应用</a-button>
            <a-button v-if="isMy" :href="`/add/question/${id}`"
              >设置题目
            </a-button>
            <a-button v-if="isMy" :href="`/add/scoring_result/${id}`"
              >设置评分
            </a-button>
            <a-button v-if="isMy" :href="`/add/app/${id}`">修改应用</a-button>
          </a-space>
          <p v-if="availableUses !== null && data.scoringStrategy === 1">
            <strong
              ><span v-if="availableUses === 0"
                >今日AI使用次数用尽，明天再来吧</span
              >
              <span v-else>
                今日AI剩余使用次数：{{ availableUses }}</span
              ></strong
            >
          </p>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.appIcon" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {
  computed,
  defineProps,
  onMounted,
  ref,
  watchEffect,
  withDefaults,
} from "vue";
import API from "@/api";
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { useLoginUserStore } from "@/store/userStore";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";
import { getAvailableUsesUsingGet } from "@/api/aiUsageController";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

// 声明一个响应式变量来保存剩余使用次数
const availableUses = ref<number | null>(null);

const router = useRouter();

const data = ref<API.AppVO>({});

// 获取登录用户
const loginUserStore = useLoginUserStore();
let loginUserId = loginUserStore.loginUser?.id;
// 是否为本人创建
const isMy = computed(() => {
  return loginUserId && loginUserId === data.value.userId;
});

// 在组件挂载时尝试获取一次剩余使用次数
onMounted(() => {
  fetchAvailableUses();
});

/**
 * 获取剩余使用次数
 */
const fetchAvailableUses = async () => {
  if (loginUserId) {
    const res = await getAvailableUsesUsingGet({
      userId: loginUserId,
    });
    if (res.data.code === 0) {
      availableUses.value = res.data.data;
    } else {
      message.error("获取剩余使用次数失败：" + res.data.message);
    }
  }
};

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0) {
    data.value = res.data.data as any;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
#appDetailPage {
}

#appDetailPage .content-wrapper > * {
  margin-bottom: 24px;
}
</style>
