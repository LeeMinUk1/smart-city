<template>
  <div class="file-upload">
    <div class="file-upload-input">
      <Button label="삭제" class="p-button-sm" @click="deleteFiles()" v-if="editable" />
      <span class="m-l-auto">{{ formatBytes(totalFileSize, 0) }}/10MB</span>
      <input type="file" ref="file" class="input-text" @change="fileAdded" multiple />
      <Button label="찾기" class="btn-file-search" @click="openFileDialog()" v-if="editable" />
    </div>
    <!-- <ul class="file-list">
      <li>
        <button class="btn-delete" @click="deleteFiles()" v-if="editable">삭제</button>
        <span class="file-name">파일명</span>
      </li>
      <li>
        <button class="btn-delete" @click="deleteFiles()" v-if="editable">삭제</button>
        <span class="file-name">파일명</span>
      </li>
    </ul> -->
    <DataTable
        :value="fileCollection.files"
        v-model:selection="selectedFiles"
        dataKey="id"
        responsiveLayout="scroll"
        v-if="editable"
    >
      <Column selectionMode="multiple" headerStyle="width: 3em"></Column>
      <Column field="originalFileName" header="파일명"></Column>
      <Column header="용량" bodyStyle="text-align: right">
        <template #body="slotProps">
          <span>{{ formatBytes(slotProps.data.fileSize, 0) }}</span>
        </template>
      </Column>
    </DataTable>
    <DataTable :value="fileCollection.files" dataKey="id" responsiveLayout="scroll" v-else>
      <Column field="originalFileName" header="파일명"></Column>
      <Column header="용량" bodyStyle="text-align: right">
        <template #body="slotProps">
          <span>{{ formatBytes(slotProps.data.fileSize, 0) }}</span>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<script lang="ts">
import { PropType } from 'vue';
import { Options, Vue, Ref, Model, Prop } from 'vue-property-decorator';
import { PortalFile, FileCollection } from '@/types';

@Options({})
export default class FileUpload extends Vue {
  @Model('modelValue', { type: Object as PropType<FileCollection> }) readonly fileCollection!: FileCollection;
  @Prop({ type: Boolean, default: true }) readonly editable!: boolean;
  @Ref() readonly file!: HTMLInputElement;

  selectedFiles: PortalFile[] = [];

  private idSequence = 0;

  openFileDialog() {
    this.file.click();
  }

  get totalFileSize(): number {
    let size = 0;
    this.fileCollection.files.forEach((file) => (size += file.fileSize));
    return size;
  }

  fileAdded() {
    if (this.file.files) {
      for (let i = 0; i < this.file.files.length; i++) {
        const file: File = this.file.files[i];
        this.fileCollection.files.push({
          id: this.idSequence--,
          originalFileName: file.name,
          fileSize: file.size
        });
        this.fileCollection.added.push(file);
      }
      // this.change();
    }
  }

  deleteFiles() {
    this.selectedFiles.forEach((f1) => {
      let index = this.fileCollection.files.findIndex((f2) => {
        return f1.id === f2.id;
      });
      if (index > -1) {
        this.fileCollection.files.splice(index, 1);
      }

      index = this.fileCollection.added.findIndex((f2) => {
        return f1.originalFileName === f2.name;
      });
      if (index > -1) {
        this.fileCollection.added.splice(index, 1);
      }

      if (f1.id) {
        this.fileCollection.deleted.push(f1.id);
      }
    });

    this.selectedFiles = [];

    // this.change();
  }

  // @Emit()
  // change() {
  //   return this.fileCollection;
  // }

  formatBytes(bytes: number, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + sizes[i];
  }
}
</script>

<style lang="scss" scoped>
@import '~@/assets/style/components/FileUpload';
</style>
