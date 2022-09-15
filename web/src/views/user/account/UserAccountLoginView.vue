<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" v-model="username" class="form-control" id="username" placeholder="place input username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" v-model="password" class="form-control" id="password" placeholder="place input password">
                    </div>
                    <div class="error-message"> {{ error_message }} </div>
                    <button type="submit" class="btn btn-primary">submit</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import router from '@/router';
import { useStore } from 'vuex';
import { ref } from 'vue'
import ContentField from '../../../components/ContentField.vue'

export default {
    components: {
        ContentField
    },
    setup() {
        // 定义全局变量
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo",{
                success() {
                    router.push({name: "home"});
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false);
        }

        const login = () => {  // 如果提交就触发这个函数
            error_message.value = "";
            store.dispatch("login",{  // 调用action里的函数就要用dispatch
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getinfo",{ 
                        success() {
                            router.push({name: 'home'});  // 跳转
                            console.log(store.state.user);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
button.btn{
    width: 100%;
}
div.error-message {
    color: red;
}
</style>