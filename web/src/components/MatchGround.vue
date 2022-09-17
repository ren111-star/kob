<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4 user-select-bot">
                <select v-model="select_bot" class="form-select" aria-label="Default select example">
                    <option value="-1" selected>亲自上阵</option>
                    <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                        {{ bot.title }}
                    </option>
                </select>
            </div>
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center;padding-top: 15vh;">
                <button type="button" @click="matching_btn" class="btn btn-warning btn-lg">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import store from '@/store';
import { ref } from 'vue';
import $ from 'jquery'

export default {
    components: {

    },
    setup() {
        let match_btn_info = ref("match");
        let bots = ref([]);
        let select_bot = ref("-1");

        const matching_btn = () =>{
            if (match_btn_info.value === "match") {
                match_btn_info.value = "matching";
                // 向后端发送请求
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value
                }));
            }
            else {
                match_btn_info.value = "match";
                // 向后端发送请求
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }));
                
                store.commit("updateOpponent", {
                    username: "我的对手",
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
                })
            }
        }

        const refresh_bots = () => {
            $.ajax({
                url: "https://app3156.acapp.acwing.com.cn/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        } 

        refresh_bots();  // 获取bots

        return {
            matching_btn,
            match_btn_info,
            bots,
            select_bot
        }
    }
}

</script>

<style scoped>
div.matchground {
    width: 70vw;
    height: 80vh;
    margin: 10px auto 0px;
    background-color: rgba(50, 50, 50, 0.5)
}

div.user-photo {
    text-align: center;
    padding-top: 10vh;
}
div.user-photo>img {
    border-radius: 50%;
    width: 20vh;
}
div.user-username {
    color: aliceblue;
    text-align: center;
    font-size: 30px;
    font-weight: 600;
    padding-top: 2vh;
}
div.user-select-bot {
    padding-top: 20vh;
}

div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>