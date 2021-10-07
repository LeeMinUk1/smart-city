<template src="./NoticeList.html" />

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import DataTable from 'primevue/datatable';
import {Notice, NoticeListCondition, PageEvent, User} from "@/types";
import {useConfirm} from "primevue/useconfirm";
import * as noticeApi from "@/api/notice-api";
import * as utility from "@/utility";
import {useRouter} from "vue-router";

@Options({})
export default class NoticeList extends Vue {
  @Ref() readonly dataTable!: DataTable;

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
  notice = new Notice();

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
    this.openDialog(this.selectedRow!);
  }

  openDialog(oldNotice: Notice) {
    this.notice = Object.assign([], oldNotice);
    this.displayDialog = true;
  }

  close() {
    this.displayDialog = false;
  }
}
</script>

<style lang="scss" src="./NoticeList.scss" />
