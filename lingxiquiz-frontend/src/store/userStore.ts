import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";


export const useLoginUserStore = defineStore("loginuser", () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: "未登录",
  });
  function setLoginUser(user: API.LoginUserVO) {
    loginUser.value = user;
  }
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code == 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      loginUser.value = { userRole: "NOT_LOGIN" };
    }
  }
  return { loginUser, setLoginUser, fetchLoginUser };
});
