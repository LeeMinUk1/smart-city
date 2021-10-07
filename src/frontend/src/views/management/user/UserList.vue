<template src="./UserList.html"/>

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import {useConfirm} from "primevue/useconfirm";
import {PageEvent, User, UserListCondition} from "@/types";
import * as userApi from '@/api/user-api';
import * as utility from '@/utility';
import DataTable from "primevue/datatable";

@Options({})
export default class UserList extends Vue {
  @Ref() readonly menu!: any;
  @Ref() readonly dataTable!: DataTable;

  typeList = [
    {label: '전체', value: 'all'},
    {label: '내부', value: 'internal'},
    {label: '외부', value: 'external'}
  ];

  keywordTypeList = [
    {label: '전체', value: 'all'},
    {label: '이름', value: 'name'},
    {label: '소속', value: 'department'}
  ];

  condition: UserListCondition = {
    suspendedOrDeleted: false,
    type: this.typeList[0].value,
    searchType: this.keywordTypeList[0].value,
    keyword: '',
    limit: 10,
    offset: 0
  };

  loading = false;
  list: User[] = [];
  totalRecords = 0;

  menuItems = [
    {label: '수정', icon: 'pi pi-pencil', command: () => this.openUpdateDialog()},
    {label: '삭제', icon: 'pi pi-times', command: () => this.requestDelete()},
    {label: '사용 정지', icon: 'pi pi-times-circle', command: () => this.requestSuspend()},
    {label: '재사용', icon: 'pi pi-replay', command: () => this.requestUnsuspend()},
    // {label: '비밀번호 초기화', icon: 'pi pi-lock-open'},
  ];
  selectedUser = {} as User;

  displayDialog = false;
  dialogMode: 'add' | 'update' = 'add';
  user = new User();
  confirmedPassword = '';

  inputError = {
    name: {
      empty: false,
      tooLong: false
    },
    department: {
      empty: false,
      tooLong: false
    },
    id: {
      empty: false,
      tooLong: false
    },
    password: {
      empty: false,
      tooLong: false
    },
    confirmedPassword: {
      empty: false,
      tooLong: false,
      mismatch: false
    }
  };

  confirm = useConfirm();

  mounted() {
    this.user.type = this.typeList[1].value;
    this.load();
  }

  onPage(event: PageEvent) {
    this.condition.limit = event.rows;
    this.condition.offset = event.rows * event.page;
    this.load();
  }

  load() {
    this.loading = true;
    userApi.list(this.condition).then(response => {
      const page = response.data;
      utility.setRowNumbers(page, this.condition.limit, this.condition.offset);
      this.list = page.data;
      this.totalRecords = page.totalRecords;
    }).catch(error => {
      console.log(error);
    }).finally(() => this.loading = false);
  }

  toggleMenu(event: MouseEvent, user: User) {
    this.selectedUser = user;
    this.menu.toggle(event);
  }

  openAddDialog() {
    this.dialogMode = 'add';
    this.displayDialog = true;
  }

  openUpdateDialog() {
    this.user = Object.assign([], this.selectedUser);
    this.confirmedPassword = this.user.password;
    this.dialogMode = 'update';
    this.displayDialog = true;
  }

  requestDelete() {
    this.confirm.require({
      message: '사용자 계정을 삭제합니다.',
      header: '확인',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: '확인',
      rejectLabel: '취소',
      accept: () => {
        this.delete();
      }
    });
  }

  delete() {
    userApi.remove(this.selectedUser.id)
        .then(response => {
          this.load();
        }).catch(error => {
          console.log(error)
        }).finally(() => {
          this.user = new User();
          this.user.type = this.typeList[0].value;
          this.confirmedPassword = '';
          this.displayDialog = false;
        });
  }

  requestSuspend() {
    this.confirm.require({
      message: '사용자 계정을 정지합니다.',
      header: '확인',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: '확인',
      rejectLabel: '취소',
      accept: () => {
        this.suspendUser();
      }
    });
  }

  suspendUser() {
    userApi.suspend(this.selectedUser.id)
        .then(response => {
          this.load();
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.user = new User();
      this.user.type = this.typeList[0].value;
      this.confirmedPassword = '';
      this.displayDialog = false;
    });
  }

  requestUnsuspend() {
    this.confirm.require({
      message: '사용자 계정을 재사용합니다.',
      header: '확인',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: '확인',
      rejectLabel: '취소',
      accept: () => {
        this.unsuspendUser();
      }
    });
  }

  unsuspendUser() {
    userApi.unsuspend(this.selectedUser.id)
        .then(response => {
          this.load();
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.user = new User();
      this.user.type = this.typeList[0].value;
      this.confirmedPassword = '';
      this.displayDialog = false;
    });
  }

  requestSave() {
    this.inputError.name.empty = this.user.name.trim().length === 0;
    this.inputError.name.tooLong = this.user.name.trim().length > 20;

    this.inputError.department.empty = this.user.department.trim().length === 0;
    this.inputError.department.tooLong = this.user.department.trim().length > 20;

    this.inputError.id.empty = this.user.id.trim().length === 0;
    this.inputError.id.tooLong = this.user.id.trim().length > 20;

    this.inputError.password.empty = this.user.password.trim().length === 0;
    this.inputError.password.tooLong = this.user.password.trim().length > 20;

    this.inputError.confirmedPassword.empty = this.confirmedPassword.trim().length === 0;
    this.inputError.confirmedPassword.tooLong = this.confirmedPassword.trim().length > 20;

    this.inputError.confirmedPassword.mismatch = false;

    if (this.inputError.name.empty ||
        this.inputError.name.tooLong ||
        this.inputError.department.empty ||
        this.inputError.department.tooLong ||
        this.inputError.id.empty ||
        this.inputError.id.tooLong ||
        this.inputError.password.empty ||
        this.inputError.password.tooLong ||
        this.inputError.confirmedPassword.empty ||
        this.inputError.confirmedPassword.tooLong) {
      return;
    }

    this.inputError.confirmedPassword.mismatch = this.user.password !== this.confirmedPassword;
    if (this.inputError.confirmedPassword.mismatch) {
      return;
    }

    this.confirm.require({
      message: '사용자 계정을 ' + (this.dialogMode === 'add' ? '추가' : '수정') + '합니다.',
      header: '확인',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: '확인',
      rejectLabel: '취소',
      accept: () => {
        this.save();
      }
    });
  }

  save() {
    userApi
        .addOrUpdate({...this.user}, this.dialogMode)
        .then(response => {
          if (!response.data.succeeded) {
            this.confirm.require({
              message: response.data.message,
              header: '확인',
              icon: 'pi pi-exclamation-triangle',
              acceptLabel: '확인',
              rejectClass: 'dialog-invisible-reject'
            });
          } else {
            this.load();
          }
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.user = new User();
      this.user.type = this.typeList[1].value;
      this.confirmedPassword = '';
      this.displayDialog = false;
    });
  }

  close() {
    this.displayDialog = false;
  }
}
</script>

<style lang="scss" src="./UserList.scss"/>
