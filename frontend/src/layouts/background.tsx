import React from 'react';
import classNames from 'classnames/bind';
import { Outlet } from 'react-router-dom';
import styles from './background.module.scss';
import cloud1 from 'assets/images/cloud1.png';
import cloud2 from 'assets/images/cloud2.png';

const cx = classNames.bind(styles);
const background = () => {
  return (
    <>
      {/* <img className={cx('cloud1')} src={cloud1} alt="cloud1" /> */}
      <Outlet />
    </>
  );
};

export default background;
