<template>
<ContentField>
<table class="table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>USERNAME</th>
            <th>PASSWORD (encrypted)</th>
        </tr>
    </thead>
    <tbody>
        <tr v-for="user in users" :key="user.id">
            <td>{{user.id}}</td>
            <td>{{user.username}}</td>
            <td>{{user.password}}</td>
        </tr>
    </tbody>
</table>
</ContentField>
</template>

<script>
import $ from 'jquery'
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'

export default {
    components: {
        ContentField,
    },
    setup() {
        let users = ref([]);
        const store = useStore();


        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/vip/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    users.value = resp;
                }
            })
        }

        refresh_bots();

        return {
            users
        }
    }
}
</script>

<style scoped>

</style>