<script setup>
import { ref, onMounted, watch, getCurrentInstance} from 'vue'
import { Search,User } from '@element-plus/icons-vue'
import { useTokenStore,usePsingStore } from '@/store'
import { onUnmounted } from 'vue'
import axios from 'axios';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const PSingStore = usePsingStore();
const txcount = ref(0);
// 定义请求数据的函数
const fetchData1 = async () => {
    try {
        const response = await axios.get('http://47.96.177.120:8080/block/blockInfo');
        const data = response.data;
        if (Array.isArray(data)) {
            data.forEach((obj,index) => {
                //console.log(obj);
                //console.log(index+1);
                if (value1.value[index]) { // 检查本地数组是否有相应的索引
                    value1.value[index].value_append = obj.blockNumber;
                    value1.value[index].value_prepend = shortenString(obj.blockHash.toString(),20);
                    //value1.value[index].timestamp = formatTimestamp(obj.timestamp.toString());
                    value1.value[index].timestamp = formatDate(obj.timestamp.toString());
                }
            });
            //console.log(value1.value[0].value_append);
        } else {
            console.error('Received data is not an array');
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

const fetchData2 = () => {
    fetch('http://47.96.177.120:8080/block/txInfo')
        .then(response => response.json())
        .then(data => {
            if (Array.isArray(data)) {
                data.forEach((obj,index) => {
                    // 处理每个对象
                    console.log(obj);
                    if (value2.value[index]) { // 检查本地数组是否有相应的索引
                    value2.value[index].value_append = shortenString1(obj.hash.toString(),30);
                    value2.value[index].createdAt = formatTimestamp(obj.createdAt.toString());
                    value2.value[index].value_from = shortenString(obj.from.toString(),16);
                    value2.value[index].value_to = shortenString(obj.to.toString(),16);
                    value2.value[index].value_prepend1 = obj.hash.toString();
                }
                });
            } else {
                console.error('Received data is not an array');
            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
        fetch('http://47.96.177.120:8080/block/txCount')
          .then(response => response.json())
          .then(data => {
            //console.log(data);
            txcount.value = data;
          });
};

function formatDate(dateString) {
  const date = new Date(dateString);

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const formatTimestamp = (timestamp) => {
      const date = new Date(timestamp);  // 将时间戳字符串转换为Date对象
      const currentTime = new Date();  // 获取当前时间
      const difference = currentTime - date;  // 计算时间差，单位为毫秒
      return Math.floor(difference / 1000);
      /*
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份需要加1，同时补0处理
      const day = date.getDate().toString().padStart(2, '0');  // 天数补0处理
      const hours = date.getHours().toString().padStart(2, '0');  // 小时补0处理
      const minutes = date.getMinutes().toString().padStart(2, '0');  // 分钟补0处理
      const seconds = date.getSeconds().toString().padStart(2, '0');  // 秒数补0处理
      const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;  // 格式化输出年月日时分秒
      return formattedDate;
      */
}

function shortenString(str, maxLength) {
    if (str.length <= maxLength) {
        return str;
    } else {
        const ellipsisLength = 3; // 省略号的长度
        const contentLength = maxLength - ellipsisLength;

        // 分配左右两边各一半的长度，如果有余数，则多分配一个字符给左边
        const leftLength = Math.ceil(contentLength / 2);
        const rightLength = Math.floor(contentLength / 2);

        return str.slice(0, leftLength) + '......' + str.slice(-rightLength);
    }
}

function shortenString1(str, maxLength) {
    if (str.length <= maxLength) {
        return str;
    } else {
        const ellipsisLength = 3; // 省略号的长度
        const contentLength = maxLength - ellipsisLength;

        // 分配左右两边各一半的长度，如果有余数，则多分配一个字符给左边
        const leftLength = Math.ceil(contentLength / 2);
        //const rightLength = Math.floor(contentLength / 2);

        return str.slice(0, leftLength) + '......' ;
    }
}
/* onMounted(() => {
    // 初始加载数据
    fetchData1();
    fetchData2();

    // 每隔5秒发送一次GET请求
    const interval1 = setInterval(fetchData1, 5000);
    const interval2 = setInterval(fetchData2, 5000);

    // 在组件销毁时清除定时器
    onUnmounted(() => clearInterval(interval1));
    onUnmounted(() => clearInterval(interval2));
}); */

const input2 = ref('')
const TokenStore = useTokenStore();
const value1 = ref([
  {id:1, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'},
  {id:2, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'},
  {id:3, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'},
  {id:4, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'},
  {id:5, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'},
  {id:6, value_append:'0', timestamp:'0', value_prepend:'1', value_prepend1:'1'}
])

const value2 = ref([
  {id:1, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'},
  {id:2, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'},
  {id:3, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'},
  {id:4, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'},
  {id:5, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'},
  {id:6, value_append:'0xa0d4dc9e38be6a8e2', createdAt:'0', value_from:'0x40A941...fe6420Cd', value_to:'0xA00759...93d3cafe', value_prepend1:'1'}
])

/* fetchData1();
fetchData2();
console.log(123);
console.log(value1.value[0].value_append); */

const lastItemValueAppend = computed(() => {
      const lastItem = value1.value[0];
      return lastItem ? lastItem.value_append : null;
    });

const handleClick = async (item) => {
  /* console.log("Clicked item ID:", item.id);
  console.log(item);
  console.log(item.value_prepend1); */
  try {
    const response = await axios.get(`http://47.96.177.120:8080/block/findTxByHash?hash=${item.value_prepend1}`);
    /* console.log(response.data);
    console.log(response.data.hash); */
    const data = {
            blockhash: response.data.hash,
            blocknumber: response.data.blockNumber,
            GasLimit: response.data.gasLimit,
            GasPrice: response.data.gasPrice,
            from: response.data.from,
            nonce: response.data.nonce,
            status: response.data.status,
            to: response.data.to,
            inputData: response.data.inputData,
            current_time: response.data.createdAt
        }
    PSingStore.setData(data);
    //console.log(PSingStore.blockinf);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};


const inputValue = ref('');

const searchData = async () => {
  if (!inputValue.value) {
    // 如果输入为空，则提醒用户并返回
    console.error("Please enter search query."); // 理论上应使用弹窗提醒，此处仅为演示
    return;
  }
  try {
    const response = await axios.get(`http://47.96.177.120:8080/block/findTxByHash?hash=${inputValue.value}`);
    console.log(response.data);
    console.log(response.data.hash);
    const data = {
            blockhash: response.data.hash,
            blocknumber: response.data.blockNumber,
            GasLimit: response.data.gasLimit,
            GasPrice: response.data.gasPrice,
            from: response.data.from,
            nonce: response.data.nonce,
            status: response.data.status,
            to: response.data.to,
            inputData: response.data.inputData,
            current_time: response.data.createdAt
        }
    PSingStore.setData(data);
    console.log(PSingStore.blockinf);
    router.push(`/blockchain/${inputValue.value}`);
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};


const elements= ref([
        {id:1, title: "交易数量", value: 268500, img: "@/img/交易数量.png" },
        {id:2, title: "中等燃料价格", value: 268500, img: "@/img/中等燃气价格.png" },
        {id:3, title: "追溯者数量", value: 5, img: "@/img/追溯者数量.png" }
]);

const getSpan = (index) => {
      return index === elements.value.length - 1 ? 0 : 1;
    }

import backgroundUrl from '@/img/蓝背景.jpg';
onMounted(() => {
  document.body.style.background = `url(${backgroundUrl}) no-repeat`;
  document.body.style.backgroundPosition = '50% 5.85%'; // 将背景图片水平居中，垂直偏移 7.5%
  document.body.style.backgroundSize = '100% 20%'; // 设置背景图片大小
}); 

const animated = ref(true);
/* setTimeout(() => {
    animated.value = false;
    }, 1800);  */
 watch(value1,(newValue) => {
  if (newValue) {
    animated.value = true;
    setTimeout(() => {
    animated.value = false;
    }, 100); 
  }else{
    console.log('error');
  }
}, { deep: true });

const blockNumber1 = ref(null);
const blockHash = ref(null);
const timestamps = ref(null);
let intervalId = null;

    const fetchLatestBlock = async (web3) => {
      try {
    // 获取最新的区块号
    const latestBlockNumber = await web3.eth.getBlockNumber();
    //console.log('Latest Block Number:', Number(latestBlockNumber));

    // 获取最新区块的详细信息，`true` 表示获取区块内的交易详细信息
    const latestBlock = await web3.eth.getBlock(latestBlockNumber, true);
    //console.log('Latest Block Details:', latestBlock);

    // 提取并打印有关区块的详细信息
    const {
      number,
      hash,
      parentHash,
      nonce,
      sha3Uncles,
      logsBloom,
      transactionsRoot,
      stateRoot,
      receiptsRoot,
      miner,
      difficulty,
      totalDifficulty,
      extraData,
      gasLimit,
      gasUsed,
      timestamp,
      transactions
    } = latestBlock;
    const timestampNumber = Number(timestamp);
    const blockDate = new Date(timestampNumber * 1000);
    const year = blockDate.getUTCFullYear();
    const month = String(blockDate.getUTCMonth() + 1).padStart(2, '0'); // 月份从 0 开始，所以需要加 1
    const day = String(blockDate.getUTCDate()).padStart(2, '0');
    const hours = String(blockDate.getUTCHours()).padStart(2, '0');
    const minutes = String(blockDate.getUTCMinutes()).padStart(2, '0');
    const seconds = String(blockDate.getUTCSeconds()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    //console.log(`Block was mined on: ${formattedDate}`);

    // 更新 blockNumber 以便在 UI 中显示最新的区块号
    blockNumber1.value = Number(number).toString(); 
    //console.log(blockNumber1.value);
    blockHash.value = parentHash;
    timestamps.value = formattedDate;
  } catch (error) {
    console.error('Error fetching latest block:', error);
  }
    };

    onMounted(async () => {

      const { appContext } = getCurrentInstance();
      const web3 = appContext.config.globalProperties.$web3;

      // 检查 Web3 实例是否正确获取
      console.log('Web3 instance from global properties:', web3);

      if (!web3) {
        console.error('Web3 instance is not available');
        return;
      }
      await fetchData1(); // 等待 fetchData1 完成
      console.log(123);
      console.log(value1.value[0].value_append); // 确保数据已更新
      // 立即获取一次最新的区块号
      /* await fetchLatestBlock(web3);
      //console.log(123);
      //console.log(blockNumber1.value);
      if(blockNumber1!= value1.value[0].value_append){
        //console.log('123');
        //value1.value.pop();
        value1.value.unshift(value1.value.pop());
        //console.log(blockNumber1.value.toString());
        value1.value[0].value_append = Number(value1.value[0].value_append)+1;
        //console.log(shortenString(blockHash.value.toString(),20));
        value1.value[0].value_prepend = shortenString(blockHash.value.toString(),20);
        //console.log(timestamps.value);
        value1.value[0].timestamp = timestamps.value;
        value1.value.slice(6);
      } */
      // 设置一个定时器每隔15秒获取最新的区块号
      intervalId = setInterval(async () => {
        await fetchLatestBlock(web3);
        //if(blockNumber1!= value1.value[0].value_append){
        //console.log('123');
        //value1.value.pop();
        value1.value.unshift(value1.value.pop());
        //console.log(blockNumber1.value.toString());
        //value1.value[0].value_append = blockNumber1.value.toString();
        console.log(value1.value[0].value_append);
        value1.value[0].value_append = value1.value[0].value_append + 6;
        console.log(value1.value[0].value_append);
        //console.log(shortenString(blockHash.value.toString(),20));
        value1.value[0].value_prepend = shortenString(blockHash.value.toString(),20);
        //console.log(timestamps.value);
        value1.value[0].timestamp = timestamps.value;
        value1.value.slice(6);
      //}
      }, 5000); // 5秒
    });

function shiftList() {
  // 移除最后一个元素
  value1.value.pop();
  // 在前面插入一个默认值（根据需求可以调整）
  value1.value.unshift(value1.value.pop());

}



</script>

<template>
  <div class="common-layout">
    <el-container>
    
      <el-header> 
        <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        @select="handleSelect"
        router
        >
        <img src="@\img\DeTAPS旁边的logo.png" style="width: 35px;height: 35px; margin-top: 10px;margin-bottom: 15px;" alt="My Logo">
        <div style="color:rgba(4, 26, 159) ;font-family: '方正字迹-惊鸿体',sans-serif;font-size: 40px; font-weight:bold ; display: flex; align-items: center; justify-content: center;margin-left: 18px;">DeTAPS</div>
        <div class="flex-grow" />
        <el-menu-item style="font-family: '兰亭粗黑',sans-serif;font-size: 20px;" index="/signature">首页</el-menu-item>
        <el-menu-item style="font-family: '兰亭粗黑',sans-serif;font-size: 20px;" index="/blockchain">区块链</el-menu-item>
        <el-menu-item style="font-family: '兰亭粗黑',sans-serif;font-size: 20px;" index="2">更多</el-menu-item>
        <el-divider style="margin-top: 22px;" direction="vertical" />
        <el-menu-item style="margin-right: 100px;font-family: '兰亭粗黑',sans-serif;font-size: 20px;" index="/login"><img src="@\img\登录人像.png" style="width: 28px;height: 28px;margin-right: 15px;" alt="My Logo">
          登录</el-menu-item>
        </el-menu>
      </el-header>




      <el-main class="main1">
      <div class="container1">
      <span style="color: white;font-family: '方正字迹-惊鸿体',sans-serif; font-size: 40px; font-weight: bold;">The DeTAPS Blockchain Explorer</span>
      <!--
      <div class="search">
        <el-input v-model="input" placeholder="Transaction Hash" class="el-input--small" />
        <el-button type="primary" :icon="Search" class="el-button--small"/>
      </div>
       -->
       <!--
      <el-input v-model="input2" class="w-50 m-2 input1" placeholder="Transaction Hash" :suffix-icon="Search"/>
       -->
      <div class="input-div">
        <el-input v-model="inputValue" class="input-class" size="large" placeholder="Search by Txn Hash/Block" @keyup.enter="searchData">
            <template #prepend>
                <el-button :icon="Search" @click="searchData"/>
            </template>
            <template #append><span style="cursor: pointer;" @click="searchData">搜索</span></template>
        </el-input>
    </div>

     </div>
        <el-card class="box-card1">
            <div>
                <div class="card1" >
                  <!--
                  <el-row>
                    <div v-for="(item, index) in elements" :key="index">
                    <el-col :span="getSpan(index)">
                    <div style="display: flex; margin-bottom: 10px;">
                    <img :src="item.img" style="width: 32px; height: 32px; margin-right: 12px;">
                    <el-statistic :title="item.title" :value="item.value" />
                    </div>
                    </el-col>
                    <div v-if="index < elements.length - 1" class="Border_l"></div>
                    </div>
                  </el-row>
                -->
                <!--
                <el-row >
                    <el-col :span="6" style="margin-left: 13%;display:flex;">
                      <div style="display: flex; margin-bottom: 10px;">
                        <img src="@\img\交易数量.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title="交易数量" :value="268500" />
                      </div>
                      <div class="Border_l" style="margin-right: 30%;margin-left: 60%;"></div>
                    </el-col>
                    
                    <el-col :span="6" style="margin-left: 6%;display:flex;">
                      <div style="display: flex; margin-bottom: 10px;">
                        <img src="@\img\区块数量.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title=" 区块数量  " :value="lastItemValueAppend.toString()" />
                        
                      </div>
                      <div class="Border_l" style="margin-right: 30%;margin-left: 10%;"></div>
                    </el-col>
                    
                    <el-col :span="6" style="margin-left: 6%;">
                      <div style="display: flex; margin-bottom: 10px;">
                      <img src="@\img\追溯者数量.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title="追溯者数量" :value="5" style="margin-right: 40%;" />
                      </div>
                    </el-col>
                  </el-row>

                  <div class="Border_2"></div>
                  
                  <el-row >
                    <el-col :span="6" style="margin-left: 13%;">
                      <div style="display: flex; margin-top: 10px;">
                        <img src="@\img\中等燃气价格.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title="中等燃料价格" :value="268500" style="margin-right: 40%;" />
                        <div class="Border_l" style="margin-right: 30%;"></div>
                      
                      </div>
                    </el-col>
                    
                    <el-col :span="6" style="margin-left: 6%;">
                      <div style="display: flex; margin-top: 10px;">
                      <img src="@\img\挖矿难度.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title=" 挖矿难度  " :value="10" style="margin-right: 40%;"/>
                        <div class="Border_l" style="margin-right: 30%;"></div>
                      </div>
                    </el-col>
                    
                    <el-col :span="6" style="margin-left: 6%;">
                      <div style="display: flex; margin-top: 10px;">
                      <img src="@\img\合并者数量.png" style="width: 32px;height: 32px;margin-right: 3%;">
                        <el-statistic title="合并者数量" :value="5" style="margin-right: 40%;"/>
                      </div>
                    </el-col>
                  </el-row>
                -->
                <el-row type="flex" justify="center">
                    <el-col :span="5" style="margin-left: 10%;">
                      <div style="display: flex; margin-bottom: 10px;">
                        <img src="@\img\交易数量.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="交易数量" :value="txcount" />
                      </div>
                    </el-col>
                    <div class="Border_l" style="margin-right: 12%;"></div>
                    <el-col :span="5">
                      <div style="display: flex; margin-bottom: 10px;">
                      <img src="@\img\中等燃气价格.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="中等燃料价格" :value="268500" />
                      </div>
                    </el-col>
                    <div class="Border_l" style="margin-right: 12%;"></div>
                    <el-col :span="5">
                      <div style="display: flex; margin-bottom: 10px;">
                      <img src="@\img\追溯者数量.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="追溯者数量" :value="5" />
                      </div>
                    </el-col>
                  </el-row>
                  <div class="Border_2"></div>
                  <el-row type="flex" justify="center">
                    <el-col :span="5" style="margin-left: 10%;">
                      <div style="display: flex; margin-top: 10px;">
                      <img src="@\img\区块数量.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="区块数量" :value="lastItemValueAppend" />
                      </div>
                    </el-col>
                    <div class="Border_l" style="margin-right: 12%;"></div>
                    <el-col :span="5">
                      <div style="display: flex; margin-top: 10px;">
                      <img src="@\img\挖矿难度.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="挖矿难度" :value="10" />
                      </div>
                    </el-col>
                    <div class="Border_l" style="margin-right: 12%;"></div>
                    <el-col :span="5">
                      <div style="display: flex; margin-top: 10px;">
                      <img src="@\img\合并者数量.png" style="width: 32px;height: 32px;margin-right: 12px;">
                        <el-statistic title="合并者数量" :value="5" />
                      </div>
                    </el-col>
                  </el-row>
                </div>
            </div>
        </el-card>

        <div class="card2" style="margin-bottom: 80px;">


            <el-card class="box-card2">
            <template #header>
            <div class="card-header">
            <span style="font-size: 24px; font-weight: bold;">Latest Blocks</span>
            </div>
            </template>


            <div>

<!-- animation: index === 0 && animated , scaleDown: (index === 0 && !animated) || (index === 5 && !animated), animation2: index === 5 && animated, move:animated-->
            <div v-for="(item,index) in value1" :key="item.id" :class="{animation2: index === 0 && animated , scaleDown: (index === 0 && !animated)}">
                <div style="display: flex;height: 5vh;margin-bottom: 3%;">
                  <img src="@\img\最近区块.png" style="width: 32px;height: 32px;margin-right: 10px; margin-top: 5px;">
                <div class="my-card">
                  <router-link :to="`/blockchain/block/${item.value_append}`" style="font-size: 15px;text-decoration: none;color: #0784c3; " @click.native="handleClick(item)">{{ item.value_append }}</router-link>
                  <div class="my-footer" style="color: #606266;">
                    <!--<span style="margin-right: 5px;">in {{ item.timestamp }} secs ago</span>-->
                    <span style="margin-right: 5px;">{{ item.timestamp }} </span>
                  </div>
                </div>

                <div class="my-card" style="margin-left: 120px;">
                  <!--<span style="font-size: 15px;">Fee Recipient </span>-->
                  <div style="display: flex;">
                    <span style="font-size: 15px;">Hash:</span>
                    <router-link :to="`/blockchain/block/${item.value_append}`" style="font-size: 15px;text-decoration: none;color: #0784c3;font-family: Arial, sans-serif;display: inline-block; width: 200px; white-space: nowrap;  text-align: center; " @click.native="handleClick(item)" >{{ item.value_prepend }}</router-link>
                  </div>
                  <div class="my-footer" style="color: #606266;">
                    <!--
                    <router-link to="/blockchain/1" style="margin-right: 5px;text-decoration: none;">110  txns</router-link>
                    <span>in 12 secs</span>-->
                  </div>
                  
                </div>
                <el-tag type="info" style="margin-left: 10%;">0 ETH</el-tag>
            </div>
            <el-divider />
          </div>

        </div>
            </el-card>



            <el-card class="box-card3">
            <template #header>
            <div class="card-header">
            <span style="font-size: 24px; font-weight: bold;">Latest Transactions</span>
            </div>
            </template>
            <div>


                <div v-for="(item,index) in value2" :key="item.id">
            <div style="display: flex; height: 5vh;">
              <img src="@\img\最近交易.png" style="width: 40px;height: 40px;margin-right: 10px; margin-top: 3px;">
            <div class="my-card">
                  <router-link :to="`/blockchain/${item.value_prepend1}`" style="font-size: 15px;text-decoration: none;color: #0784c3;width: 170px; " @click.native="handleClick(item)">{{ item.value_append }}</router-link>
                  <div class="my-footer" style="color: #606266;">
                    
                    <span style="margin-right: 5px;">in {{ item.createdAt }} secs ago</span>
                    <!--
                    <span>secs ago</span>
                    -->
                  </div>
                </div>

            <div class="my-card" style="margin-left: 10%;">
              <div class="display:flex">
                  <span style="font-size: 15px;">From </span>
                  <router-link :to="`/blockchain/${item.value_prepend1}`"  style="font-size: 15px;text-decoration: none;color: #0784c3; display: inline;" @click.native="handleClick(item)">{{ item.value_from }}</router-link>
                </div>
                  <div class="my-footer">
                    <div class="display:flex">
                    <span style="font-size: 15px;">To </span>
                    <router-link :to="`/blockchain/${item.value_prepend1}`" style="margin-right: 5px;font-size: 15px;text-decoration: none;color: #0784c3;" @click.native="handleClick(item)">{{ item.value_to }}</router-link>
                  </div>
                  </div>
                </div>
                <el-tag type="info" style="margin-left: 10%;">0 ETH</el-tag>
            </div>

            <el-divider />


          </div>
        </div>



            


            </el-card>


        </div>   
      </el-main>




      <el-footer>
         <!--底部区-->
         
         <div class="footer">
      <!--
        <p>
          <a href="#">慕课简介</a>
          <i>|</i>
          <a href="#">慕课公告</a>
          <i>|</i>
          <a href="#">招纳贤士</a>
          <i>|</i>
          <a href="#">联系我们</a>
          <i>|</i>
          <a href="#">客服热线：400-675-1234</a>
        </p>
        <p>
          Copyright © 2006 - 2014 慕课版权所有&nbsp;&nbsp;&nbsp;京ICP备09037834号 &nbsp;&nbsp;&nbsp;某市公安局龙潭分局备案编号：32432421231
        </p>
        -->
      </div>
   
      </el-footer>
    </el-container>
  </div>
</template>

<style scoped>
.animation {
  transition: transform 0.9s ease;
  transform: scale(1.3);
}
.animation2{
  transition: transform 0.9s ease,opacity 0.9s ease;
  transform: scale(0);
  
}
.scaleDown{
  transition: transform 2.6s ease;
  transform: scale(1);
  opacity: 1;
}
.move {
  transition: transform 1s ease;
  transform: translate(0, 90px); 
}
.el-statistic :deep(.el-statistic__head){
  font-size: 18px;
  color: #000;
}
@font-face{
  font-family: '方正字迹-惊鸿体';
  src: url('@/img/方正字迹-惊鸿体.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

@font-face{
  font-family: '兰亭粗黑';
  src: url('@/img/兰亭粗黑.TTF') format('truetype');
  font-weight: normal;
  font-style: normal;
}

body{
    background: url("@/img/蓝背景.jpg") no-repeat;
    background-position: 50% 7.5%; /* 将背景图片水平居中，垂直偏移30% */
    background-size: 100% 20%
}

.flex-grow {
  flex-grow: 1;
}

.main1 {

  display: flex;
  flex-direction: column;
  align-self: center;
}

.input1 {
  border-radius: 95px;
  width: 750px; 
  height: 45px;
  margin-top: 15px;

}


.input-div {
    width: 750px;
    height:45px;
    border-radius: 95px;
    background-color: #fff;
    margin-top: 1%;
}
.input-class ::v-deep .el-input-group__prepend {
    margin-top: 3px;
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}

.input-class ::v-deep .el-input__wrapper {
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}

.input-class ::v-deep .el-input-group__append {
    margin-top: 3px;
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}
 /*
.input-class {
 
 .el-input-group__prepend {
     margin-top: 3px;
     border-radius: 95px;
     border: 0;
     box-shadow: 0 0 0 0px;
 }

 .el-input__wrapper {
     
     border-radius: 95px;
     border: 0;
     box-shadow: 0 0 0 0px;
 }

 .el-input-group__append {
     margin-top: 3px;
     border-radius: 95px;
     border: 0;
     box-shadow: 0 0 0 0px;
 }
}
*/

.box-card1 {
  width: 1430px;
  height: 150px;
  margin-top: 50px;
  border-radius: 20px;
}

.container1 {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 30px;
}

.search {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  margin-top: 30px;
  width: 553px;
  height: 48px;
  border:1px solid #000;
  border-radius: 5px;
}

.el-input--small {
  width: 500px; 
  height: 38px; 
  margin: 2px;
}

.el-button--small {
  margin-left: 5px;
  height: 38px;
}

.box-card2 {
  width: 700px;
  border-radius: 20px;
}

.box-card3 {
  width: 700px;
  margin-left: 20px;
  border-radius: 20px;
}

.card1 {
  margin-left: 30px;
  margin-top: 0px;
}


.Border_l{

  margin-top: 10px;
    border-left:1.8px solid #000;
    height: 40px;
}

.Border_2{
    border-bottom:1.8px solid #000;
    width: 1290px;
    margin-left: 20px;
}

.card2 {
  display: flex;

  margin-top: 50px;
}


.my-card {
  display: flex;
  flex-direction: column;
  margin-top: 5.5px;
}

.my-footer {
  font-size: 12px;
  margin-top: 5px;
}

/*底部样式*/
.footer {
  background-color: #F8F9FA;
  text-align: center;
  line-height: 24px;
  padding: 50px 0;
}
.footer i {
  font-style: normal;
  margin: 0 5px;
}
.footer a {
  color: #000;
}
.footer a:hover {
  color: #F60;
}
.web {
  padding-top: 30px;
}
.web img {
  margin: 0 12px;
}
.hr_25 {
  height: 25px;
}
</style>

<style>
@keyframes fade {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>