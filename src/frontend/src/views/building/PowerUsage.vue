<template>
  <div class="progressbar flex-column gap-sm" v-if="!unityLoaded">
    <ProgressBar :value="unityLoadingProgress" />
    <span>Loading...</span>
  </div>
  <canvas ref="canvas" width=1600 height=900 else></canvas>
<!--  <canvas ref="canvas" else></canvas>-->
</template>

<script lang="ts">
import {Vue, Options, Ref} from 'vue-property-decorator';
import ProgressBar from 'primevue/progressbar';
import {inject} from "vue";
declare function createUnityInstance(a: any, b: any, c: any): any;

@Options({
  components: {ProgressBar}
})
export default class PowerUsage2 extends Vue {
  @Ref() readonly canvas!: HTMLCanvasElement;

  private buildUrl = "siheungsi_out_allbuildings/Build";
  // private loaderUrl = this.buildUrl + "/siheungsi_out_allbuildings.loader.js";
  private config = {
    dataUrl: this.buildUrl + "/siheungsi_out_allbuildings.data",
    frameworkUrl: this.buildUrl + "/siheungsi_out_allbuildings.framework.js",
    codeUrl: this.buildUrl + "/siheungsi_out_allbuildings.wasm",
    streamingAssetsUrl: "StreamingAssets",
    companyName: "DefaultCompany",
    productName: "JWD",
    productVersion: "0.1",
    showBanner: this.unityShowBanner,
  };
  unityLoadingProgress = 0;
  unityLoaded = false;
  unityInstance: any;

  mounted() {
    this.unityLoadingProgress = 0;
    this.unityLoaded = false;

    createUnityInstance(this.canvas, this.config, (progress: any) => {
      console.log(100 * progress + "%");
      this.unityLoadingProgress = 100 * Math.round(progress);
    }).then((unityInstance: any) => {
      this.unityInstance = unityInstance;
      this.unityLoadingProgress = 100;
      this.unityLoaded = true;
      this.unityInstance.SendMessage('ScriptManager', 'comfortPrediction_jsCall');

    }).catch((message: any) => {
      alert(message);
    });

    const emitter: any = inject('emitter');
    emitter.on('full-screen-event', () => {
      this.unityInstance.SetFullscreen(1);
    });
  }

  private unityShowBanner(msg: any, type: any) {
    console.error('Unity:', msg);
  }
}
</script>

<style lang="scss" scoped>
canvas {
  width: 100%;
  height: 100%;
  aspect-ratio: 1 / 0.5;
}

.progressbar {
  position: absolute;
  width: 300px;
  height: 50px;
  top: 50%;
  left: 50%;
  align-items: center;

  .p-progressbar {
    align-self: stretch;
    background-color: var(--bluegray-300);
  }
}
</style>
