import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './LandingPage.module.scss';
import { useNavigate } from 'react-router';
import toast, { Toaster } from 'react-hot-toast';
import LandingModel from 'components/Landing/LandingModel';
import { petalCatchResultList, petalCatchResultSeq } from 'atoms/atoms';
import { ReactComponent as Menu } from 'assets/images/menu.svg';
import {
  getContents,
  leftSeedCount,
  resetContentsState,
} from 'services/api/Contents';
import { useRecoilValue, useSetRecoilState } from 'recoil';
const cx = classNames.bind(styles);
const LandingPage = () => {
  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0];
  const [loading, setLoading] = useState(false);
  const [prevent, setPrevent] = useState(0);
  const [throttle, setThrottle] = useState(false);
  const setPetalData = useSetRecoilState(petalCatchResultList);
  const setPetalSeq = useSetRecoilState(petalCatchResultSeq);
  const petaldata = useRecoilValue(petalCatchResultList);
  const patalseq = useRecoilValue(petalCatchResultSeq);

  let howManyTouches = 0;
  const navigate = useNavigate();

  const mocklist = [
    {
      contentImageUrlPath:
        'https://blog.kakaocdn.net/dn/bVa1Ja/btqTtrb27nz/dF3Mr20K37IUZ6K2lGJGJ1/img.png',
      createdDate: '2022-10-10',
      message: '123',
      nation: 'KOREA',
      nationImageUrlPath: '123',
      seq: 1,
    },
    {
      contentImageUrlPath:
        'https://blog.kakaocdn.net/dn/bVa1Ja/btqTtrb27nz/dF3Mr20K37IUZ6K2lGJGJ1/img.png',
      createdDate: '2022-10-11',
      message: '456',
      nation: 'KOREA',
      nationImageUrlPath: '123',
      seq: 2,
    },
  ];

  const moveListPage = async () => {
    try {
      setThrottle(true);
      const result = await getContents();
      console.log(result);
      navigate('/contents/list');
      if (result.status === 204) {
        toast('현재 잡을 수 있는 씨앗이 없습니다.', {
          icon: '🌼',
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
          },
        });
        setThrottle(false);
        const result = mocklist;
        setPetalData(result.reverse());
        setPetalSeq(122);
        navigate('/contents/list');
        //navigate('/');
      } else if (result.status === 200) {
        setPetalData(result.data.data.petalInfos.reverse());
        setPetalSeq(result.data.data.dandelionSeq);
        navigate('/contents/list');
        setThrottle(false);
        setLoading(true);
      } else {
        const result = mocklist;
        setPetalData(result);
        setPetalSeq(122);
        navigate('/contents/list');
      }
      //const result = mocklist;
      //console.log(result);

      //setList(mocklist);
      //setSeq(2);
    } catch (error) {
      console.log(error);
    }

    console.log('swipe down');
  };

  const moveCreatePage = async () => {
    try {
      const result = await leftSeedCount();
      setIsShowing(true);
      if (result.data.leftSeedCount > 0) {
        console.log(result.data.leftSeedCount);
        console.log('action: swipe up');
        navigate('/contents/create');
      } else {
        toast('남은 씨앗 수가 없습니다.', {
          icon: '🌼',
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
          },
        });
      }
    } catch (error) {
      console.log(error);
    }
  };
  const handleTouchStart = (e: TouchEvent) => {
    howManyTouches = e.touches.length;
    if (howManyTouches > 1) {
      return;
    }
    xStart = e.touches[0].clientX;
    yStart = e.touches[0].clientY;
  };
  const handleTouchEnd = (e: TouchEvent) => {
    if (howManyTouches > 1) {
      return;
    }
    xEnd = e.changedTouches[0].clientX;
    yEnd = e.changedTouches[0].clientY;
    if (Math.abs(xEnd - xStart) < 30) {
      if (yEnd - yStart > 50 && !throttle) {
        moveListPage();
        //setIsShowing(false);
      } else if (yEnd - yStart < -50) {
        moveCreatePage();
      }
    }
  };

  const resetState = async () => {
    const response = await resetContentsState(patalseq);
    console.log(response);
    setPetalData([{}]);
    setPetalSeq(0);
  };

  const [isShowing, setIsShowing] = useState(true);

  useEffect(() => {
    if (!localStorage.getItem('token')) {
      navigate('/login');
    }
    console.log(patalseq);
    if (patalseq !== 0) {
      resetState();
    }

    window.addEventListener('touchstart', handleTouchStart);
    window.addEventListener('touchend', handleTouchEnd);

    return () => {
      window.removeEventListener('touchstart', handleTouchStart);
      window.removeEventListener('touchend', handleTouchEnd);
    };
  }, []);

  return (
    <section
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      {/* <h1>제발!!</h1> */}
      <button className={cx('menu-button')}>
        <Toaster position="top-center" reverseOrder={false} />
        <Menu className={cx('menu-svg')}></Menu>
      </button>

      {isShowing && <LandingModel></LandingModel>}
    </section>
  );
};

export default LandingPage;
