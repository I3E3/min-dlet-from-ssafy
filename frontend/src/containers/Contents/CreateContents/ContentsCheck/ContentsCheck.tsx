import React, { useEffect, useRef, useState } from 'react';
import petal from 'assets/images/img_petal_1.png';
import classNames from 'classnames/bind';
import styles from './ContentsCheck.module.scss';
import { fDateDash } from 'utils/formatTime';
import { ReactComponent as ImageIcon } from 'assets/images/icon/image_white.svg';
import { ReactComponent as DeleteImg } from 'assets/images/icon/icon_img_delete.svg';
const cx = classNames.bind(styles);

const ContentsCheck = ({ onClick, form, setForm }: any) => {
  const date: string = new Date().toString();
  const [letters, SetLetters] = useState(0);
  const [imgFile, setImgFile] = useState('');
  const [text, SetText] = useState<string>('');

  const back = () => {
    onClick(1);
  };

  const inputRef = useRef<HTMLInputElement>(null);

  const handleUploadBtnClick = () => {
    inputRef.current?.click();
  };

  const sendData = () => {
    console.log(imgFile);
    console.log(text);
    onClick(3);
  };

  useEffect(() => {
    SetText(form.message);
    setImgFile(form.image);
  }, []);

  const popupOn = () => {
    console.log('ㅇㅅㅇ');
  };

  return (
    <div className={cx('container')}>
      {/* <div
        className={
          open ? [styles.openModal, styles.modal].join(' ') : styles['modal']
        }
      >
        {open ? (
          <section>
            <header>
              프로필 수정
              <button className={cx('close')} onClick={close}>
                &times;
              </button>
            </header>
            <main>
              <div className={cx('title')}>닉네임 수정</div>
            
              <div className={cx('title')}>프로필 사진 변경</div>
          
            </main>
          </section>
        ) : null}
      </div> */}

      <div className={cx('inner-container')}>
        <div className={cx('petal-img')}>
          <img className={cx('petal')} src={petal} alt="petal" />
          <div className={cx('editor')}>
            <div className={cx('date')}> {fDateDash(date)}</div>
            <div className={cx('thumbnail')}>
              <div className={cx('default')} onClick={handleUploadBtnClick}>
                {imgFile ? (
                  <>
                    <div onClick={popupOn} className={cx('preview-img')}>
                      <img src={imgFile} alt="preview" />
                    </div>
                  </>
                ) : (
                  <div className={cx('upload')}>
                    <ImageIcon width={42} height={42} />
                  </div>
                )}
              </div>
            </div>
            <textarea value={text} disabled />
          </div>
        </div>
        <div>
          <button onClick={back}>back</button>
          <div className={cx('write-btn')} onClick={sendData}>
            Send
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContentsCheck;
