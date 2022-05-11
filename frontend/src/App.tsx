import React from 'react';
import { RecoilRoot } from 'recoil';
import Router from 'routes/routes';
import { BrowserView, MobileView } from 'react-device-detect';
import LoginRecoil from 'utils/LoginRecoil';
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <div>
      <RecoilRoot>
        <Toaster position="top-center" />
        <LoginRecoil />
        <BrowserView>
          <h1> 모바일에 최적화 된 페이지 입니다. 모바일로 접속해주세요! </h1>
        </BrowserView>
        <MobileView>
          <Router />
        </MobileView>
      </RecoilRoot>
    </div>
  );
}

export default App;
