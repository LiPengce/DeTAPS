import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(persist)

export default pinia

import { usePsingStore } from '@/store/pb.ts'
export { usePsingStore }

import { useTokenStore } from '@/store/token.ts'
export { useTokenStore }

import { useLoginStore } from '@/store/login.ts'
export { useLoginStore }