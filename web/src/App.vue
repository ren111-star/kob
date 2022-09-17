<template>
  <NavBar/>
  <router-view/>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  components:{
    NavBar
  },
  setup() {
    // $.ajax({
    //   url: "https://app3156.acapp.acwing.com.cn/api/user/account/token/",
    //   type: "post",
    //   data: {
    //     username: "yxc",
    //     password: 'pyxc',
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // });

    // $.ajax({
    //   url: "https://app3156.acapp.acwing.com.cn/api/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZDQ0NGM3N2UxNDY0YjVmOTg5NGI3YjBjNGE4ZjljZiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2MTM0Nzk1NiwiZXhwIjoxNjYyNTU3NTU2fQ.iZUAw995o4Rn8lW16r0IuQjC3F3XQqmmlyPWfxKkyEs"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // });

    // $.ajax({
    //   url: "https://app3156.acapp.acwing.com.cn/api/user/account/register/",
    //   type: "post",
    //   data: {
    //     username: "yxc",
    //     password: "123",
    //     confirmedPassword: "123"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // })
  }
}
</script>

<style>
body {
  background-image: url("@/assets/images/pexels-pixabay-40896.jpg");
  background-size: cover;
}
</style>
