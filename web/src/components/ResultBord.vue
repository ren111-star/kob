<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser == 'all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser == 'A'
                 && $store.state.pk.a_id == $store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser == 'B'
                 && $store.state.pk.b_id == $store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-warning btn-lg">
                again!    
            </button>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex'
    
export default {
    setup() {
        const sotre = useStore();
        const restart = () => {
            console.log("restart");
            sotre.commit("updateStatus", "matching");
            sotre.commit("updateLoser", "none");
            sotre.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
        }
        return {
            restart
        };
    }
}
</script>

<style scoped>
    div.result-board {
        height: 30vh;
        width: 30%;
        position: absolute;
        background-color: rgba(50, 50, 50, 0.7);
        top: 30vh;
        left: 35%;
    }
    div.result-board-text {
        text-align: center;
        color: white;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
    div.result-board-btn {
        padding-top: 7vh;
        text-align: center;
        
    }
</style>