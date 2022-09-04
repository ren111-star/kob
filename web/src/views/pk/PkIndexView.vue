<template>
        <PlayGround v-if="$store.state.pk.status === 'playing'"/>
        <MatchGround v-if="$store.state.pk.status === 'matching'"/>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import { onMounted } from 'vue';  // 当组件被挂在时使用的函数
import { onUnmounted } from 'vue';  // 当组件被卸载时使用的函数
import { useStore } from 'vuex';
import MatchGround from '../../components/MatchGround.vue'

export default {
    components: {
        PlayGround,
        MatchGround
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
        let socket = null;
        onMounted(() => {
            // 创建一个连接
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })

            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", socket)
            };

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                
                if (data.event === "start-matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });

                    setTimeout(() => {
                        store.commit("updateStatus", "playing") 
                    }, 2000);
                    store.commit("updateGamemap", data.gamemap);
                }
            }

            socket.onclose = () => {
                console.log("disconnected~");
                store.commit("updateStatus", "matching");
            }
        });

        onUnmounted(() => {
            socket.close();
        })
    }
}
</script>

<style scoped>

</style>