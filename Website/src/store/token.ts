import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useTokenStore = defineStore('Token', () => {
    const token = ref('')
    
    const setToken = (tk) => {
        token.value = tk;
    }

    return {
        token, setToken
    }
},{
    persist: true,
})