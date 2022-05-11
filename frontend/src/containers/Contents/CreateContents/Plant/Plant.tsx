import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './Plant.module.scss';
import { animationEnd } from 'atoms/atoms';
import { useRecoilState, useRecoilValue } from 'recoil';
import { useNavigate } from 'react-router';
import LandingModel from 'components/Landing/LandingModel';
import { ReactComponent as Menu } from 'assets/images/menu.svg';
import NewPlantAnimation from 'components/Animation/PlantAnimation/NewPlantAnimation';

const cx = classNames.bind(styles);
const Plant = ({ onClick }: any) => {
  const [endstate, SetEndstate] = useState(false);
  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0];
  let howManyTouches = 0;

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };

  // const navigate = useNavigate();

  // const handleTouchStart = (e: TouchEvent) => {
  //   howManyTouches = e.touches.length;
  //   if (howManyTouches > 1) {
  //     return;
  //   }
  //   xStart = e.touches[0].clientX;
  //   yStart = e.touches[0].clientY;
  // };
  // const handleTouchEnd = (e: TouchEvent) => {
  //   if (howManyTouches > 1) {
  //     return;
  //   }
  //   xEnd = e.changedTouches[0].clientX;
  //   yEnd = e.changedTouches[0].clientY;
  //   if (Math.abs(xEnd - xStart) < 30) {
  //     if (yEnd - yStart > 50) {
  //       console.log('swipe down');
  //       navigate('/');
  //       setIsShowing(false);
  //     }
  //   }
  // };

  const [isShowing, setIsShowing] = useState(true);

  // useEffect(() => {
  //   window.addEventListener('touchstart', handleTouchStart);
  //   window.addEventListener('touchend', handleTouchEnd);

  //   return () => {
  //     window.removeEventListener('touchstart', handleTouchStart);
  //     window.removeEventListener('touchend', handleTouchEnd);
  //   };
  // }, []);

  useEffect(() => {
    console.log(endstate);
    if (endstate === true) {
      onClick(1);
    }
  }, [endstate]);

  return (
    <div
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      {isShowing && (
        <NewPlantAnimation endstate={stateDetect}></NewPlantAnimation>
      )}
    </div>
  );
};

export default Plant;
