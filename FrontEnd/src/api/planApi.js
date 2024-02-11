import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 여행 계획 생성
async function planCreateApi(param, success, fail) {
  await local.post(`/plan/create`, param).then(success).catch(fail);
}

// 나의 여행 계획 리스트 조회
async function planListGetApi() {
  return await local.get(`/plan/list`);
}

// 해당 여행 계획 조회하기
async function planGetApi(planId) {
  return await local.get(`/plan/${planId}`);
}

// 여행 계획 수정 (계획명)
async function planNameUpdateApi(planId, param) {
  return await local.patch(`/plan/update/${planId}/name`, param);
}

// 여행 계획 수정 (일정)
async function planDateUpdateApi(planId, param) {
  return await local.patch(`/plan/update/${planId}/date`, param);
}

// 여행 세부 계획 생성 OR 수정
async function planDetailCreateApi(planId, param) {
  return await local.post(`/plan/${planId}/detail/update`, param);
}

// 여행 세부 계획 리스트 조회
async function planDetailListGetApi(planId) {
  return await local.get(`/plan/${planId}/detail`, planId);
}

// 여행 세부 계획 수정
async function planDetailUpdateApi(param, success, fail) {
  console.log("param", param);
  await local.put(`/plan/${param.planId}/detail`, param).then(success).catch(fail);
}

export {
  planCreateApi,
  planListGetApi,
  planGetApi,
  planNameUpdateApi,
  planDateUpdateApi,
  planDetailCreateApi,
  planDetailListGetApi,
  planDetailUpdateApi,
};
