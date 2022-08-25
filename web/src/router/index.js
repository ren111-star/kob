import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import UserBotIndexView from '../views/user/bots/UserBotIndexView'
import NotFound from '../views/error/NotFoundView'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterview from '../views/user/account/UserAccountRegisterView'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/"
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView
  },
  {
    path: "/user/bots/",
    name: "user_bot_index",
    component: UserBotIndexView
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterview
  },
  {
    path: "/404/",
    name: "not_found",
    component: NotFound
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
