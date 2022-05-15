import React, { useEffect, useRef, useState } from 'react';
import petal from 'assets/images/img_petal_1.png';
import classNames from 'classnames/bind';

import 'react-day-picker/dist/style.css';
import styles from './ContentsCheck.module.scss';
import { fDateDash } from 'utils/formatTime';
import iconimg from 'assets/images/icon/icon_dandelion.png';
import { useNavigate } from 'react-router-dom';
const cx = classNames.bind(styles);

const ContentsCheck = ({ onClick, form, setForm }: any) => {
  const date: string = new Date().toString();
  const [letters, SetLetters] = useState(0);
  const [imgFile, setImgFile] = useState('');
  const [text, SetText] = useState<string>('');
  const navigate = useNavigate();
  const back = () => {
    onClick(2);
  };

  const inputRef = useRef<HTMLInputElement>(null);

  const handleUploadBtnClick = () => {
    inputRef.current?.click();
  };
  const home = () => {
    navigate('/');
  };

  const sendData = () => {
    console.log(imgFile);
    console.log(text);
    onClick(4);
  };

  useEffect(() => {
    SetText(form.message);
    if (form.image) setImgFile(URL.createObjectURL(form.image));
  }, []);

  const popupOn = () => {
    //todo popup 실행
  };

  return (
    <div className={cx('container')}>
      <div className={cx('inner-container')}>
        <img
          className={cx('back-btn')}
          src={iconimg}
          onClick={home}
          alt="home"
        />
        <div className={cx('petal-img')}>
          <img className={cx('petal')} src={petal} alt="petal" />
          <div className={cx('editor')}>
            <div className={cx('date')}> {fDateDash(date)}</div>
            <div className={cx('scrollBar')}>
              <div className={cx('thumbnail')}>
                <div className={cx('default')}>
                  {imgFile ? (
                    <>
                      <div onClick={popupOn} className={cx('preview-img')}>
                        <img src={imgFile} alt="preview" />
                      </div>
                    </>
                  ) : null}
                </div>
              </div>
              <textarea value={text} disabled />
            </div>
          </div>
        </div>
        <div>
          <div className={cx('btn')} onClick={sendData}>
            Send
          </div>
          <div className={cx('btn')} onClick={back}>
            Back
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContentsCheck;
function moment(date: Date) {
  throw new Error('Function not implemented.');
}
