import React, { useEffect } from 'react';
import { RecoilRoot } from 'recoil';
import Router from 'routes/routes';
import { BrowserView, MobileView } from 'react-device-detect';

function App() {
  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }

  useEffect(() => {
    setScreenSize();
  });

  return (
    <div>
      <RecoilRoot>
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
