<template src="./QnaManagement.html" />

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import DataTable from 'primevue/datatable';
import {Qna, QnaListCondition, PageEvent} from "@/types";
import {useConfirm} from "primevue/useconfirm";
import * as qnaApi from "@/api/qna-api";
import * as utility from "@/utility";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/store/auth";

@Options({})
export default class NoticeManagement extends Vue {
  @Ref() readonly dataTable!: DataTable;
  @Ref() readonly file!: HTMLInputElement;

  router = useRouter();
  auth = useAuthStore();

  condition: QnaListCondition = {
    keyword: '',
    limit: 10,
    offset: 0
  };

  loading = false;
  list: Qna[] = [];
  totalRecords = 0;
  selectedRow: Qna | null = null;

  displayDialog = false;
  dialogMode: 'add' | 'update' = 'add';
  qna = new Qna();

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
    qnaApi.list(this.condition).then(response => {
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
    this.qna = new Qna();
    this.dialogMode = 'add';
    this.displayDialog = true;
  }

  openUpdateDialog(oldQna: Qna) {
    this.qna = Object.assign([], oldQna);
    this.dialogMode = 'update';
    this.displayDialog = true;
  }

  requestDelete() {
    this.confirm.require({
      message: 'Q & A를 삭제합니다.',
      header: '확인',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: '확인',
      rejectLabel: '취소',
      accept: () => {
        this.qnaDelete();
      }
    });
  }

  qnaDelete() {
    qnaApi.remove(this.qna.id)
        .then(response => {
          this.load();
        }).catch(error => {
      console.log(error)
    }).finally(() => {
      this.qna = new Qna();
      this.displayDialog = false;
    });
  }

  requestSave() {
    this.inputError.title.empty = this.qna.title.trim().length === 0;
    this.inputError.title.tooLong = this.qna.title.trim().length > 100;

    this.inputError.content.empty = this.qna.content.trim().length === 0;
    this.inputError.content.tooLong = this.qna.content.trim().length > 2000;

    if (this.inputError.title.empty ||
        this.inputError.title.tooLong ||
        this.inputError.content.empty ||
        this.inputError.content.tooLong) {
      return;
    }

    // console.log("files:", this.file);
    if (this.file.files && this.file.files.length > 0) {
      // console.log('file count:', this.file.files.length);
      this.qna.attachment = this.file.files[0];
    }
    // console.log("attachment:", this.notice.attachment);

    this.confirm.require({
      message: 'Q & A를 ' + (this.dialogMode === 'add' ? '추가' : '수정') + '합니다.',
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
    qnaApi
        .addOrUpdate({...this.qna}, this.dialogMode)
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
      this.qna = new Qna();
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

<style lang="scss" src="./QnaManagement.scss" />
