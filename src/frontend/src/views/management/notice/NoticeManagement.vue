<template src="./NoticeManagement.html" />

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import DataTable from 'primevue/datatable';
import {Notice, NoticeListCondition, PageEvent, User} from "@/types";
import {useConfirm} from "primevue/useconfirm";
import * as noticeApi from "@/api/notice-api";
import * as utility from "@/utility";
import {useRouter} from "vue-router";

@Options({})
export default class NoticeManagement extends Vue {
  @Ref() readonly dataTable!: DataTable;
  @Ref() readonly file!: HTMLInputElement;

  router = useRouter();

  condition: NoticeListCondition = {
    keyword: '',
    limit: 10,
    offset: 0
  };

  loading = false;
  list: Notice[] = [];
  totalRecords = 0;
  selectedRow: Notice | null = null;

  displayDialog = false;
  dialogMode: 'add' | 'update' = 'add';
  notice = new Notice();

  inputError = {
    title: {
      empty: false,
      tooLong: false
    },
    content: {
      empty: false,
      tooLong: false
    }
  };

  confirm = useConfirm();

  mounted() {
    this.load();
  }

  onPage(event: PageEvent) {
    this.condition.limit = event.rows;
    this.condition.offset = event.rows * event.page;
    this.load();
  }

  load() {
    this.loading = true;
    noticeApi.list(this.condition).then(response => {
      const page = response.data;
      utility.setRowNumbers(page, this.condition.limit, this.condition.offset);
      this.list = page.data;
      this.totalRecords = page.totalRecords;
    }).catch(error => {
      console.log(error);
    }).finally(() => this.loading = false);
  }

  onRowSelect(event: any) {
    console.log(event.data);
    this.openUpdateDialog(this.selectedRow!);
  }

  openAddDialog() {
    this.notice = new Notice();
    this.dialogMode = 'add';
    this.displayDialog = true;
  }

  openUpdateDialog(oldNotice: Notice) {
    this.notice = Object.assign([], oldNotice);
    this.dialogMode = 'update';
    this.displayDialog = true;
  }

  requestDelete() {
    this.confirm.require({
      message: '공지 사항을 삭제합니다.',
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
    noticeApi.remove(this.notice.id)
        .then(response => {
          this.load();
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.notice = new Notice();
      this.displayDialog = false;
    });
  }

  requestSave() {
    this.inputError.title.empty = this.notice.title.trim().length === 0;
    this.inputError.title.tooLong = this.notice.title.trim().length > 100;

    this.inputError.content.empty = this.notice.content.trim().length === 0;
    this.inputError.content.tooLong = this.notice.content.trim().length > 2000;

    if (this.inputError.title.empty ||
        this.inputError.title.tooLong ||
        this.inputError.content.empty ||
        this.inputError.content.tooLong) {
      return;
    }

    // console.log("files:", this.file);
    if (this.file.files && this.file.files.length > 0) {
      // console.log('file count:', this.file.files.length);
      this.notice.attachment = this.file.files[0];
    }
    // console.log("attachment:", this.notice.attachment);

    this.confirm.require({
      message: '공지 사항을 ' + (this.dialogMode === 'add' ? '추가' : '수정') + '합니다.',
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
    noticeApi
        .addOrUpdate({...this.notice}, this.dialogMode)
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
            // this.load();
            this.router.go(0);
          }
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.notice = new Notice();
      this.displayDialog = false;
      // this.file.files = null;
    });
  }

  close() {
    this.displayDialog = false;
    // this.file.files = null;
  }
}
</script>

<style lang="scss" src="./NoticeManagement.scss" />
