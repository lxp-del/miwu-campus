import { defineConfig } from 'vite';
import uni from '@dcloudio/vite-plugin-uni';

export default defineConfig({
  plugins: [uni()],
  server: {
    port: 5173,
    proxy: {
      // 匹配所有以 /lxp-api 开头的请求
      '/lxp-api': {
        target: 'http://localhost:9090',
        changeOrigin: true,
        // 核心配置：路径重写
        rewrite: (path) => path.replace(/^\/lxp-api/, '')
      }
    }
  }
});