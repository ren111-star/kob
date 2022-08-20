<!-- 游戏区域 -->

<template>
    <div class="playground">
        <GameMap />
    </div>
</template>

<script>
import GameMap from './GameMap.vue'

export default {
    components: {
    GameMap
}
}

</script>

<style scoped>
div.playground {
    width: 70vw;
    height: 80vh;
    margin: 10px auto 0px;
}
</style>