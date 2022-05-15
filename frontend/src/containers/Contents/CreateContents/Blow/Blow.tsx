import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { sound } from 'utils/soundRecognize';
import BlowAnimation from 'components/Animation/BlowAnimation/BlowAnimation';
import { postContents } from 'services/api/Contents';
import toast, { Toaster } from 'react-hot-toast';
const Blow = ({ onClick, form, setForm }: any) => {
  const [isShowing, setIsShowing] = useState(true);
  const [loading, SetLoading] = useState(false);
  const [endstate, SetEndstate] = useState(false);
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

  const sendDataForm = async () => {
    console.log(form.date);
    const formData = new FormData();
    formData.append('imageFile', form.image);
    formData.append(
      'dandelionRegisterForm',
      new Blob(
        [
          JSON.stringify({
            message: form.message,
            blossomedDate: form.date,
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
      const response = await postContents(formData);
      if (response.status === 201) {
        console.log('성공');
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

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };
  const msgDetect = (state: number) => {
    SetCheckState(state);
  };

  const possible = (state: number) => {
    SetState(state);
  };

  const handleSend = () => {
    if (throttle) return;
    if (!throttle) {
      sendDataForm();
    }
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
      <Toaster position="top-center" reverseOrder={false} />
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
