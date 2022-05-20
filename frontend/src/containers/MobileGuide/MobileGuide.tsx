import React from 'react';
import classNames from 'classnames/bind';
import styles from './MobileGuide.module.scss';
import LandingModel from 'components/Landing/LandingModel';
const cx = classNames.bind(styles);
const MobileGuide = () => {
  return (
    <div className={cx('guide')}>
      <LandingModel />
      모바일에 최적화 된 페이지 입니다.
    </div>
  );
};

export default MobileGuide;
