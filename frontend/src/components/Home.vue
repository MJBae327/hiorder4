<template>
    <div class="home">  
      <div class="hero-section">
        <h2>편리한 주문 관리 시스템</h2>
        <p>비즈니스에 맞춘 완벽한 주문 솔루션을 만나보세요.</p>
        <!-- 메뉴로 이동 -->
        <button @click="navigateTo('/menus/menus')" class="cta-button">메뉴 보기</button>
      </div>
  
      <div class="features">
        <h2>주요 기능</h2>
  
        <!-- 메뉴 관리 -->
        <div class="feature">
          <div class="feature-icon">📋</div>
          <div class="feature-text">
            <button @click="navigateTo('/menus/menus')" class="cta-button">메뉴 관리</button>
            <p>직관적인 인터페이스로 메뉴를 쉽게 추가, 수정, 삭제할 수 있습니다.</p>
          </div>
        </div>
  
        <!-- 주문 관리 -->
        <div class="feature">
          <div class="feature-icon">📈</div>
          <div class="feature-text">
            <button @click="navigateTo('/orders/orders')" class="cta-button">주문 관리</button>
            <p>주문 상태를 실시간으로 확인하고 관리할 수 있습니다.</p>
          </div>
        </div>
  
        <!-- 재고 관리 -->
        <div class="feature">
          <div class="feature-icon">📦</div>
          <div class="feature-text">
            <button @click="navigateTo('/inventories/inventories')" class="cta-button">재고 관리</button>
            <p>재고를 쉽게 추적하고, 부족할 때 알림을 받을 수 있습니다.</p>
          </div>
        </div>
  
        <!-- 링크 없는 기능들 -->
        <div class="feature">
          <div class="feature-icon">🔥</div>
          <div class="feature-text">
            <button @click="showHotMenu" class="cta-button">지금 가장 핫한 메뉴 보기</button>
            <p>가장 많이 주문된 메뉴를 한눈에 확인해 보세요.</p>
          </div>
        </div>
  
        <div class="feature">
          <div class="feature-icon">💡</div>
          <div class="feature-text">
            <button @click="recommendBasedOnPeople" class="cta-button">인원에 맞게 추천받기</button>
            <p>고객의 인원수에 맞는 메뉴를 추천해 드립니다.</p>
          </div>
        </div>
  
        <div class="feature">
          <div class="feature-icon">💰</div>
          <div class="feature-text">
            <button @click="recommendBasedOnBudget" class="cta-button">예산에 맞게 추천받기</button>
            <p>고객의 예산에 맞는 메뉴를 추천해 드립니다.</p>
          </div>
        </div>
      </div> <!-- .features 닫는 태그 추가 -->
    </div> <!-- .home 닫는 태그 추가 -->
  </template>
  
  <script>
  export default {
    name: 'Home',
    methods: {
      navigateTo(path) {
        this.$router.push(path);
      },
      showHotMenu() {
        // "지금 가장 핫한 메뉴 보기" 버튼 클릭 시 동작
        alert('지금 가장 핫한 메뉴를 표시합니다!');
      },
      recommendBasedOnPeople() {
        // "인원에 맞게 추천받기" 버튼 클릭 시 동작
        alert('인원에 맞는 메뉴를 추천합니다!');
      },
      recommendBasedOnBudget() {
        // "예산에 맞게 추천받기" 버튼 클릭 시 동작
        alert('예산에 맞는 메뉴를 추천합니다!');
      }
    }
  };
  </script>
  
  <style scoped>
  /* 기존 스타일링 유지 */
  .hero-section {
    position: relative;
    color: #000;
    padding: 80px 20px;
    text-align: center;
  }
  .hero-section::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: url('https://enterprise.kt.com/entpf/images/product/webResource/P_PD_NE_00_301-s1-img01_re.png') no-repeat center center/cover;
    filter: blur(8px);
    z-index: -1;
  }
  .hero-section h2 {
    font-size: 2.5em;
    margin: 0;
  }
  .hero-section p {
    font-size: 1.2em;
    margin: 20px 0;
  }
  .cta-button {
    display: inline-block;
    background-color: #fff;
    color: #009688;
    font-size: 1.2em;
    font-weight: bold;
    padding: 15px 30px;
    border-radius: 30px;
    text-decoration: none;
    transition: background-color 0.3s, color 0.3s, transform 0.3s;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    cursor: pointer;
  }
  .cta-button:hover {
    background-color: #009688;
    color: #fff;
    transform: scale(1.05);
  }
  .cta-button:active {
    background-color: #00796b;
    color: #fff;
    transform: scale(0.95);
  }
  .features {
    padding: 40px 20px;
    background-color: #ffffff;
  }
  .features h2 {
    font-size: 2em;
    margin-bottom: 20px;
  }
  .features .feature {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin-bottom: 20px;
    padding: 20px;
    border-radius: 8px;
    background-color: #e0f7fa;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: background-color 0.3s, transform 0.3s;
    gap: 20px;
  }
  .features .feature:hover {
    background-color: #b2ebf2;
    transform: scale(1.03);
  }
  .features .feature-icon {
    font-size: 2.5em;
  }
  .features .feature-text {
    text-align: left;
    flex: 1;
  }
  .features .feature-text button {
    display: inline-block;
    margin-bottom: 10px;
  }
  </style>
  