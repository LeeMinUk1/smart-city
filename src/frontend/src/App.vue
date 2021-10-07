<template>
  <router-view/>
</template>

<style lang="scss">
html,
body {
  width: 100%;
  height: 100%;
}

body {
  margin: 0px;
  display: flex;
}

#app {
  flex: auto;
  display: flex;
  gap: $spacing-xs;
  font-family: $font-family;
  -webkit-font-smoothing: $webkit-font-smoothing;
  -moz-osx-font-smoothing: $moz-osx-font-smoothing;
  background-color: $background-color;
  color: $foreground-color;
}
</style>
