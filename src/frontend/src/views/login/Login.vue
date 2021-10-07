<template>
  <div class="flex-column flex-auto p-ai-center p-jc-center root">
    <div class="flex-column gap-lg">
      <div class="flex-row logo gap-md">
        <img src="../../assets/image/login_logo.png"/>
<!--        <div class="flex-row gap-sm">-->
<!--          <span>에너지</span>-->
<!--          <span>관리 플랫폼</span>-->
<!--        </div>-->
      </div>
      <div class="flex-column gap-sm">
        <div class="p-inputgroup">
          <span class="p-inputgroup-addon">
              <i class="pi pi-user"></i>
          </span>
          <InputText placeholder="Username" v-model="id"/>
        </div>
        <div class="p-inputgroup">
          <span class="p-inputgroup-addon">
              <i class="pi pi-lock"></i>
          </span>
          <Password placeholder="Password" v-model="password" :feedback="false" @keydown.enter="login"/>
        </div>
      </div>
      <Button @click="login">
        <img src="../../assets/image/login_icon_lock.png" />
        <span class="p-ml-3 p-text-bold login">로그인</span>
      </Button>
    </div>
  </div>
</template>

<script lang="ts">
import {Vue, Options} from 'vue-property-decorator';
import {useRouter} from "vue-router";
import {useAuthStore} from "@/store/auth";
import * as userApi from '@/api/user-api';
import {User} from "@/types";

@Options({})
export default class Login extends Vue {
  id = '';
  password = '';
  private authStore = useAuthStore();
  private router = useRouter();

  inputError = {
    idEmpty: false,
    passwordEmpty: false,
    mismatch: false
  };

  login() {
    this.inputError.idEmpty = this.id.trim().length === 0;
    this.inputError.passwordEmpty = this.password.trim().length === 0;
    if (this.inputError.idEmpty || this.inputError.passwordEmpty) {
      return;
    }

    userApi.login(this.id, this.password)
        .then(response => {
          this.inputError.mismatch = !response.data.payload;
          if (!response.data.succeeded) {
            this.inputError.mismatch = true;
            this.id = '';
            this.password = '';
          } else {
            this.authStore.setUser(response.data.payload);
            this.router.push('/home');
          }
        }).catch(error => {
          console.log(error)
        });
  }
}
</script>

<style lang="scss" scoped>
.root {
  background-image: url("../../assets/image/login_background.jpg");
  background-position: center center;
  //padding-top: 100px;

  //> div {
  //  margin: auto;
  //}

  ::v-deep .p-inputgroup {
    .p-inputgroup-addon {
      background: rgb(28,43,64);
      border-top-left-radius: 18px;
      border-bottom-left-radius: 18px;
      border-color: transparent;
    }

    .p-inputtext {
      background: rgb(28,43,64);
      border-top-right-radius: 18px !important;
      border-bottom-right-radius: 18px !important;
      border-left-width: 0;
      box-shadow: none;
      border-color: transparent;
      color: rgba(255, 255, 255, 0.8);

      &::placeholder {
        color: rgba(255, 255, 255, 0.5);
      }
    }
  }

  button {
    border-radius: 19px;
    img {
      margin-left: auto;
    }
    .login {
      margin-right: auto;
    }
  }
}
</style>
