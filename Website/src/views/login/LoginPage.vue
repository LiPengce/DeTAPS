<script setup>
import { ref, reactive,onMounted,onBeforeUnmount } from 'vue';
import { User, Lock,Search} from '@element-plus/icons-vue';
import { useTokenStore } from '@/store'
import { useLoginStore } from '@/store'
import axios from 'axios';
import router from '../../router';


const TokenStore = useTokenStore();
const LoginStore = useLoginStore();

const formData = ref({
    role:'签名者',
    username:'test01',
    password:'123456',
})
import backgroundUrl from '@/img/登录背景图.jpg';
onMounted(() => {
  document.body.style.background = `url(${backgroundUrl}) no-repeat`;
  document.body.style.backgroundSize = "100% 140%";
});
onBeforeUnmount(() => {
  // 组件卸载时恢复背景样式
  document.body.style.background = "";
  document.body.style.backgroundSize = "";
});

const handleSubmit = (event) =>{
    event.preventDefault();
    
    const params = new FormData();
    params.append('role', formData.value.role);
    params.append('username', formData.value.username);
    params.append('password', formData.value.password);

    LoginStore.setUsername(formData.value.username);
    LoginStore.setRole(formData.value.role);

    axios.post('http://47.96.177.120:8080/user/login',params)
        .then(response =>{
            console.log(response.data);
            if (response.data && response.data.data && response.data.code === 0 && formData.value.role == '签名者') {
                TokenStore.setToken(response.data.data);
                console.log(response.data.data);
                console.log('登录成功');
                router.push('/signature')//跳转到指定路由
            } else if(response.data && response.data.data && response.data.code === 0 && formData.value.role == '公证人'){
                TokenStore.setToken(response.data.data);
                console.log(response.data.data);
                console.log('登录成功');
                router.push('/notary')//跳转到指定路由
            }else {
                alert(response.data.message + '登录失败');
            }
        })
        .catch(error =>{
            console.log(error);
        });
}

</script>

<template>
    <div class="container">
        <!--
        <div style="margin-top: 7.5%;">
            <span style="font-size: 60px;font-family: '兰亭粗黑',sans-serif;margin-bottom: -20px;letter-spacing: 8px;">欢迎使用</span>
            <span style="font-size: 130px;font-family: '方正字迹-惊鸿体',sans-serif;">DeTAPS</span>
            <span style="font-size: 45px;font-family: '兰亭粗黑',sans-serif;">支持动态隐私溯源的</span>
            <span style="font-size: 45px;font-family: '兰亭粗黑',sans-serif;">分布式门限签名系统</span>
        </div>
    -->
    <div class="image-box" style="background-color: #122960;">
        <img src="@/img/登录图1.jpg" style="height: 400px;">
    </div>
    
        <div id="login_box">
            <div style="display: flex;margin-top:0;justify-content: center; align-items: center;">
                <h2 style="font-family: '兰亭粗黑',sans-serif;font-size: 30px;margin-top: 7.5%;">Explore  </h2>
                <h2 style="font-family: '方正字迹-惊鸿体',sans-serif;margin-left: 10%;font-size: 45px;color:#122960 ;"> DeTAPS</h2>
        </div>    
            <form @submit="handleSubmit" style="margin-top:25px">
                <div class="input-group" style="border-radius: 5px;border: 0px solid #000;border: 0px solid #000;background-color: #dce8e7;">
                    <!--<i class="el-icon-edit1"></i>-->
                    <!--<el-icon class="prefix-icon"><User/></el-icon>-->
                        <el-select size="large" class="custom-input" v-model="formData.role" style="padding:1px,11px; height:40px;border: 0px solid #000;width: 100%;border-radius: 5px;">
                            <template #prefix>
                                <el-icon class="el-input__icon" style="font-size:20px;margin-left:0px"><User /></el-icon>
                            </template>
                        <el-option style="color: black; font-size: 15px;font-family:'兰亭黑简',sans-serif;" value="签名者">签名者</el-option>
                        <el-option style="color: black; font-size: 15px;font-family:'兰亭黑简',sans-serif;" value="公证人">公证人</el-option>
                    </el-select>
                </div>
                <div class="input-group custom-input" style="margin-top: 30px;border-radius: 5px;border: 0px solid #000;">
                <!--<i class="el-icon-edit"></i>-->
                <!--<input type="text" v-model="formData.username" style="width: 360px;color: black;border-radius: 5px;" placeholder="test01">-->
                
                    <el-input :prefix-icon="Search" class="custom-input" v-model="formData.username" style="height:40px ; width: 100%;color: black;border-radius: 5px;background-color: #dce7e5;" placeholder="用户名" clearable />   
                </div>
            
                <div class="input-group" style="margin-top: 30px;border-radius: 5px;border: 0px solid #000;">
                <!--<i class="el-icon-share"></i>-->
                <!--<input type="password" v-model="formData.password" style="width: 360px;color: black;border-radius: 5px;" placeholder="123456">-->
                <el-input :prefix-icon="Lock" v-model="formData.password" style="height:40px ;width: 100%;color: black;border-radius: 5px;border: 0px solid #000;" placeholder="密码"  type="password" show-password />
                </div>
                <!--
                <div style="display: flex;justify-content: flex-end;margin-right: 70px;">
                    <el-button style="color:black;" type="text">忘记密码？</el-button>
                </div>
                -->
                <div style="margin-left: -320px;margin-top: 20px; ">
                <input type="checkbox" id="option4" name="myOptions" value="Option 1">
                <label style="margin-left: 10px;" for="option1">记住密码</label>
                </div>

                <div class="button-group">
                    <el-button  style="font-family:'兰亭黑简',sans-serif;font-size: 20px;background-color: #122960;width:100px;transition: 'transform 0.3s ease' " type="submit" @click="handleSubmit">登录</el-button>
                </div>
                <div style="display: flex;margin-top: 15px;">
                    <div style="margin-right: 205px;">
                        <label  for="option5" style="margin-right: 10px;font-family:'兰亭黑简',sans-serif;">没有账号</label>
                        <router-link to="#" style="text-decoration: none;font-family:'兰亭黑简',sans-serif;">去注册</router-link>
                    </div>
                    <router-link to="#" style="text-decoration: none;font-family:'兰亭黑简',sans-serif;">忘记密码？</router-link>
                </div>
            </form>       
        </div>
    </div>
   
</template>

<style scoped>
.prefix-icon {
  position: absolute;
  top: 50%;
  left: 1000px;
  transform: translateY(-0%);
  font-size: 20px;
  color: black;
}

:deep(.el-input__wrapper) {
    padding: 1px 11px;
}

.el-input :deep(.el-input__icon) {
    font-size: 20px;
    margin-left: 0px
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

@font-face{
  font-family: '兰亭黑简';
  src: url('@/img/兰亭黑简.TTF') format('truetype');
  font-weight: normal;
  font-style: normal;
}


body{
    background: url('@/img/登录背景图.jpg') no-repeat;
    background-size: 100% 100%;
}

.container{
    display: flex;
    align-items: center;
    justify-content: center;
}

.image-box {
    margin-top: 10%;
    width: 25%;
    height: 500px;
    padding:50px 0px;
    display: flex;
    justify-content: center;
    align-items: center; 
}

#login_box{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start; 
    width: 30%;
    height: 500px;
    background: #dce7e5;

    margin-top: 10%;
    text-align: center;

    padding:50px 0px;
    align-items: center;
    justify-content: center;
}



.custom-input {
    color: black;                          
    background-color: #dce8e7;          
    border-color: #dcdfe6;                 

}


h2{
    color:black;
    margin-top: 5%
}




#login_box .button-group {
    display: flex;
    justify-content: center; /* 水平居中按钮 */
    margin-left: 0px;
    margin-top: 20px;

}

#login_box .button-group button {
    margin-left: 0px;
    background-color:#022F9E;
    color: white;
    padding: 6% 46.7%;
    border: 1px solid white;
}


#login_box form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start; /* 垂直居中所有子元素 */
}

.el-icon-edit{
    background: url('@/img/用户名.png') center center no-repeat;
    background-size: 26px,26px;
    margin-left: 5px;
    margin-top: 10px;
}
.el-icon-edit:before {
    content: "11";
    visibility: hidden;
}

.el-icon-share{
    background: url('@/img/密码.png') center center no-repeat;
    background-size: 22px,22px;
    margin-left: 5px;
    margin-top: 8px;
}
.el-icon-share:before {
    content: "11";
    visibility: hidden;
}

.el-icon-edit1{
    background: url('@/img/身份.png') center center no-repeat;
    background-size: 22px,22px;
    margin-right: 15px;
    margin-left: 5px;
    margin-top: 5px;
}
.el-icon-edit1:before {
    content: "11";
    visibility: hidden;
}
.el-button:hover {
  transform: scale(0.78);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  background-color: #3b4e8a; /* 更改背景颜色 */
}
:deep(.el-button){
    border-radius: 150px;
}
</style>
