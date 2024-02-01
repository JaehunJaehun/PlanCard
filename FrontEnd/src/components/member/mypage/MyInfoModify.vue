<template>
  <div class="card p-fluid">
    <div>
      <h1>회원 정보 수정</h1>
    </div>
      
    <hr id="separator">
    
    <div id="box">

      <div class="container; card p-fluid" id="modifyBox">
        <h1 id="modifyTitle">회원 정보 수정</h1>
        
        <form id="modifyForm">
          
          <div v-if="userPhotoUrl" id="profileImg">
            <img :src="userPhotoUrl" alt="프로필 이미지">
          </div>
          <div v-else></div>
          
          
          <div class="box, card p-fluid" id="userNicknameInput">
            <input type="userNickname" id="userNickname" v-model.trim="userNickname" placeholder="새로운 닉네임">
          </div>
          
          <div class="card p-fluid" id="profilePhoto">
            <p id="profilePhotoGuide">프로필 사진</p>
            <hr id="separator">
            <div class="box, card p-fluid" id="userPhotoInput">
              <input type="file" id="userPhoto" accept="image/*" @change="handlePhotoUpload">
            </div>
          </div>
          
          <div class="box, card p-fluid"  id="modifySubmit">
            <input type="submit" value="변경하기">
          </div>
        </form>

        <div id="etc1">
          <p id="withdrawalGuide">탈퇴하려고? </p>
          <button id="withdrawalBtn" @click="withdrawalRequest()">[회원탈퇴]</button>
        </div>

        <div id="overlay" v-if="withdrawalActive"></div>
        <div class="card p-fluid" v-if="withdrawalActive" id="withdrawalBox">
          <h3 id="withdrawalTitle">회원 탈퇴 확인</h3>
            <p>진짜 탈퇴해?</p>
            <div style="display: flex;">
              <form class="box, card p-fluid" @submit.prevent="" id="withdrawalForm">
                <input class="box card p-fluid" type="submit" id="withdrawalSubmit" value="탈퇴">
              </form>
              <button @click="withdrawalRequest()" id="withdrawalCloseBtn">취소</button>
            </div>
        </div>
        
        
        
      </div>
    </div>
  </div>
  </template>




<script setup>
  import { ref } from 'vue';

  const userPhoto = ref(null);
  const userPhotoUrl = ref('');

  // 파일 업로드를 위한 이벤트 핸들러
  const handlePhotoUpload = (event) => {
      const file = event.target.files[0];
      // 파일을 FormData에 추가하여 전송 가능
      userPhoto.value = file;
      userPhotoUrl.value = URL.createObjectURL(file);
  }

  const withdrawalActive = ref(false);
  // 회원탈퇴 팝업 on/off
  const withdrawalRequest = () => {
    withdrawalActive.value = !withdrawalActive.value;
  };
</script>



<style scoped>
  h1 {
    font-weight: bold;
  }
  #separator {
    border: none;
    border-top: 4px solid #3498DB;
    border-radius: 10px;
  }

  #box {
    display: flex;
    justify-content: center;
  }

  #modifyBox {
    background-color: #FFFFFF;
    border: 1px solid rgba(52, 152, 219, 0.5);
    width: 400px;
    height: 50%;
    padding: 40px;

    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
  }
  #modifyTitle {
    color: #3498db;
    font-weight: bold;
  }
  #modifyForm {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  #profileImg {
    border-radius: 50%;
    border: 1px solid rgba(52, 152, 219, 0.5);
    background-color: #FFFFFF;
    width: 250px;
    height: 250px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative; /* 상대 위치 지정 */
    overflow: hidden; /* 원 밖으로 넘치는 부분 숨김 */
    margin-top: 10px;
    margin-bottom: 20px;
  }
  #profileImg img {
    max-width: none; /* 이미지 너비를 원본 크기로 설정 */
    max-height: none; /* 이미지 높이를 원본 크기로 설정 */
    width: 100%; /* 이미지 너비를 자동으로 조정 */
    height: auto; /* 이미지 높이를 자동으로 조정 */
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%); /* 이미지를 수평 및 수직 가운데 정렬 */
  }
  #separator {
    margin-top: 3px;
    border: none;
    border-top:  1px solid rgba(52, 152, 219, 0.5);
    border-radius: 10px;
  }
  #profilePhoto {
    background-color: #FFFFFF;
    border: 1px solid rgba(52, 152, 219, 0.5);
    color: rgba(0, 0, 0, 0.5);
    font-weight: bold;
    width: 100%;
    padding: 5px;
    margin-top: 20px;
  }
  #profilePhotoGuide {
    text-align: center;
    padding-top: 5px;
    margin: 0;
  }
  #userPhotoInput {
    border: 1px solid rgba(52, 152, 219, 0.5);
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center;
    width: 90%; /* 현재 가로 너비 설정에 따라 조정 */
    margin: 0 auto; /* 가운데 정렬을 위한 외부 여백 설정 */
    margin-bottom: 10px;
  }

  #userNickname, #userPhoto {
    height: 35px;
    width: 250px;
  }
  #userNicknameInput {
    background-color: rgba(245, 245, 245, 0.1);
    width: 90%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 0px;
  }

  #modifySubmit {
    width: 90%;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498db;
    color: #FFFFFF;
    font-weight: bold;
    font-size: medium;
    height: 20px;
    padding: 20px;
  }




  #etc1 {
    display: flex;
    margin-top: 10px;
  }
  #withdrawalGuide {
    color: rgba(0, 0, 0, 0.3);
    font-weight: bold;
    margin: 0;
    margin-right: 10px;
  }
  #withdrawalBtn {
    font-weight: bold;
    color: rgba(255, 0, 0, 1);
  }

  #overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
    z-index: 997; /* 모달보다 한 단계 낮은 z-index */
  }
  #withdrawalBox {
    border: 1px solid rgba(52, 152, 219, 0.5);
    position: fixed;
    top: 30%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 9999;
  }




  #withdrawalForm {
    border: none;
    margin-bottom: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    padding-bottom: 0;
  }
  #withdrawalTitle {
    color: #3498db;
    font-weight: bold;
    margin: 0;
  }
  #withdrawalSubmit {
    width: 100px;
    height: 30px;
    text-align: center;
    color: #FFFFFF;
    background-color: #FF0000;
    position: relative;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    padding: 10px;
    margin-top: 5px; /* 위쪽 여백 추가 */
    line-height: 0px;

  }

  #withdrawalCloseBtn {
    width: 100px;
    height: 30px;
    text-align: center;
    color: #FFFFFF;
    background-color: #3498DB;
    position: relative;
    line-height: 25px;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    top: 15px;
  }

</style>