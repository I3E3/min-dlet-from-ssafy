import React from 'react';
import classNames from 'classnames/bind';
import styles from './LandingPage.module.scss';

const cx = classNames.bind(styles);
const LandingPage = () => {
  return <div className={cx('main')}>폰트확인 Blow Your Dandelion</div>;
};

export default LandingPage;
