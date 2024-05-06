/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router'
import routes from "@/router/routes";

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('accessToken');

  // 인증이 필요한 페이지인 경우
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/Login'); // 로그인 페이지로 리다이렉션
  } else {
    next(); // 그 외의 경우는 그냥 진행
  }
});

export default router
