import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLoginStore = defineStore('Login', () => {
    const role=ref('')
    const username=ref('')
    const password=ref('password')
    
    const setRole = (rl) => {
        role.value = rl;
    }

    const setUsername = (un) => {
        username.value = un;
    }

    const setPassword = (pw) => {
        password.value = pw;
    }

    return {
       role,username,password,setRole,setUsername,setPassword
    }
},{
    persist: true,
})