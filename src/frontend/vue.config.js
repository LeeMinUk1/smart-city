module.exports = {
  css: {
    loaderOptions: {
      sass: {
        prependData: `@import "@/assets/style/app.scss";`
      }
    }
  },
  devServer: {
    port: 8081,
    proxy: {
      '/api/': {
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true
      }
    }
  }
};
