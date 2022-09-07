<template>
    <div ref="parent" class="gamemap">
        <!-- 画布 -->
        <!-- tabindex 捕获键盘操作 -->
        <canvas ref="canvas" tabindex="0">

        </canvas>
    </div>
</template>

<script>
import { useStore } from "vuex";
import { GameMap } from "@/assets/script/GameMap";
import { ref, onMounted } from "vue";  // 使用template就要引入
                                      // onMounted 当组件挂载完需要执行哪些操作

export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);
        const store = useStore();

        onMounted(() => {
            store.commit("updateGameObject", 
            new GameMap(canvas.value.getContext('2d'), parent.value, store))
        });

        return {
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.gamemap{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>