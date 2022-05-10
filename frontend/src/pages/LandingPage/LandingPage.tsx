import React, { useEffect ,useState } from 'react';
import classNames from 'classnames/bind';
import styles from './LandingPage.module.scss';
import LandingModel from 'components/Landing/LandingModel'
import { ReactComponent as Menu } from 'assets/images/menu.svg';

const cx = classNames.bind(styles);
const LandingPage = () => {
  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0]
  let howManyTouches = 0

  const handleTouchStart = (e:TouchEvent) => {
    howManyTouches = e.touches.length
    if (howManyTouches > 1) {
      return
    }
    xStart = e.touches[0].clientX
    yStart = e.touches[0].clientY
  }

  const handleTouchEnd = (e:TouchEvent) => {
    if (howManyTouches > 1) {
      return
    }
    xEnd = e.changedTouches[0].clientX
    yEnd = e.changedTouches[0].clientY
    if (Math.abs(xEnd - xStart) < 30) {
      if (yEnd - yStart > 50) {
        console.log("swipe down")
        setIsShowing(false)
      } else if (yEnd - yStart < -50) {
        console.log("swipe up")
        setIsShowing(true)
      }
    }
  }

  const [isShowing, setIsShowing] = useState(true)

  useEffect(
    () => {
      window.addEventListener('touchstart', handleTouchStart)
      window.addEventListener('touchend', handleTouchEnd)
      return () => {
        window.removeEventListener('touchstart', handleTouchStart)
        window.removeEventListener('touchend', handleTouchEnd)
      }
    }
  , [])

  return (
    <section style={{
      width: "100%", 
      height: "100vh", 
      overflow: "hidden"}}>
      {/* <h1>제발!!</h1> */}
      <button className={cx('menu-button')}>
        <Menu className={cx('menu-svg')}></Menu>
      </button>
      {isShowing && <LandingModel></LandingModel>}
    </section>

  );
};

export default LandingPage;
