import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { sound } from 'utils/soundRecognize';
const Blow = ({ onClick, form, setForm }: any) => {
  const [loading, SetLoading] = useState(false);
  const navigate = useNavigate();
  const blow = () => {
    console.log('바람 인식');
    SetLoading(true);
    window.removeEventListener('blow', blow);
    setTimeout(pagemove, 300);
  };

  const pagemove = () => {
    navigate('/');
  };

  useEffect(() => {
    console.log(form);
    if (loading === true) {
      console.log('전송');
    }
  }, [loading]);

  useEffect(() => {
    sound();
    window.addEventListener('blow', blow);
    return () => {
      window.removeEventListener('blow', blow);
    };
  }, []);
  return (
    <div>
      Blow
      <div>{loading ? '바람확인' : '바람대기중'}</div>
      <button onClick={pagemove}>Home</button>
    </div>
  );
};

export default Blow;
