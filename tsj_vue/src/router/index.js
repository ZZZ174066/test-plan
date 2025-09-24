import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Login',
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/admin',
      name: 'Admin',
      component: () => import('../views/admin.vue'),
    },
    {
      path: '/student',
      name: 'Student',
      component: () => import('../views/student.vue'),
    },
    {
      path: '/teacher',
      name: 'Teacher',
      component: () => import('../views/teacher.vue'),
    },
    // 可以在这里添加新的路由配置
  ],
})
export default router