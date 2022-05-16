import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './LandingPage.module.scss';
import { useNavigate } from 'react-router';
import toast, { Toaster } from 'react-hot-toast';
import LandingModel from 'components/Landing/LandingModel';
import GroupSelection from 'components/Landing/GroupSelection';
import { ReactComponent as Menu } from 'assets/images/menu.svg';
import { leftSeedCount } from 'services/api/Contents';

const cx = classNames.bind(styles);
const LandingPage = () => {
  const [isShowing, setIsShowing] = useState(true);
  const [isGroupShowing, setIsGroupShowing] = useState(false)
  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0];
  let howManyTouches = 0;
  const navigate = useNavigate();
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
      if (yEnd - yStart > 50) {
        console.log('swipe down');
        navigate('/contents/list');
        setIsShowing(false);
      } else if (yEnd - yStart < -50) {
        moveCreatePage();
      }
    }
  };

  useEffect(() => {
    if (!localStorage.getItem('token')) {
      navigate('/login');
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
      <button className={cx('menu-button')}
        onClick={() => {setIsGroupShowing((isGroupShowing) => !isGroupShowing)}}>
      {/* <button className={cx('menu-button')}> */}
        {' '}
        {/* <Toaster position="top-center" reverseOrder={false} /> */}
        <Menu className={cx('menu-svg')}></Menu>
      </button>

      {isShowing && <LandingModel></LandingModel>}
      {isGroupShowing && <GroupSelection setIsGroupShowing={setIsGroupShowing} />}
    </section>
  );
};

export default LandingPage;
