<script setup>
import { ElMessage } from 'element-plus'
import { ref,onMounted,computed } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { sk } from 'element-plus/es/locale/index.mjs';
import { useTokenStore,useLoginStore } from '@/store'
import CryptoJS from 'crypto-js';
import Web3 from "web3";

const TokenStore = useTokenStore();
const LoginStore = useLoginStore();

const startApp = (provider) => {
      if (provider !== window.ethereum) {
        console.error('Do you have multiple wallets installed?');
      }
};

const result = ref({
  combinerIndex:'',
  gid:'',
  share:'',
})

const getAccount = async () => {
      try {
        const provider = await detectEthereumProvider();
        if (provider) {
          startApp(provider);
          const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
          accounts.value = accounts[0];
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
const getAccount1 = async (combinerIndex, gid, share) => {
  ischeck2.value = true;
     // 检查是否安装了MetaMask
     if (typeof window.ethereum === 'undefined') {
            alert('请安装MetaMask!');
        } else {
          progress.value = 3;
          console.log(progress);
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
                const contractAddress = '0x6f08ceb8c1356f71c7e150d4b0e889c3c098f82b';
                const myContract = new web3.eth.Contract(contractABI, contractAddress);
                
                // 获取用户账户
                const accounts = await web3.eth.getAccounts();
                /*
console.log(combinerIndex);
console.log(gid);
console.log(share);
*/
//let combinerIndex=1;
                //let gid="8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
                //let share="BNW7VLslHJ5tOPiodvHSUkhBm4lMHzp+XpW3KwNWSn/SJa/DXVHHRL76C4zAOcMafkw3OfxsCquTRATmCYiT5P1ctKueuZv0039t5JIIL/de/m7l+ts2MBBlw6GKm1h0GUu1zX41oD5gxlsyDoYRhurH6AGT8IalY+JbCvvdY6fjs6JdaTGGADoTvCKFwAmi6EzXFcdICEBuFRPdUINrg5on5Gbl8Kjnvl9Pd4HvITxl+sJZg+97Kf+Wt3TsQmVXl5rGwakext/dM0+w33isQKYNsgy80ARJKQ2sLzVfrQkRhgbJW/ljLV6+grERje0aq+aFsQY3NijOaWU5N3zhemWKFRNDjxhHrDcFw7eIPDUzB/T2Q6FJr0V0cczcqU5qCPxTrQbHPqKoAuwzAdVQcqEOTCvrc60Fkx1eti5hqjHEfqma5OHsScAvjLn4UOUxVC3WfAlDBHplTfp+QWkgPaOEquX8+LnHK4y/tR2ilp3qN/3VlaEmdV6l97oixdzWkllRgcP6s3+B86S+5FKKlc96ETa8053qUGk34qgUSkkb26wbfNsi5XiIMuxbXXReCzYOPby4zORnL+aAbJ1R7wLr0aICxh9qYUGCanscarTdrNffGfoTKZ5icwPDS6hg99qbK9S3mMkrvQTBH07jWyHYUdoJVqxY9AFP7FgOBN21Yn2o/WrrF4Ukjkaw1IhR9GoxMlszcEaG1fMapaCwIVv9WmcHp0WyBMVse6z0HiyyHuOJLRgoKcQQW/3CjaZ0OvzeGPkzeGXFs1WNABuh02LDt5JQ333+bH3ULyBZl2FlOw1Fqp0SPyC4/OZ82vygvqav5nz9teT8nhcgCoNSFsCN9R/mUSlLJg==";
                //调用合约方法
                /*
                myContract.methods.submitSignShare(combinerIndex, gid, share).send({ from: accounts[0] })
                  .then(function(receipt){
                    console.log(receipt);
                  });
              }
              */

//调用合约方法
console.log(combinerIndex);
console.log(gid);
console.log(share);
myContract.methods.submitSignShare(combinerIndex,gid,share).send({ 
//myContract.methods.submitSignShare(1,"8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c22","BNW7VLslHJ5tOPiodvHSUkhBm4lMHzp+XpW3KwNWSn/SJa/DXVHHRL76C4zAOcMafkw3OfxsCquTRATmCYiT5P1ctKueuZv0039t5JIIL/de/m7l+ts2MBBlw6GKm1h0GUu1zX41oD5gxlsyDoYRhurH6AGT8IalY+JbCvvdY6fjs6JdaTGGADoTvCKFwAmi6EzXFcdICEBuFRPdUINrg5on5Gbl8Kjnvl9Pd4HvITxl+sJZg+97Kf+Wt3TsQmVXl5rGwakext/dM0+w33isQKYNsgy80ARJKQ2sLzVfrQkRhgbJW/ljLV6+grERje0aq+aFsQY3NijOaWU5N3zhemWKFRNDjxhHrDcFw7eIPDUzB/T2Q6FJr0V0cczcqU5qCPxTrQbHPqKoAuwzAdVQcqEOTCvrc60Fkx1eti5hqjHEfqma5OHsScAvjLn4UOUxVC3WfAlDBHplTfp+QWkgPaOEquX8+LnHK4y/tR2ilp3qN/3VlaEmdV6l97oixdzWkllRgcP6s3+B86S+5FKKlc96ETa8053qUGk34qgUSkkb26wbfNsi5XiIMuxbXXReCzYOPby4zORnL+aAbJ1R7wLr0aICxh9qYUGCanscarTdrNffGfoTKZ5icwPDS6hg99qbK9S3mMkrvQTBH07jWyHYUdoJVqxY9AFP7FgOBN21Yn2o/WrrF4Ukjkaw1IhR9GoxMlszcEaG1fMapaCwIVv9WmcHp0WyBMVse6z0HiyyHuOJLRgoKcQQW/3CjaZ0OvzeGPkzeGXFs1WNABuh02LDt5JQ333+bH3ULyBZl2FlOw1Fqp0SPyC4/OZ82vygvqav5nz9teT8nhcgCoNSFsCN9R/mUSlLJg==").send({ 
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

/*
              const gas = await myContract.methods.submitSignShare(combinerIndex, gid, share).estimateGas({ from: accounts[0] });
        const gasPrice = await web3.eth.getGasPrice();
        const maxFeePerGas = web3.utils.toBN(gasPrice); // 设置最大费用每单位燃气
        const maxPriorityFeePerGas = web3.utils.toBN(1 * 10 ** 9); // 设置基础费用每单位燃气

        const tx = {
            from: accounts[0],
            to: contractAddress,
            gas: gas,
            maxFeePerGas: maxFeePerGas,
            maxPriorityFeePerGas: maxPriorityFeePerGas,
            data: myContract.methods.submitSignShare(combinerIndex, gid, share).encodeABI()
        };

        // 发送交易
        const receipt = await web3.eth.sendTransaction(tx);
        console.log(receipt);
        */
      }
              //if((formData.Sk.length == 40) && (re.test(formData.Sk)) && (formData.gid.length == 64) && (re.test(formData.gid))){
      //const data = new FormData();
      //data.append('Sk', formData.value.Sk);
      //data.append('gid', formData.value.gid);
      //data.append('m','123456');//现在还是文件
      //data.append('pids','e17e31daabd3361fc2817a897b53d20bb552cc02733fbfd827b8386c8961afa0,7e8dc379b2eb50eb15bac5a261a8f7ee2c2aedbb15f2c753fef26871474cd9d7');//现在还是文件
    //}



const textarea = ref('')
const textarea2 = ref('')
const upload1Validated = ref(false);
const upload2Validated = ref(false);
const formData = ref({
    skstr: '',
    gid: '',
});


const fileContent1 = ref('');
/*
const fileContent2 = ref('');
const beforeAvatarUpload1 = async (file) => {
  const fileType = file.name.split('.').pop();
  if (fileType !== 'txt') {
    ElMessage.error('File must be a text file (.txt)!')
    upload1Validated.value = false
    return
  } else if (file.size / 1024  > 500) {
    ElMessage.error('Avatar picture size can not exceed 500kB!')
    upload1Validated.value = false
    return
  }
  upload1Validated.value = true;
  uploadRef1 = file;
  const base64String = await fileToBase64(file);
  fileContent1.value = base64String;
  console.log(fileContent1);
  return;
}

const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      resolve(reader.result);
    };
    reader.onerror = (error) => {
      reject(error);
    };
    reader.readAsDataURL(file);
  });
};

const beforeAvatarUpload2 = async (file) => {
  
  uploadRef2 = file;
  console.log(uploadRef2);
  const base64String = await fileToBase64(file);
  fileContent2.value = base64String;
  console.log(fileContent2);
  return;
}
*/
const fileInput1 = ref(null);
const base64String1 = ref('');

function handleFileUpload1(file) {
  //const file = fileInput1.value.files[0];
  console.log(file);
  if (!file) {
    alert("请先选择一个文件。");
    return;
  }

  const reader = new FileReader();
  reader.onload = (event) => {
    base64String1.value = event.target.result;
  };
  //reader.readAsDataURL(file);
  reader.readAsText(file);  
  console.log('123');
  console.log(base64String1);
}
function convertToBase641() {
  handleFileUpload1();
}

const fileInput2 = ref(null);
const base64String2 = ref('');

function handleFileUpload2(file) {
  //const file = fileInput2.value.files[0];
  if (!file) {
    alert("请先选择一个文件。");
    return;
  }

  const reader = new FileReader();
  reader.onload = (event) => {
    base64String2.value = event.target.result;
  };
  //reader.readAsDataURL(file);
  reader.readAsText(file);  
  console.log(base64String2);
}
function convertToBase642() {
  handleFileUpload2();
}

const publicKey = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwzJCWTWJ+9X2MKaQPmfBiocesGcvi7yCC9QPkEbgftlRMtpuEcBx0tCgBcURHnOlPowScYA7jkaHHrrZezgnAyPq5liPj5a992D8dW7VxoJrpt68JDuSuTQBlKBtsbjiuPNxT4NLOmnpZ8cFpLdst+luEtlbolClE/9S5JuJxJNAnwgR2mfqe+ZmVaqhXfa0XQON2E55Tiy3d7rO/lDY1OifKzI9xikTHrtYCauIo7wVWAlX5LCwrALmNFvZdIon5qqq3WgJs/YH9sijL20K2llZewFF478qfQ2JsaEZjweJMu4t+ETCe4PsaV9pEsVQfOZ9rQvTF8HjmHLHBtmffQIDAQAB
-----END PUBLIC KEY-----`;

const encryptData = (data) => {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey);
  return encryptor.encrypt(data);
};

const re = /^[a-zA-Z]+$/;
const animatedButton = ref(null);
const submitUpload = async () => {
  const button = animatedButton.value.$el;
if (button) {
  console.log('Button element:', button); // 打印按钮元素
  button.classList.add('animate');
  console.log('Class list after adding animate:', button.classList); // 打印类名列表
 // 动画结束后移除 class
 const handleAnimationEnd = () => {
          console.log('Animation ended');
          button.classList.remove('animate');
          console.log('Class list after removing animate:', button.classList); // 打印类名列表
          button.removeEventListener('animationend', handleAnimationEnd);
        };

        // 绑定动画结束事件
        button.addEventListener('animationend', handleAnimationEnd);
      } else {
        console.log('Button element not found');
      }
  //uploadRef1.value.submit();
  //uploadRef2.value.submit();
  //event.preventDefault();
  //if((formData.value.Sk.length == 40) && (re.test(formData.value.Sk)) && (formData.value.gid.length == 64) && (re.test(formData.value.gid))){
      const data = new FormData();
      data.append('skstr', skstr.value);
      data.append('gid', gid.value);

      //改变按钮文字
      textvalue.value = '签名中';
      ischeck.value = true;
      //data.append('m','123456');//现在还是文件
      //data.append('pids','e17e31daabd3361fc2817a897b53d20bb552cc02733fbfd827b8386c8961afa0,7e8dc379b2eb50eb15bac5a261a8f7ee2c2aedbb15f2c753fef26871474cd9d7');//现在还是文件
/*
      const encryptedm = encryptData(base64String1.value);
      const encryptedpids = encryptData(base64String2.value);

      data.append('m',encryptedm);
      data.append('pids',encryptedpids);
*/
      
      data.append('m',m.value);
      data.append('pids',pids.value);

      //console.log(fileContent1);
      //console.log(fileContent2);
      for (let [key, value] of data.entries()) {
        console.log(`${key}: ${value}`);
      }
/*
      let jsonData = {};
      data.forEach((value, key) => {
        jsonData[key] = value;
      });
      let jsonString = JSON.stringify(jsonData);
 */     
      /*axios.post('http://47.96.177.120:9090/signer/sign',data)
          .then(response =>{
            if(response.data.code == 0 && response.data.data){
              //getAccount1(response.data.CombinerIndex, response.data.gid, response.data.share);
              console.log(response.data.CombinerIndex);
              console.log(response.data.gid);
              console.log(response.data.share);
            }
          })
          .catch(error =>{
              console.log(error);
          });
    //}*/
    
    try {
      console.log(TokenStore.token)
        const response = await axios.post('http://47.96.177.120:8080/signer/sign', data,{
          headers: {
          //"Access-Control-Allow-Origin": "*", // 允许任何来源的跨域请求，生产环境中应更具体地指定来源
          "Authorization":TokenStore.token, // 将请求头的键设置为"token"，并将值设置为token的值
          'Content-Type': 'multipart/form-data'
          },
        });
        
        if (response.data.code == 0 && response.data.data) {
          progress.value = 2;
          console.log(progress);
          console.log(response.data.data);
            /*console.log(response.data.data.combinerIndex);
            console.log(response.data.data.gid);
            console.log(response.data.data.share);*/
            textarea2.value=response.data.data.share;
            result.value.combinerIndex=response.data.data.combinerIndex;
            result.value.gid=response.data.data.gid;
            result.value.share=response.data.data.share;
            //改变按钮文字
            textvalue.value = '点击签名';
            ischeck.value = false;
            //getAccount1(result.value.combinerIndex, result.value.gid, result.value.share);
        }
    } catch (error) {
        console.log(error);
    }
    /*
    try {
        const response = await axios.post('http://47.96.177.120:9090/signer/sign', data,);
        
        if (response.data.code == 0 && response.data.data) {
          console.log(response.data.data);
            console.log(response.data.data.combinerIndex);
            console.log(response.data.data.gid);
            console.log(response.data.data.share);
            getAccount1(response.data.data.combinerIndex, response.data.data.gid, response.data.data.share);
        }
    } catch (error) {
        console.log(error);
    }
    */
}
function handleFileChange1(file, fileList) {
  if (!file.raw) {
    alert("请先选择一个文件。");
    return;
  }

  const reader = new FileReader();
  reader.onload = (event) => {
    // 更新 base64String 或相关变量的值
    base64String1.value = event.target.result;
    console.log('File content:', base64String1.value);
  };
  reader.readAsText(file.raw); // 读取文件内容作为文本
}

function handleFileChange2(file, fileList) {
  if (!file.raw) {
    alert("请先选择一个文件。");
    return;
  }

  const reader = new FileReader();
  reader.onload = (event) => {
    // 更新 base64String 或相关变量的值
    base64String2.value = event.target.result;
    console.log('File content:', base64String2.value);
  };
  reader.readAsText(file.raw); // 读取文件内容作为文本
}


const filename1 = ref('');
const filename2 = ref('');
const filename3 = ref('');
const filename4 = ref('');
const skstr = ref('');
const gid = ref('');
const m = ref('');
const pids = ref('');
const var8 = ref(0);


const beforeAvatarUpload1 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename1.value = file.name;
  console.log(filename1.value);
  if(filename1.value && filename2.value && filename3.value && filename4.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 25;
    textarea.value += filename1.value + ' 上传成功！\n';
    console.log(textarea.value);
    console.log('123' + var8.value);
  }
  
console.log(progress.value);
 
  skstr.value = file;

  return;
}

const beforeAvatarUpload2 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename2.value = file.name;
  console.log(filename2.value);
  if(filename1.value && filename2.value && filename3.value && filename4.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 25;
    textarea.value += filename2.value + ' 上传成功！\n';
  }
console.log(progress.value);
 
gid.value = file;
  return;
}

const beforeAvatarUpload3 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename3.value = file.name;
  console.log(filename3.value);
  if(filename1.value && filename2.value && filename3.value && filename4.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 25;
    textarea.value += filename3.value + ' 上传成功！\n';
  }
console.log(progress.value);

m.value = file;

  return;
}

const progress = ref(0);
const beforeAvatarUpload4 = async (file) => {
  console.log(file);
  console.log(file.name);
  filename4.value = file.name;
  console.log(filename4.value);
  if(filename1.value && filename2.value && filename3.value && filename4.value){
    progress.value = 1;
  }
  if(file && var8.value < 100){
    var8.value += 25;
    textarea.value += filename4.value + ' 上传成功！\n';
  }
console.log(progress.value);
pids.value = file;
  return;
}

const textvalue = ref('点击签名');
const ischeck = ref(false);

</script>

<template>
  <div class="common-layout">
  <el-container>
    <div style="width: 180px;height: 100%;">
    <el-aside width="100vh" height="100vh" class="layout_container">
      <!--
      <div style="color: #fff;font-family: '方正字迹-惊鸿体',sans-serif;font-size: 45px;margin-left: 10px;margin-top: 50px;margin-bottom: 70px;">DeTAPS</div>-->
      <div style="display: flex;flex-direction: column;justify-content: center;align-items: center;">
      <img src="@\img\登录人像(白).png" style="width: 62px;height: 62px;margin-top: 20px;">
      <div style="margin-top: 10px; font-size: 35px;font-family: '方正字迹-惊鸿体',sans-serif;color: #fff">欢迎回来</div>
      <div style="color: #fff;margin-top: 6%;margin-bottom: 4%;font-size: 25px;">{{ LoginStore.username }}</div>
      </div>
      <el-menu
        active-text-color="#ffd04b"
        background-color="transparent"
        :default-active="$route.path"
        text-color="#fff"
        router
      >
      <el-menu-item style="display: flex;justify-content: center;align-items: center;height: 100%;" index="#">
        <span style="color: #fff;font-size: 28px;height: 100%;">签名</span>
      </el-menu-item>
      <el-menu-item style="display: flex;justify-content: center;align-items: center;height: 100%;" index="/blockchain">
        <span style="font-size: 28px;height: 100%;">区块链</span>
      </el-menu-item>
      </el-menu>
      <router-link to="#" style="display: flex;justify-content: center;align-items: center;"><img src="@/img/退出.png" style="height: 70px;width: 70px;margin-top: 550px;margin-left: 0px;margin-bottom: 20%;"></router-link>
    </el-aside>
  </div>
    <el-container>
      <el-header>
        <el-steps :active="progress" align-center style="margin-top: 2%;">
        <el-step title="Step 1" description="文件选取" />
        <el-step title="Step 2" description="签名" />
        <el-step title="Step 3" description="完成" />
      </el-steps>
      </el-header>
      <el-main>
        <div class="main">
        <el-card class="leftpage" style="width:26%">
        <div class="text1 " style="width: 100%;">
          <div class="progress" style="margin-bottom: 6%;">
          <el-progress type="circle" :percentage="var8" class="text-center"/>
          <div class="text-center" style="margin-bottom:18px;margin-top: 18px;color: #fff;font-size: 20px;">文件上传进度</div>
          </div>
          <el-input
          style="max-width: 100%;margin: 0;padding: 0;font-size: 18px;"
          v-model="textarea"
          :rows="16"
          type="textarea"
          placeholder="欢迎来到文件上传界面，此处界面为详细信息..."
          />
        </div>
        <div class="text-center" style="margin-bottom:20px;margin-top: 30px;color: #fff;">相关信息输出</div>
        </el-card>
        
      <el-card class="middlepage" >
        <div class="text item" style="max-height: 680px; overflow: auto;max-width: 100%;">
          <form>
            <div style="margin-top: 40px; margin-bottom: 10px;font-size: 20px;">请上传私钥：</div>
            <!--
            <el-input v-model="formData.skstr" style="width: 100%;height: 50px" placeholder="请上传私钥" />
            -->
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
            <div style="margin-top: 10px; margin-bottom: 10px;font-size: 20px;">请上传gid：</div>
            <!--
            <el-input v-model="formData.gid" style="width: 100%;height: 50px" placeholder="请上传gid" />
            -->
            <el-upload
          class="upload-demo"
          drag
          multiple
          style="width:100%; margin-top: 30px;"
          ref="kaggStr"
          :before-upload="beforeAvatarUpload2"
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
            <div style="margin-top: 10px;">{{ filename2 }}</div>
          </template>
        </el-upload>
            <el-divider />
            <!--<div style="display: flex; justify-content: center;">-->
              <div style="margin-top: 20px; margin-bottom: 10px;font-size: 20px;">消息m文件：</div>
            <el-upload
          class="upload-demo"
          drag
          multiple
          style="width:100%; margin-top: 30px;"
          ref="kaggStr"
          :before-upload="beforeAvatarUpload3"
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
            <div style="margin-top: 10px;">{{ filename3 }}</div>
          </template>
        </el-upload>
        <el-divider />
        <div style="margin-top: 20px; margin-bottom: 10px;font-size: 20px;">pids文件：</div>
        <el-upload
          class="upload-demo"
          drag
          multiple
          style="width:100%; margin-top: 30px;"
          ref="kaggStr"
          :before-upload="beforeAvatarUpload4"
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
            <div style="margin-top: 10px;">{{ filename4 }}</div>
          </template>
        </el-upload>
       
        <!--
            <el-upload
              ref="uploadRef1"
              class="upload-demo"
              :auto-upload="false"
              style="margin-right:80px"
              @change="handleFileChange1"
            >
            <template #trigger>
            <el-button type="primary" style="width: 250px;height:50px"  >选择信息m文件</el-button>
            </template>
            </el-upload>
            
            <el-upload
              ref="uploadRef2"
              class="upload-demo"
              :auto-upload="false"
              type="file"
              @change="handleFileChange2"
            >
            <template #trigger>
            <el-button type="primary" style="width: 250px;height:50px" >选择pids文件</el-button>
            </template>
            </el-upload>
            -->
            <!--
            <input type="file" ref="fileInput1" @change="handleFileUpload1">-->
            <!--<button @click="convertToBase641">转换为Base64</button>-->
            <!--<div id="output">{{ base64String1 }}</div>-->
            <!--
            <input type="file" ref="fileInput2" @change="handleFileUpload2">-->
            <!--<button @click="convertToBase642">转换为Base64</button>-->
            <!--<div id="output">{{ base64String }}</div>-->
          
          <!--</div>-->
          <el-divider />
        <div class="btn">
        <!--<el-button type="primary" style="width: 250px;background-color: #4963A3;height: 50px;margin-right: 21%;font-size: 20px;" @click="submitUpload" >点击签名</el-button>-->
        <el-button type="primary" :style="{ width:'250px', backgroundColor: ischeck ? 'rgba(73, 99, 163, 0.72)' : '#4963A3', height: '50px', marginRight: '21%', fontSize: '20px',transition: 'transform 0.3s ease' }" @click="submitUpload" :disabled="ischeck" ref="animatedButton">{{textvalue}}</el-button>
        <!--<el-button type="primary" style="width: 250px;background-color: #4963A3;height: 50px;font-size: 20px;" @click="getAccount1(result.combinerIndex, result.gid, result.share);" >上传区块链</el-button>-->
        <el-button type="primary" :style="{ width:'250px', backgroundColor: ischeck2 ? 'rgba(73, 99, 163, 0.72)' : '#4963A3', height: '50px', fontSize: '20px',transition: 'transform 0.3s ease' }" @click="getAccount1(result.combinerIndex, result.gid, result.share);" >上传区块链</el-button>
      </div>
        </form>
        </div>
      </el-card>

        <el-card class="rightpage">
        <div class="text item" style="width: 100%;">
            <div class="text-center pos2" style="color: #fff;margin-bottom:30px;font-size: 20px;">加密文件Base64签名信息</div>
             <el-input
            style="max-width: 100%; height: 540px; margin-bottom: 10px;padding: 0;"
            v-model="textarea2"
            :rows="29"
            type="textarea"
            placeholder=""
            />
        </div>
        </el-card>
      </div>
      </el-main>
    </el-container>
  </el-container>
</div>
</template>

<style scoped>
* {
 box-sizing: border-box;
}

@font-face{
font-family: '方正字迹-惊鸿体';
src: url('@/img/方正字迹-惊鸿体.ttf') format('truetype');
font-weight: normal;
font-style: normal;
}

.layout_container {
  height: 100%;
  width: 100%;
  background-image: url('@/img/蓝1.jpg');
  background-size: cover;
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

.el-aside {
background-image: url('@/img/蓝1.jpg');
}

.main {
display: flex;
flex-direction: row;
justify-content: flex-start; /* 水平居中 */
align-items: center; /* 垂直居中 */
margin-top: 4%;
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

.el-steps :deep(.el-step__title){
  font-size: 25px;
}
.el-steps :deep(.el-step__description) {
  margin-top: 1%;
  font-size: 18px; /* 调整为你需要的大小 */
}

.text {
font-size: 14px;
}

.item {
padding: 5px 0;
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
background-color: #4D6DBB;
border-radius: 10px;
}
.text-center {
text-align: center;
}
.middlepage {
width:54%;
height: 100%;
margin-right: 16px;
display: flex;
flex-direction: column;
justify-content:flex-start;
background-color: #F0F0F0;
border-radius: 10px;
}
.rightpage {
width:26%;
height: 100%;
margin-left: 8px;
display: flex;
flex-direction: column;
border-radius: 10px;
background-color: #4D6DBB;
}
.pos2 {
margin-bottom:10px;
margin-top: 10px
}
.btn{
margin-top: 40px;
width: 100%; 
display: flex;
flex-direction: row;
justify-content: center;
}

.el-button:hover {
  transform: scale(0.8);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  background-color: #3b4e8a; /* 更改背景颜色 */
}

</style>