const BASE_PATH = "http://localhost:8080";
const SERVER = "https://www.min-dlet.com/login";

// 꽃밭 보기
export function getGarden() {
  return fetch(`${BASE_PATH}/garden`).then((response) => response.json());
}

// 민들레 상세정보
export function getDandelionDetail(dandelionSeq) {
  return fetch(`${BASE_PATH}/dandelions/${dandelionSeq}`).then((response) =>
    response.json()
  );
}
