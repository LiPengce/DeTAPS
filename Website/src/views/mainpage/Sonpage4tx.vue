<script setup>
import { onMounted } from 'vue'
import { ref } from 'vue'
import { Search,User,QuestionFilled,Timer } from '@element-plus/icons-vue'
import Web3 from "web3";
import { usePsingStore } from '@/store'

const PSingStore = usePsingStore();
console.log(PSingStore.blockinf);

const formatTime = () =>{
    let date = new Date();

    let year = date.getFullYear();

    let month = date.getMonth()+1;

    let day = date.getDate();

    let hour = date.getHours();

    let minute = date.getMinutes();

    let second = date.getSeconds();

    return (
        year + '-' + month.toString().padStart(2, '0') + '-' + day.toString().padStart(2, '0') + ' ' + hour.toString().padStart(2, '0') + ':' + minute.toString().padStart(2, '0') + ':' + second.toString().padStart(2, '0')
    );
};

const currentTime = ref(formatTime());


const input2 = ref('')

/* 
const list1 = ref([
    { id:1, list1_append:'Transaction Hash', list1_prepend:"0xe9568a8a88f974c68a7dc83998f454e1afa2cd51b252aba655776e6f8b38e856" },
    { id:2, list1_append:'Status', list1_prepend:"succeed" },
    { id:3, list1_append:'Block', list1_prepend:"396" },
    { id:4, list1_append:'Timestamp', list1_prepend:currentTime.value },
])
*/

const list1 = ref([
    { id:1, list1_append:'InputData', list1_prepend: PSingStore.blockinf.inputData},
    { id:2, list1_append:'Status', list1_prepend:PSingStore.blockinf.status },
    { id:3, list1_append:'Block', list1_prepend:PSingStore.blockinf.blocknumber },
    { id:4, list1_append:'Timestamp', list1_prepend:PSingStore.blockinf.current_time },
])
const list2 = ref([
    { id:5, list1_append:'From', list1_prepend:PSingStore.blockinf.from },
    { id:6, list1_append:'to', list1_prepend:PSingStore.blockinf.to }
])

const list3 = ref([
    { id:7, list1_append:'GasLimit', list1_prepend:(PSingStore.blockinf.GasLimit || 'default value').toString() },
    { id:8, list1_append:'GasPrice', list1_prepend:(PSingStore.blockinf.GasPrice || 'default value').toString() },
    { id:9, list1_append:'nonce', list1_prepend:(PSingStore.blockinf.nonce || 'default value').toString() },
    { id:10, list1_append:'Block Hash', list1_prepend:PSingStore.blockinf.blockhash }
])

function shortenString(str, maxLength) {
    if (typeof str !== 'string' || str === undefined) {
        console.error('Invalid input: Expected a string');
        return ''; // 或者返回原值，或特定错误信息
    }
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

onMounted => {
    console.log(list1);
    console.log(list2);
    console.log(list3);
    
}

const textarea = ref(0);
textarea.value = PSingStore.blockinf.inputData.toString();

</script>

<template>
 <div class="common-layout">
    <el-container>
        <el-affix :offset="0" style=" z-index: 1000; 
  width: 100%;
  background-color: white; 
  height: 60px;margin-bottom: 15px; position: fixed;top: 0px;
  left: 0;">
        <div style="display: flex; justify-content: flex-end;margin-right: 10%;margin-bottom: 0px;margin-top: 10px;">
            <div class="input-div" style="margin-bottom: -10px;">
        <el-input v-model="input2" class="input-class" style="height: 32px;" placeholder="Search by Txn Hash/Block">
            <template #prepend>
                <el-button :icon="Search" />
            </template>
            <template #append><span style="cursor: pointer;">搜索</span></template>
        </el-input>
        </div>
    </div>

    <el-divider/>
  </el-affix>
        <el-header style="margin-top: 60px;">
            
        <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        @select="handleSelect"
        style="margin-top: 0px;margin-left: 5%;margin-right: 5%;"
        router
        >
        <img src="@\img\DeTAPS旁边的logo.png" style="width: 35px;height: 35px; margin-top: 10px;margin-bottom: 15px;margin-left: 80px;" alt="My Logo">
        <div style="color:rgba(4, 26, 159) ;font-family: '方正字迹-惊鸿体',sans-serif;font-size: 38px; font-weight:bold ; display: flex; align-items: center; justify-content: center;margin-left: 18px;">DeTAPS</div>
        <div class="flex-grow" />
        <el-menu-item index="/signature" style="font-size: 20px;">首页</el-menu-item>
        <el-menu-item index="/blockchain" style="font-size: 20px;">区块链</el-menu-item>
        <el-menu-item index="2" style="font-size: 20px;">更多</el-menu-item>
        <el-divider style="margin-top: 22px;" direction="vertical" />
        <el-menu-item style="margin-right: 100px;font-size: 20px;" index="/login"><img src="@\img\登录人像.png" style="width: 27px;height: 27px;margin-right: 15px;" alt="My Logo">
          登录</el-menu-item>

        </el-menu>
      </el-header>
      <el-main class="main1">
        <div style="display: flex; flex-direction: column;justify-content: flex-start;">
            <!--<div style="display: flex;margin-left: 10%;margin-top: 0px;margin-bottom: -10px;">-->
            <div style="display: flex;justify-content: center; align-items: center;height: 100%;">
            <span style="font-weight: bold;font-size: 50px;font-family: '方正字迹-惊鸿体',sans-serif;margin-top: 0px;margin-bottom: 0%;">Transaction Details</span>
            <!--<span style="font-size: 20px;margin-left: 10px;color: #b5b6ba;">#{{ PSingStore.blockinf.blocknumber }}</span>-->
        </div>
        <div style="display: flex; justify-content: center;">
        <el-divider style="width: 100%;opacity: 0;margin-bottom: -5px;" />
        </div>
        <el-card class="card1" style="width: 80%;margin:auto;height: 100%;margin-bottom: 30px;">
            <div class="content" v-for="item in list2" :key="item.id" style="margin-top: 1vh;margin-left: 5%;margin-bottom: 2%;">
                <div class="column">
                    <span style="color: #6C757D;display: inline-flex;align-items: center;"><el-icon style="margin-right: 5px;"><QuestionFilled /></el-icon>{{ item.list1_append }}:</span>
                </div>
                <div class="column right-column">
                    <div class="inner-column" style="margin-left: -25%;">
                        <span style="color: #2784C3;">{{ item.list1_prepend }}</span>
                    </div>
                </div>
            </div>
            <el-divider />
            <div class="content" v-for="item in list3" :key="item.id" style="margin-top: 1vh;margin-left: 5%;margin-bottom: 2%;">
                <div class="column">
                    <span style="color: #6C757D;display: inline-flex;align-items: center;"><el-icon style="margin-right: 5px;"><QuestionFilled /></el-icon>{{ item.list1_append }}:</span>
                </div>
                <div class="column right-column">
                    <div class="inner-column" style="margin-left: -25%;">
                        <span>{{ shortenString(item.list1_prepend,70) }}</span>
                    </div>
                </div>
            </div>
            <!--<el-divider />-->
           
        </el-card>
        <el-card class="card1" style="width: 80%;margin: auto;height: 100%;">
            <div class="content" v-for="item in list1" :key="item.id" style="margin-top: 1vh;margin-left: 5%;margin-bottom: 2%;">
                <div v-if="item.id > 1" class="column">
                    <span style="color: #6C757D;display: inline-flex;align-items: center;"><el-icon style="margin-right: 5px;"><QuestionFilled /></el-icon>{{ item.list1_append }}:</span>
                </div>
                <div v-if="item.id > 1" class="column right-column">
                    <div class="inner-column" style="margin-left: -25%;">
                        <span v-if="item.id != 2" style="display: inline-flex;align-items: center;"><el-icon v-if="item.id == 4" style="margin-right: 5px"><Timer /></el-icon>{{ item.list1_prepend }}</span>
                        <el-tag v-else
                        size="large"
                        hit="true"
      :key="item.list1_prepend"
      type="success" effect="dark"
    >
      {{ item.list1_prepend? 'success':'failed' }}
    </el-tag>
                    </div>
                </div>
            </div>
            <el-divider />
            <div class="content" style="margin-top: 1vh;margin-left: 5%;">
                <div class="column">
                    <span style="color: #6C757D;display: inline-flex;align-items: center;"><el-icon style="margin-right: 5px;"><QuestionFilled /></el-icon>InputData :</span>
                </div>
                <div class="column right-column">
                    <div class="inner-column" style="margin-left: -25%;">
                        <el-input
    v-model="textarea"
    style="width: 100%"
    :rows="15"
    type="textarea"
    placeholder="Please input"
  />
                    </div>
                </div>
            </div>
        </el-card>
        
    </div>
      </el-main>
    </el-container>
  </div>
  <el-backtop :bottom="50" :right="30">
    
  </el-backtop>
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
</template>

<style>
@font-face{
  font-family: '方正字迹-惊鸿体';
  src: url('@/img/方正字迹-惊鸿体.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.main1 {
    background-color: #FAFBFD; 
}

.flex-grow {
  flex-grow: 1;
}

.card1 {
    height: 80vh; /* 设置容器高度为视口高度 */
    margin-top: 30px;
    margin-left: 80px;
    width: 150vh;
    border-radius: 15px;
}

.content {
    display: flex;
    justify-content: space-between;
  }

.column {
    flex: 1;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    word-wrap: break-word; /* 针对英文单词过长的情况 */
  white-space: normal;
  max-width: 50%; 
}

.right-column {
  flex: 1;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  word-wrap: break-word;
  white-space: normal;
}

.inner-column {
  width: 100%;
  text-align: start; /* 对齐方式设置为左对齐（起点对齐） */
}

.input-div {
    width: 600px;
    height:35px;
    border-radius: 95px;
    background-color: #fff;
    border: 1px solid rgba(128, 128, 128, 0.3);
}
 
.input-class {
 
    .el-input-group__prepend {
        margin-top: 0px;
        border-radius: 95px;
        border: 0;
        height: 35px;
        box-shadow: 0 0 0 0px;
    }
 
    .el-input__wrapper {
        
        border-radius: 95px;
        border: 0;
        box-shadow: 0 0 0 0px;
    }
 
    .el-input-group__append {
        margin-top: 0px;
        border-radius: 95px;
        border: 0;
        height: 35px;
        box-shadow: 0 0 0 0px;
    }
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
</style>