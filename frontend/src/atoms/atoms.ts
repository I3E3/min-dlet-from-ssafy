import { atom } from 'recoil';

export const isLoggedState = atom({
  key: 'isLoggedState',
  default: false,
});

/* 프로필 정보 요청 */
export const loggedUserState = atom({
  key: 'loggedUserState',
  default: [],
});
