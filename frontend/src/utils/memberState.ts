import { atom } from 'recoil'

const memberState = atom({
  key: 'memberState',
  default: {
    language: 'KOREAN',
    id: '익명',
    community: 'KOREA',
    soundOff: false,
    role: 'MEMBER',
    seq: 1
  }
})

export default memberState