import { atom } from 'recoil'

export const languageState = atom({
  key: 'languageState', // unique ID (with respect to other atoms/selectors)
  default: 'KOREAN', // default value (aka initial value)
});

export const idState = atom({
  key: 'idState', // unique ID (with respect to other atoms/selectors)
  default: '익명', // default value (aka initial value)
});

export const communityState = atom({
  key: 'communityState', // unique ID (with respect to other atoms/selectors)
  default: 'KOREA', // default value (aka initial value)
});

export const soundOffState = atom({
  key: 'soundOffState', // unique ID (with respect to other atoms/selectors)
  default: false, // default value (aka initial value)
});

export const roleState = atom({
  key: 'roleState', // unique ID (with respect to other atoms/selectors)
  default: 'MEMBER', // default value (aka initial value)
});