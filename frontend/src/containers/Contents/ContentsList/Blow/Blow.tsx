import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { sound } from 'utils/soundRecognize';
import BlowAnimation from 'components/Animation/BlowAnimation/BlowAnimation';

const Blow = ({ onClick, form, setForm }: any) => {
  const [isShowing, setIsShowing] = useState(true);
  const [loading, SetLoading] = useState(false);
  const [endstate, SetEndstate] = useState(false);
  const [checkState, SetCheckState] = useState(0);
  const [possibleState, SetState] = useState(0);
  const [wind, SetWind] = useState(false);
  const navigate = useNavigate();
  const blow = () => {
    console.log('바람 인식');
    SetLoading(true);
    window.removeEventListener('blow', blow);
    SetWind(true);
    //setTimeout(pagemove, 300);
  };

  const pagemove = () => {
    navigate('/');
  };

  useEffect(() => {
    console.log(endstate);
    if (endstate === true) {
      navigate('/');
    }
  }, [endstate]);
  useEffect(() => {
    console.log(form);
    if (loading === true) {
      console.log('전송');
    }
  }, [loading]);

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };
  const msgDetect = (state: number) => {
    SetCheckState(state);
  };

  const possible = (state: number) => {
    SetState(state);
  };
  // 초기값 0 가능하면 1 불가능하면 2

  useEffect(() => {
    if (possibleState) {
      sound();
      window.addEventListener('blow', blow);
      return () => {
        window.removeEventListener('blow', blow);
      };
    }
  }, [possibleState]);
  return (
    <div
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      {isShowing && (
        <BlowAnimation
          endstate={stateDetect}
          msgCheck={msgDetect}
          isPossible={possible}
          windState={wind}
        ></BlowAnimation>
      )}
    </div>
  );
};

export default Blow;
