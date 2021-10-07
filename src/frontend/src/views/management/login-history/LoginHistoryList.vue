<template src="./LoginHistoryList.html" />

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import DataTable from "primevue/datatable";
import {PageEvent, LoginHistoryListCondition, LoginHistory} from "@/types";
import * as loginHistoryApi from "@/api/login-history-api";
import * as utility from "@/utility";

@Options({})
export default class LoginHistoryList extends Vue {
  @Ref() readonly menu!: any;
  @Ref() readonly dataTable!: DataTable;

  keywordTypeList = [
    {label: '전체', value: 'all'},
    {label: '이름', value: 'name'},
    {label: 'ID', value: 'id'},
    {label: '소속', value: 'department'},
    {label: '접속 IP', value: 'ip'},
  ];

  condition: LoginHistoryListCondition = {
    beginDate: '',
    endDate: '',
    searchType: this.keywordTypeList[0].value,
    keyword: '',
    limit: 10,
    offset: 0
  };
  beginDate: any = null;
  endDate: any = null;

  loading = false;
  list: LoginHistory[] = [];
  totalRecords = 0;

  mounted() {
    this.load();
  }

  onPage(event: PageEvent) {
    this.condition.limit = event.rows;
    this.condition.offset = event.rows * event.page;
    this.load();
  }

  load() {
    this.condition.beginDate = this.beginDate === null ? '' : this.formatDate(this.beginDate) + ' 00:00';
    this.condition.endDate = this.endDate === null ? '' : this.formatDate(this.endDate) + ' 23:59';
    this.loading = true;
    loginHistoryApi.list(this.condition).then(response => {
      const page = response.data;
      utility.setRowNumbers(page, this.condition.limit, this.condition.offset);
      this.list = page.data;
      this.totalRecords = page.totalRecords;
    }).catch(error => {
      console.log(error);
    }).finally(() => this.loading = false);
  }

  private formatDate(date: any): string {
    if (date) {
      let month = '';
      if (date.getMonth() + 1 < 10) {
        month = '0' + (date.getMonth() + 1);
      } else {
        month = date.getMonth() + 1;
      }

      let day = '';
      if (date.getDate() < 10) {
        day = '0' + date.getDate();
      } else {
        day = date.getDate();
      }
      return date.getFullYear() + '-' + month + '-' + day;
    }
    return '';
  }
}
</script>

<style lang="scss" src="./LoginHistoryList.scss" />
