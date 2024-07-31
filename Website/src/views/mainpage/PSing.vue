<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import detectEthereumProvider from '@metamask/detect-provider'
import { JSEncrypt } from 'jsencrypt'
import Web3 from "web3";
import { usePsingStore } from '@/store'
import { useTokenStore,useLoginStore } from '@/store'
import CryptoJS from 'crypto-js';

const textarea = ref('');
const textarea2 = ref('');
const input1 = ref('');
const input2 = ref('');
const PSingStore = usePsingStore();
const TokenStore = useTokenStore();
const LoginStore = useLoginStore();
const result = ref({
  tracerId: 0,
  deTAPSsignId: '',
  notaryShare:'',
});

console.log(PSingStore.blockinf);
/* 
const a ='0x123';
const data = {
            blockhash: a,
            blocknumber: 12222,
            cumulativeGasUsed: 100,
            effectiveGasPrice: 1,
            from: '0xabc',
            gasUsed: 50,
            status: 'success',
            to: '0xdef',
            transactionHash: '0x456'
        }

        PSingStore.setData(data);
        console.log(PSingStore.blockinf);
 */

 const account = ref('');

const startApp = (provider) => {
      if (provider !== window.ethereum) {
        console.error('Do you have multiple wallets installed?');
      }
};


const getAccount = async () => {
      try {
        const provider = await detectEthereumProvider();
        if (provider) {
          startApp(provider);
          const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
          account.value = accounts[0];
        } else {
          console.log('Please install MetaMask!');
        }
      } catch (err) {
        if (err.code === 4001) {
          console.log('Please connect to MetaMask.');
        } else {
          console.error(err);
        }
      }
    };

    const ischeck2 = ref(false);
const getAccount1 = async (tracerId,deTAPSsignId,notaryShare) => {
  ischeck2.value = true;
     // 检查是否安装了MetaMask
     if (typeof window.ethereum === 'undefined') {
            alert('请安装MetaMask!');
        } else {
            const web3 = new Web3(window.ethereum);
            // 请求用户授权
                await window.ethereum.request({ method: 'eth_requestAccounts' });

                const contractABI = [
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"internalType": "uint256",
				"name": "tracerID",
				"type": "uint256"
			},
			{
				"indexed": true,
				"internalType": "uint256",
				"name": "DeTAPSsignId",
				"type": "uint256"
			},
			{
				"indexed": false,
				"internalType": "uint256",
				"name": "tracerId",
				"type": "uint256"
			},
			{
				"indexed": false,
				"internalType": "uint256",
				"name": "DeTAPSsignID",
				"type": "uint256"
			},
			{
				"indexed": false,
				"internalType": "uint256",
				"name": "notaryShareCount",
				"type": "uint256"
			}
		],
		"name": "NotaryShareSubmitted",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"internalType": "uint256",
				"name": "combinerID",
				"type": "uint256"
			},
			{
				"indexed": true,
				"internalType": "string",
				"name": "GID",
				"type": "string"
			},
			{
				"indexed": false,
				"internalType": "uint256",
				"name": "combinerId",
				"type": "uint256"
			},
			{
				"indexed": false,
				"internalType": "string",
				"name": "gid",
				"type": "string"
			},
			{
				"indexed": false,
				"internalType": "uint256",
				"name": "shareCount",
				"type": "uint256"
			}
		],
		"name": "SignShareSubmitted",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"internalType": "string",
				"name": "message",
				"type": "string"
			},
			{
				"indexed": false,
				"internalType": "string",
				"name": "DeTAPSsign",
				"type": "string"
			}
		],
		"name": "m_DeTAPSsignSubmitted",
		"type": "event"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "tracerId",
				"type": "uint256"
			},
			{
				"internalType": "uint256",
				"name": "DeTAPSsignId",
				"type": "uint256"
			},
			{
				"internalType": "string",
				"name": "encshare",
				"type": "string"
			}
		],
		"name": "submitEncshare",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "string",
				"name": "_message",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_DeTAPSsign",
				"type": "string"
			}
		],
		"name": "submitM_DeTAPSsign",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "combinerId",
				"type": "uint256"
			},
			{
				"internalType": "string",
				"name": "gid",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "share",
				"type": "string"
			}
		],
		"name": "submitSignShare",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "tracerId",
				"type": "uint256"
			},
			{
				"internalType": "uint256",
				"name": "DeTAPSsignId",
				"type": "uint256"
			}
		],
		"name": "getNotaryEncShares",
		"outputs": [
			{
				"internalType": "string[]",
				"name": "",
				"type": "string[]"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "combinerId",
				"type": "uint256"
			},
			{
				"internalType": "string",
				"name": "gid",
				"type": "string"
			}
		],
		"name": "getSignShares",
		"outputs": [
			{
				"internalType": "string[]",
				"name": "",
				"type": "string[]"
			}
		],
		"stateMutability": "view",
		"type": "function"
	}
];
                const contractAddress =  '0x6f08ceb8c1356f71c7e150d4b0e889c3c098f82b';
                const myContract = new web3.eth.Contract(contractABI, contractAddress);
                
                // 获取用户账户
                const accounts = await web3.eth.getAccounts();
                progress.value = 2;
              console.log(progress);

                //调用合约方法
                /*
                myContract.methods.submitEncshare(tracerId,deTAPSsignId,notaryShare).send({ from: accounts[0] })
                  .then(function(receipt){
                    console.log(receipt);
                  });*/

                  myContract.methods.submitEncshare(tracerId,deTAPSsignId,notaryShare).send({ 
                    from: accounts[0],
                    gasPrice: '900000000',
                    gas: 9000000 
                  })
    .then(function(receipt){
        // 交易成功处理
        console.log(receipt);
        //去掉receipt的每个值
        console.log("Transaction Hash: ", receipt.transactionHash);
        ischeck2.value = false;
    }).catch(function(error){
        // 交易失败处理
        console.error(error);
});
        }

}


const submit = function () {
  let encryptor = new JSEncrypt();
  let publicKey ='-----BEGIN PUBLIC KEY-----'+'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwzJCWTWJ+9X2MKaQPmfBiocesGcvi7yCC9QPkEbgftlRMtpuEcBx0tCgBcURHnOlPowScYA7jkaHHrrZezgnAyPq5liPj5a992D8dW7VxoJrpt68JDuSuTQBlKBtsbjiuPNxT4NLOmnpZ8cFpLdst+luEtlbolClE/9S5JuJxJNAnwgR2mfqe+ZmVaqhXfa0XQON2E55Tiy3d7rO/lDY1OifKzI9xikTHrtYCauIo7wVWAlX5LCwrALmNFvZdIon5qqq3WgJs/YH9sijL20K2llZewFF478qfQ2JsaEZjweJMu4t+ETCe4PsaV9pEsVQfOZ9rQvTF8HjmHLHBtmffQIDAQAB'+'-----END PUBLIC KEY-----';
  encryptor.setPublicKey(publicKey);
  let str = '123';
  console.log('加密前数据:%o', str);
  console.log('加密后数据:%o',encryptor.encrypt(str));
  let privateKey ='MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDDMkJZNYn71fYwppA+Z8GKhx6wZy+LvIIL1A+QRuB+2VEy2m4RwHHS0KAFxREec6U+jBJxgDuORoceutl7OCcDI+rmWI+Plr33YPx1btXGgmum3rwkO5K5NAGUoG2xuOK483FPg0s6aelnxwWkt2y36W4S2VuiUKUT/1Lkm4nEk0CfCBHaZ+p75mZVqqFd9rRdA43YTnlOLLd3us7+UNjU6J8rMj3GKRMeu1gJq4ijvBVYCVfksLCsAuY0W9l0iifmqqrdaAmz9gf2yKMvbQraWVl7AUXjvyp9DYmxoRmPB4ky7i34RMJ7g+xpX2kSxVB85n2tC9MXweOYcscG2Z99AgMBAAECggEATcNvFVU0BIVIEDe7TXFOdJ7CBisFkRjngkjYbnUeKMrxA15r7VyKvB7rMYFCAay2z0VEb71TfKnyWLa9v84uFJ0xjtUHVIAMbEpSKYKfXfdFZfRkZdbp7Cig3JJRQTVT9bTelnvK85mQKAzs3aNJh7dSZ+X6EJ8qNN5KKtMgPpRs1Xfhtg3cdygt/ywMqT/g/wHBIXk6UqTn0ceyilV7bVCbFEJ+8Zw3dWMog7IubX3uJsEFqU9rMM4SXame1FWYZ1luzKj813h8qLIS7LEbfzvEoI4NXojRSoWvlB60xT1SCOeS5mLMRLy3Wuq/tsdsbBdw0Uj2VprKvCxXRn68QQKBgQDw1wOcpery/uGToFc6OdgSt8nvvCecNAmMSWp6tg/MEXVpuP6QzEjqu5QYrY7qKNRAZYMrDso+Hnj8UhVAOoZoJJFdXn+bku5mFvlZ1fkt7ht+cVWK6LOkDYsMoQVugzWVlIgYJ4QRkxD4FYICERy5mgiKxwsMi2KyRJ/kIbGicQKBgQDPe7okw9oZjQixzxVMopb04a95EvYB0sEfmq8VAoy/RrM42Yqa8YyvzIWpSQE2digMb+3R+oz4tA9oXJ+Qkbd1YzAFAY/xy+iyNSpPfVu9nyvdXkMZ+dtVxRGcoRUp4AAe9HEgq4ahAj8Lqs4hNX8aLjhaLX5pTnspxKFOvhK7zQKBgGoOhWyqM/il/ThBV6gwaNJ1VNvZg6fp0nqQqe831WUMGyRfbtrc2w9jYygq5dCAAlgJW+XOMYmBac3TZ8BNBXs+/zSatJ8R6SHT5CKHc5+iR0E7L2xoZRNNU3pQPlAYP7SLIBNi82m51bMp5YJ7fqbNCg3y8Q6pIuFWDwMrQ0pBAoGBAMZmfZziWw/LuOCuMKPTZZ5MoMqZwvwDWACsnXbttUDCjmZyPwPirrD8VWOHF83INouODE+Z0muPYpxc1Ygcjt2FN0nSMJ76aUzfYdIZATzVk9BebrPMJFxj0W+A6Qfd1r/C5tpiqp4T082ISwueaCfUwrYYgqSYE1XhXI47WxaRAoGBAM4lKtXbFAfXNOl35DsG4lG3y+XdRHSfUS9EFCjlNQG3F4MUU/MsdLWLohj7N8WdeRTh1CR5npO42wI45EhiBzgterFQtESzd4h2J3DIbu+o8QanEXoX7IRgdjlxLgnKWkGNs7Zg6wPvKuevMSqx7dQWImfKspeIMcCULDK8ogs5';
  let str1=encryptor.encrypt(str)
  encryptor.setPrivateKey(privateKey);
  console.log(encryptor.decrypt(str1))
}

const progress = ref(0);
const filename1 = ref('');
const filename2 = ref('');
const upload1Validated = ref(false);
const upload2Validated = ref(false);
const base64String1 = ref('');
const kaggStr = ref('');
const beforeAvatarUpload1 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename1.value = file.name;
  console.log(filename1.value);
  if(filename1.value && filename2.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 50;
    textarea.value += filename1.value + ' 上传成功！\n';
  }
  console.log(progress.value);
  /*
  const reader = new FileReader();

reader.onload = (event) => {
  const fileContent = event.target.result;
  console.log(fileContent);
  // 在这里可以对文件内容进行进一步处理

  upload1Validated.value = true;
  kaggStr.value = fileContent;
  console.log(kaggStr);
};

reader.readAsText(file);
*/
//console.log(kaggStr);
kaggStr.value = file;
  return;
}


const uskStr = ref('');
const beforeAvatarUpload2 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename2.value = file.name;
  console.log(filename2.value);
  if(filename1.value && filename2.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 50;
    textarea.value += filename2.value + ' 上传成功！\n';
  }
  console.log(progress.value);
uskStr.value = file;
  return;
}

const publicKey = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwzJCWTWJ+9X2MKaQPmfBiocesGcvi7yCC9QPkEbgftlRMtpuEcBx0tCgBcURHnOlPowScYA7jkaHHrrZezgnAyPq5liPj5a992D8dW7VxoJrpt68JDuSuTQBlKBtsbjiuPNxT4NLOmnpZ8cFpLdst+luEtlbolClE/9S5JuJxJNAnwgR2mfqe+ZmVaqhXfa0XQON2E55Tiy3d7rO/lDY1OifKzI9xikTHrtYCauIo7wVWAlX5LCwrALmNFvZdIon5qqq3WgJs/YH9sijL20K2llZewFF478qfQ2JsaEZjweJMu4t+ETCe4PsaV9pEsVQfOZ9rQvTF8HjmHLHBtmffQIDAQAB
-----END PUBLIC KEY-----`;

const encryptData = (data) => {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(data);
};

const handleUpload = async () => {
      const data = new FormData();
      data.append('kaggStr', kaggStr.value);
      data.append('uskStr', uskStr.value);

      //修改按钮文字
      textvalue.value = '公证中';
      ischeck.value = true;

      for (let [key, value] of data.entries()) {
        console.log(`${key}: ${value}`);
      }

      axios.post('http://47.96.177.120:8080/notary/trace',data,{
        headers: {
          "Authorization":TokenStore.token, // 将请求头的键设置为"token"，并将值设置为token的值
          'Content-Type': 'multipart/form-data'
        },
      })
          .then(response =>{
            if(response.data.code == 0 && response.data.data){
              progress.value = 2;
              console.log(progress);
              console.log(response.data.data.tracerId);
              console.log(response.data.data.deTAPSsignId);
              console.log(response.data.data.notaryShare);
              result.value.tracerId = response.data.data.tracerId;
              result.value.deTAPSsignId = response.data.data.deTAPSsignId;
              result.value.notaryShare = response.data.data.notaryShare;
              textarea2.value = response.data.data.notaryShare;

              //修改按钮文字
              textvalue.value = '公证';
              ischeck.value = false;
            }
          })
          .catch(error =>{
              console.log(error);
          });

 };
const var8 = ref(0);

const table = ref(false);
const loading = ref(false);
const items = ref([]);

const checkinfo = async () =>{
  table.value = true;
  try{
    const response = await axios.get('http://47.96.177.120:8080/notary/record');
    if (Array.isArray(response.data)) {
      items.value = response.data;
      console.log(items.value);
      for(const i of items.value){
        console.log(i);

      }
    } else {
      console.log('Expected an array but received:', response.data);
    }
  }catch(error){
    console.error('Error fetching data:', error);
  }  
}

const formatTimestamp = (timestamp) => {
      const date = new Date(timestamp);  // 将时间戳字符串转换为Date对象
      const currentTime = new Date();  // 获取当前时间
      const difference = currentTime - date;  // 计算时间差，单位为毫秒
      //return Math.floor(difference / 1000);
      
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份需要加1，同时补0处理
      const day = date.getDate().toString().padStart(2, '0');  // 天数补0处理
      const hours = date.getHours().toString().padStart(2, '0');  // 小时补0处理
      const minutes = date.getMinutes().toString().padStart(2, '0');  // 分钟补0处理
      const seconds = date.getSeconds().toString().padStart(2, '0');  // 秒数补0处理
      const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;  // 格式化输出年月日时分秒
      return formattedDate;
     
}

const textvalue = ref("公证");
const ischeck = ref(false);


</script>

<template>
    <div class="common-layout">
    <el-container>
      <div style="width: 180px;">
      <el-aside width="100%" height="100%" class="layout_container" style="display: flex;flex-direction: column;justify-content: start;align-items: center;align-self:center;">
        <el-menu
          active-text-color="#ffd04b"
          background-color="transparent"
          :default-active="$route.path"
          text-color="#fff"
          router
          style="width: 100%;margin-top: 15%;"
        >
        <el-menu-item style="display: flex;justify-content: center;align-items: center;height: 50%;" index="#">
          <span style="color: #fff;font-size: 28px;">公证</span>
        </el-menu-item>
        <el-menu-item style="display: flex;justify-content: center;align-items: center;height: 50%;" index="/blockchain">
          <span style="font-size: 28px;">区块链</span>
        </el-menu-item>
        <el-menu-item style="display: flex;justify-content: center;align-items: center;height: 50%;">
          <el-button type = "text" @click="checkinfo()" style="color: #fff;font-size: 28px;">公证记录</el-button>
        </el-menu-item>
        </el-menu>
        <router-link to="/login" style="display: flex;justify-content: center;align-items: center;"><img src="@/img/退出.png" style="height: 70px;width: 70px;margin-top: 850%;margin-left: 0px;"></router-link>
      </el-aside>
      
      <el-drawer
    v-model="table"
    :with-header="false"
    direction="rtl"
    size="40%"
  >
  <div v-loading="loading" style="position: absolute; top: 0; left: 0; width: 100%; max-height: 100%; z-index: 10; overflow: auto;">
    <div style="text-align: center;margin-top: 2vh;font-size: 30px;">公证记录信息</div>
  <el-descriptions
  class="description-container"
    direction="horizontal"
    :column="1"
    :size="small"
    :style="{marginTop: index === 0 ? '3vh' : '5vh', marginLeft: '1vw',}"
    border
    v-for="(item, index) in items" :key="index"
  >
    <el-descriptions-item label="签名者ID">{{ item.signerID }}</el-descriptions-item>
    <el-descriptions-item label="公司名称">{{ item.companyName }}</el-descriptions-item>
    <el-descriptions-item label="事件描述">{{ item.crimeDescription }}</el-descriptions-item>
    <el-descriptions-item label="追溯时间">{{ formatTimestamp(item.timestamp) }}</el-descriptions-item>
    
  </el-descriptions>

</div>
</el-drawer>

    </div>
      <el-container>
        <el-header style="height: 16%;">
      <div style="width: 100%;height: 100%;display: flex;justify-content: start;align-items: center;margin-left: 1%;">
        <img src="@/img/登录人像.png" style="height: 55%;">
        <div class="name" >
          <div style="margin-left: 23%;font-size: 30px;font-weight: bold;">
            欢迎回来，
            <span style="color:rgb(71, 141, 78);">{{ LoginStore.username }}</span>
            . 您当前的身份是：
            <span style="color: rgb(71, 141, 78);">{{ LoginStore.role }}</span>
          </div>
        </div>
        
      </div>
        </el-header>
        <el-main>
          <div class="main">
          <el-card class="leftpage" style="width:26%;border: 3px solid black;">
          <div class="text1 " style="width: 100%;">
            <div class="progress">
            <el-progress type="circle" :percentage="var8" class="text-center" color="#3bbd48"/>
            <div class="text-center" style="margin-bottom:18px;margin-top: 10px;color: #fff;font-size: 18px;">文件上传进度</div>
            </div>
            <el-input
            style="max-width: 100%;margin: 0;padding: 0;font-size: 18px;"
            v-model="textarea"
            :rows="14"
            type="textarea"
            placeholder="欢迎来到文件上传界面，此处界面为详细信息..."
            />
          </div>
          <div class="text-center" style="margin-bottom:20px;margin-top: 30px;color: #fff;">相关信息输出</div>
          </el-card>
          
          <el-card class="middlepage" style="border: 3px solid black;">
          <div class="text item">
            <div style="margin-top: 25px; margin-bottom: -20px;font-size: 20px;">请上传聚合密钥：</div>
            <el-upload
            class="upload-demo"
            drag
            multiple
            style="width:100%; margin-top: 30px;"
            ref="kaggStr"
            :before-upload="beforeAvatarUpload1"
            >
            <el-icon 
            class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
            拖入文件或 <em>点击上传</em>
            </div>
            <template #tip>
              <!--
              <div class="el-upload__tip">
              文本文件不超过500kb
              </div>
              -->
              <div style="margin-top: 10px;">{{ filename1 }}</div>
            </template>
          </el-upload>
          <el-divider />
          <div style="margin-top: 10px; margin-bottom: -20px;font-size: 20px;">请上传私钥：</div>
          <el-upload
            class="upload-demo"
            drag
            multiple
            style="width:100%; margin-top: 30px;"
            ref="uskStr"
            :before-upload="beforeAvatarUpload2"
            @dragover="showDragoverTip"
            >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
            拖入文件或 <em>点击上传</em>
            </div>
            <template #tip>
              <!--
              <div class="el-upload__tip">
              文本文件不超过500kb
              </div>
              -->
              <div style="margin-top: 10px;">{{ filename2 }}</div>
            </template>
          </el-upload>
          <el-divider />
          <div class="btn" style="margin-top: 50px;">
            <!--<el-button type="primary" style="width: 250px;background-color: rgb(71, 141, 78);height: 50px;margin-right: 21%;font-size: 20px;border:1px solid rgb(71, 141, 78);" @click="handleUpload">公证</el-button>-->
            <el-button type="primary" :style="{ width: '250px', backgroundColor: ischeck ? 'rgba(71, 141, 78, 0.7)' : 'rgb(71, 141, 78)', height: '50px', marginRight: '21%', fontSize: '20px',border:'1px solid rgb(71, 141, 78)' }" @click="handleUpload" :disabled="ischeck">{{ textvalue }}</el-button>
            <!--<el-button type="primary" style="width: 250px;background-color: rgb(71, 141, 78);height: 50px;font-size: 20px;border: 1px solid rgb(71, 141, 78);" @click="getAccount1(result.tracerId, result.deTAPSsignId, result.notaryShare);">上传至区块链</el-button>-->
            <el-button type="primary" :style="{ width: '250px', backgroundColor: ischeck2 ? 'rgba(71, 141, 78, 0.7)' : 'rgb(71, 141, 78)', height: '50px', fontSize: '20px',border:'1px solid rgb(71, 141, 78)' }" @click="getAccount1(result.tracerId, result.deTAPSsignId, result.notaryShare);">上传至区块链</el-button>
          </div>
          </div>
          </el-card>
          <el-card class="rightpage" style="border: 3px solid black;">
            <div class="text item" style="display: flex;flex-direction: column;align-items: center;">
            <div class="text-center1" style="margin-top: 40px;color: #fff;margin-bottom: 10px;font-size: 20px;">解密份额加密后结果</div>
              <el-input
              style="max-width: 100%; height: 420px; margin-bottom: 40px;"
              v-model="textarea2"
              :rows="25"
              type="textarea"
              placeholder=""
              />

          </div>
          <div style="display: flex;justify-content: center;">
            <!--
            <el-button type="primary" style="width: 200px;height:40px; background-color:#EAEBF3;color: #131F94;" @click="getAccount1(result.tracerId, result.deTAPSsignId, result.notaryShare);">上传至区块链</el-button>
            -->
            <!--
            <el-button type="primary" style="width: 200px;height:40px; background-color:#EAEBF3;color: #131F94;" @click="submit()">上传至区块链123</el-button>
            -->
          </div>
          </el-card>
        </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.name{
  height: 100%;
    width: 80%;
    
    background-size: cover; 
  background-image: url('@/img/框.png');
  background-position: center top 50%; 
  display: flex;
  margin-left: -2%;
  justify-content: start;align-items: center;
  
}

@font-face{
  font-family: '方正字迹-惊鸿体';
  src: url('@/img/方正字迹-惊鸿体.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

:deep(.el-upload){
  height: 100%;
  line-height: 0px;
}
.el-upload :deep(.el-upload-dragger){
  height: 10px;
}

.el-aside {
  
}

.layout_container {
  height: 100%;
    width: 100%;
    background-size: cover; 
    display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.el-menu {
  position: relative;
  z-index: 2;
}
.layout_container.el-aside {
      background-color: #232323;
}
.layout_container.el-aside.el-menu {
        border-right: none;
}


.main {
  display: flex;
  flex-direction: row;
  justify-content: flex-start; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  margin-top: 0%;
  height: 92%; 
}

.progress {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  :deep(.el-progress__text){
           color: #fff;
  }
}
.el-steps :deep(.el-step__head.is-finish){
  color: #4D6DBB;
  border-color: #4D6DBB;;
}

.el-steps :deep(.el-step__title.is-finish){
  color: #4D6DBB;
  font-weight: bold;
}

.el-steps :deep(.el-step__description.is-finish){
  color: #4D6DBB;
}

.text {
  font-size: 14px;
}

.item {
    padding: 5px 0px;
}
.text1 {
  font-size: 14px;
}

.item1 {
  padding: 0px 0;
}
.leftpage {
  width:26%;
  height: 100%;
  margin-right: 22px;
  display: flex;
  flex-direction: column;
  justify-content:flex-end;
  background-color: rgb(71, 141, 78);
  border-radius: 10px;
}
.text-center {
  text-align: center;
}
.text-center1 {
  text-align: flex-start;
}
.middlepage {
  width:54%;
  height: 100%;
  margin-right: 16px;
  display: flex;
  flex-direction: column;
  justify-content:flex-start;
  border-radius: 10px;
  background-color: #F0F0F0;
}
.rightpage {
  width:26%;
  height: 100%;
  margin-left: 8px;
  display: flex;
  flex-direction: column;
  background-color: rgb(71, 141, 78);
  border-radius: 10px;
}
.pos2 {
  margin-bottom:10px;
  margin-top: 10px
}
.btn{
  margin-top: 0px;
  width: 100%; 
  display: flex;
  flex-direction: row;
  justify-content: center;
}
.description-container .el-descriptions-item:first-child {
  margin-top: 3vh;
}

.description-container .el-descriptions-item:not(:first-child) {
  margin-top: 5vh;
}

.el-button:hover {
  transform: scale(0.8);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  background-color: #3b4e8a; /* 更改背景颜色 */
}
</style>


