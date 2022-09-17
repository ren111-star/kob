import $ from 'jquery'

// 用于存放用户信息（全局变量）
export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,
    },
    getters: {
    },
    mutations: {  // 用来修改数据
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
          state.id = "";
          state.username = "";
          state.photo = "";
          state.token = "";
          state.is_login = false;
        },
        updatePullingInfo(state, pulling_info) {
          state.pulling_info = pulling_info;
        }
    },
    actions: {
        // 修改state的函数都会放在actions中
        login(context, data) {
            $.ajax({
                url: "https://app3156.acapp.acwing.com.cn/api/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(resp) {
                  if (resp.error_message === "success") {  // ?
                    context.commit("updateToken", resp.token);
                    localStorage.setItem("jwt_token", resp.token);
                    data.success(resp);  // 调用回调函数
                  } else {
                    data.error(resp);
                  }
                },
                error(resp) {
                  data.error(resp);
                }
              });
        },
        getinfo(context, data) {
          $.ajax({
            url: "https://app3156.acapp.acwing.com.cn/api/user/account/info/",
            type: "get",
            headers: {
              Authorization: "Bearer " + context.state.token,  // 获取token
            },
            success(resp) {
                if (resp.error_message === "success") {
                  // 更新用户
                  context.commit("updateUser", {
                    ...resp,
                    is_login: true,
                  });
                  data.success(resp);  // 调用回档函数
                } else {
                  data.error(resp);
                }
              },
            error(resp) {
              data.error(resp);
            }
          });
        },
        logout(context) {
          localStorage.removeItem("jwt_token");
          context.commit("logout");
        }
    },
    modules: {
    }
  }
  