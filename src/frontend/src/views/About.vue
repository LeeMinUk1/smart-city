<template>
  <div id="unity-container" class="unity-desktop">
    <canvas ref="canvas" id="unity-canvas" width="1600" height="900"></canvas>
<!--    <div id="unity-loading-bar" :style="{display: display}">-->
<!--      <div id="unity-logo"></div>-->
<!--      <div id="unity-progress-bar-empty">-->
<!--        <div id="unity-progress-bar-full"></div>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div id="unity-warning"> </div>-->
<!--    <div id="unity-footer">-->
<!--      <div id="unity-webgl-logo"></div>-->
<!--      <div id="unity-fullscreen-button"></div>-->
<!--      <div id="unity-build-title">JWD</div>-->
<!--    </div>-->
  </div>
</template>
<script lang="ts">
import { Vue, Options, Ref } from 'vue-property-decorator';
 declare function createUnityInstance(a: any, b: any, c: any): any;

@Options({})
export default class Home extends Vue {
  @Ref() readonly canvas!: HTMLCanvasElement;

  private buildUrl = "siheungsi_out_allbuildings/Build";
  private loaderUrl = this.buildUrl + "/siheungsi_out_allbuildings.loader.js";
  private config = {
    dataUrl: this.buildUrl + "/siheungsi_out_allbuildings.data",
    frameworkUrl: this.buildUrl + "/siheungsi_out_allbuildings.framework.js",
    codeUrl: this.buildUrl + "/siheungsi_out_allbuildings.wasm",
    streamingAssetsUrl: "StreamingAssets",
    companyName: "DefaultCompany",
    productName: "JWD",
    productVersion: "0.1",
    // showBanner: unityShowBanner,
  };

  mounted() {
    createUnityInstance(this.canvas, this.config, (progress: any) => {
      //progressBarFull.style.width = 100 * progress + "%";
      console.log(100 * progress + "%");
    }).then((unityInstance: any) => {
      console.log('XXX: ', unityInstance);
      // loadingBar.style.display = "none";
      // fullscreenButton.onclick = () => {
      //   unityInstance.SetFullscreen(1);
      // };
    }).catch((message: any) => {
      alert(message);
    });
  }
}
</script>