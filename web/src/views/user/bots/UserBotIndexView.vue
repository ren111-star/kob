<template>
    <ContentField>
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 10px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style = "width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 10px">
                    <div class="card-header">
                        <span style="font-size: 130%;">我的Bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">create</button>
                            <!-- Modal -->
                        <div class="modal fade modal-xl" id="add-bot-btn" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">create bot</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-tittle" class="form-label">title</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-tittle" placeholder="place input the bot's title">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">description</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="place input the bot's description"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-content" class="form-label">content</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />
                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <div class="error_message">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="button" class="btn btn-primary" @click="add_bot">submit</button>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Bot名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">update</button>
                                        <div class="modal fade modal-xl" :id="'update-bot-modal-' + bot.id" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="staticBackdropLabel">create bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="add-bot-tittle" class="form-label">title</label>
                                                            <input v-model="bot.title" type="text" class="form-control" id="add-bot-tittle" placeholder="place input the bot's title">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-description" class="form-label">description</label>
                                                            <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="place input the bot's description"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-content" class="form-label">content</label>
                                                            <VAceEditor
                                                                v-model:value="bot.content"
                                                                @init="editorInit"
                                                                lang="c_cpp"
                                                                theme="textmate"
                                                                style="height: 300px" />
                                                        </div>

                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error_message">{{ bot.error_message }}</div>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                        <button type="button" class="btn btn-primary" @click="update_bot(bot)">submit</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">delete</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'  
import { ref,reactive } from 'vue'
import $ from 'jquery'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    components: {
    ContentField,
    VAceEditor
    },
    setup() {
        const store = useStore();
        let bots = ref([]);
        ace.config.set(
        "basePath", 
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

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

        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "",
            $.ajax({
                url: "https://app3156.acapp.acwing.com.cn/api/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                        botadd.title = "",
                        botadd.description = "",
                        botadd.content = "",
                        Modal.getInstance("#add-bot-btn").hide()
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "https://app3156.acapp.acwing.com.cn/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }

        const update_bot = (bot) => {
            botadd.error_message = "",
            $.ajax({
                url: "https://app3156.acapp.acwing.com.cn/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide()
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            update_bot
        }
    }
}
</script>

<style scoped>
div.error_message{
    color: red;
}
</style>