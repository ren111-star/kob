<template>
    <ResultBord v-if="$store.state.pk.loser != 'none' "/>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-else-if="$store.state.pk.status === 'matching'"/>
    <div class="user-color" v-if="$store.state.pk.status === 'playing' && $store.state.user.id == $store.state.pk.a_id">BLUE</div>
    <div class="user-color" v-if="$store.state.pk.status === 'playing' && $store.state.user.id == $store.state.pk.b_id">RED</div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import { onMounted } from 'vue'  // 当组件被挂在时使用的函数
import { onUnmounted } from 'vue'  // 当组件被卸载时使用的函数
import { useStore } from 'vuex'
import MatchGround from '../../components/MatchGround.vue'
import ResultBord from '../../components/ResultBord.vue'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBord,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
        store.commit("updateLoser", "none");
        store.commit("updateIsRecord", false);

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
                    }, 200);

                    store.commit("updateGame", data.game);
                } else if (data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if (data.loser === "all" || data.loser === "A") {
                        snake0.status = "die"
                    }

                    if (data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
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
div.user-color {
    text-align: center;
    color: cornsilk;
    font-size: 30px;
    font-weight: bold;
}
</style>