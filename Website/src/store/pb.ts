import { defineStore } from 'pinia'
import { reactive } from 'vue'

export const usePsingStore = defineStore('PSing', () => {
    const blockinf = reactive({
        blockhash: "1",
        blocknumber: "1",
        GasLimit: "1",
        GasPrice: "1",
        from: "1",
        nonce: "1", 
        status: "1",
        to: "1",
        inputData: "1",
        current_time: "1"
    })
    
    const setData = (data) => {
        blockinf.blockhash = data.blockhash;
        blockinf.blocknumber = data.blocknumber;
        blockinf.GasLimit = data.GasLimit;
        blockinf. GasPrice = data. GasPrice;
        blockinf.from = data.from;
        blockinf.nonce = data.nonce;
        blockinf.status = data.status;
        blockinf.to = data.to;
        blockinf.inputData = data.inputData;
        blockinf.current_time = data.current_time;
    }

    return {
        blockinf, setData
    }
},{
    persist: true,
})