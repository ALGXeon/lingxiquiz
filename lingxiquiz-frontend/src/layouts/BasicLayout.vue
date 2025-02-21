<template>
  <div id="basicLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a
          href="http://github.com/ALGXeon"
          target="_blank"
          style="text-decoration: none"
        >
          By ALGXeon
        </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<style scoped>
#basicLayout {
}

#basicLayout .content {
  max-width: 1200px;
  width: 100%;
  box-sizing: border-box;
  margin: 0 auto 28px;
  padding: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
}

#basicLayout .footer {
  text-align: center;
  padding: 10px;
  background: #f6f6f6;
}

#basicLayout .header {
  margin-bottom: 1px;
  background: linear-gradient(to right, #f6f6f6, #fff);
  box-shadow: #eee 1px 1px 5px;
}
</style>
