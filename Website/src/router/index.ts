import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/login', component: () => import('@/views/login/LoginPage.vue') },
        {
            path: '/',
            redirect: '/login',
            children: [
                {
                    path: '/signature',
                    component: () => import('@/views/layout/LayoutContainer.vue'),
                    meta: { refreshPage: true }
                },
                {
                    path: '/blockchain',
                    component: () => import('@/views/mainpage/BlockChain.vue'),
                    meta: { refreshPage: true }
                },
                {
                  path: '/blockchain/:words',
                  component: () => import('@/views/mainpage/Sonpage4tx.vue'),
                  meta: { refreshPage: true }
                },
                {
                  path: '/blockchain/block/:words',
                  component: () => import('@/views/mainpage/Sonpage4bl.vue'),
                  meta: { refreshPage: true }
                },
                {
                  path: '/notary',
                  component: () => import('@/views/mainpage/PSing.vue'),
                  meta: { refreshPage: true }
                },
                {
                  path:'/abc',
                  component: () => import('@/views/mainpage/abc.vue'),
                  meta: { refreshPage: true }
                }
            ]
        }
    ]
})

router.afterEach((to, from) => {
    if (to.path !== from.path) {
      if (!sessionStorage.getItem('reloaded')) {
        sessionStorage.setItem('reloaded', 'true');
        window.location.reload();
      } else {
        sessionStorage.removeItem('reloaded');
      }
    }
  });

export default router