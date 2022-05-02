import React from 'react';
import { RecoilRoot } from 'recoil';
import Router from 'routes/routes';
import { BrowserView, MobileView } from 'react-device-detect';

function App() {
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
