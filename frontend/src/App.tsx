import React from 'react';
import { RecoilRoot } from 'recoil';
import Router from 'routes/routes';
import { BrowserView, MobileView } from 'react-device-detect';
import LoginRecoil from 'utils/LoginRecoil';
import { Toaster } from 'react-hot-toast';
import MobileGuidePage from 'pages/MobileGuidePage/MobileGuidePage';

function setScreenSize() {
  let vh = window.innerHeight * 0.01;
  document.documentElement.style.setProperty('--vh', `${vh}px`);
  console.log('실행!');
}
setScreenSize();
window.addEventListener('resize', setScreenSize);

function App() {
  return (
    <div>
      <RecoilRoot>
        <Toaster position="top-center" />
        <LoginRecoil />
        <BrowserView>
          <MobileGuidePage />
        </BrowserView>
        <MobileView>
          <Router />
        </MobileView>
      </RecoilRoot>
    </div>
  );
}

export default App;
