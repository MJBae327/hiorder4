<template>
  <v-app id="inspire">
    <header>
      <v-btn
        icon
        @click="$router.push('/')"
        style="position: absolute; top: 40px; left: 20px;"
      >
        <v-icon>mdi-home</v-icon>
      </v-btn>
      <h1>HiOrder</h1>
      <p>가게의 주문과 재고 관리를 간편하게</p>
    </header>

    <router-view></router-view>

    <footer class="footer">
      <p>&copy; 2024 HiOrder. All rights reserved.</p>
    </footer>
  </v-app>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style scoped>
/* 헤더와 푸터 스타일만 유지 */
header {
  background-color: #009688;
  color: #fff;
  padding: 40px 20px;
  margin-bottom: 30px;
}
header h1 {
  margin: 0;
  font-size: 2.8em;
  font-weight: bold;
}
header p {
  font-size: 1.2em;
  margin: 10px 0;
}
.footer {
  background-color: #00796b;
  color: #fff;
  padding: 20px;
  position: relative;
  bottom: 0;
  width: 100%;
}
.footer p {
  margin: 0;
  font-size: 1em;
}
</style>