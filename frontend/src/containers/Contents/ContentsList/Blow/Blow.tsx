import React, { useEffect, useState } from 'react';
import { sound } from 'utils/soundRecognize';
import classNames from 'classnames/bind';
import BlowAnimation from 'components/Animation/BlowAnimation/BlowAnimation';
import toast, { Toaster } from 'react-hot-toast';
import styles from './Blow.module.scss';
import { postContentsAdd } from 'services/api/Contents';
import { useNavigate } from 'react-router-dom';
const cx = classNames.bind(styles);

const Blow = ({ onClick, form, setForm, seq }: any) => {
  const [isShowing, setIsShowing] = useState(true);
  const [loading, SetLoading] = useState(false);
  const [endstate, SetEndstate] = useState(false);
  const [touchstate, SetTouchstate] = useState(false);
  const [throttle, setThrottle] = useState(false);
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
      handleSend();
      console.log('전송');
    }
  }, [loading]);

  useEffect(() => {
    if (touchstate === true) {
      SetWind(true);
    }
  }, [touchstate]);

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };
  const msgDetect = (state: number) => {
    SetCheckState(state);
  };

  const possible = (state: number) => {
    SetState(state);
  };

  const touchDetect = (state: boolean) => {
    SetTouchstate(true);
  };

  const sendDataForm = async () => {
    console.log(form.date);
    const formData = new FormData();
    formData.append('imageFile', form.image);
    formData.append(
      'petalRegisterForm',
      new Blob(
        [
          JSON.stringify({
            message: form.message,
          }),
        ],
        {
          type: 'application/json',
        }
      )
    );
    console.log(formData);
    try {
      setThrottle(true);
      const response = await postContentsAdd({ formData, seq });
      if (response.status === 201) {
        console.log('성공');
        setThrottle(false);
      } else {
        console.log(response);
        setThrottle(false);
      }
    } catch (error) {
      console.log('실패');
      toast('전송에 실패하였습니다.', {
        icon: '🌼',
        style: {
          borderRadius: '10px',
          background: '#333',
          color: '#fff',
        },
      });
      navigate('/');
      setThrottle(false);
    }
  };

  useEffect(() => {
    if (possibleState) {
      sound();
      window.addEventListener('blow', blow);
      return () => {
        window.removeEventListener('blow', blow);
      };
    }
  }, [possibleState]);

  const handleSend = () => {
    if (throttle) return;
    if (!throttle) {
      sendDataForm();
    }
  };

  return (
    <div
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      <Toaster position="top-center" reverseOrder={false} />
      {isShowing && (
        <>
          {possibleState && !wind && (
            <div className={cx('windGuide')}>바람을 불어주세요</div>
          )}
          <BlowAnimation
            endstate={stateDetect}
            msgCheck={msgDetect}
            isPossible={possible}
            windState={wind}
            touchEvt={touchDetect}
          />
        </>
      )}
    </div>
  );
};

export default Blow;
