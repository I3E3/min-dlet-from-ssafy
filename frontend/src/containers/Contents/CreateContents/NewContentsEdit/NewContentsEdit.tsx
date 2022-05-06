import ContentsEditor from 'components/ContentsEditor/ContentsEditor';
import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './NewContentsEdit.module.scss';
import iconimg from 'assets/images/icon/icon_dandelion.png';
const cx = classNames.bind(styles);

const NewContentsEdit = ({ onClick, form, setForm }: any) => {
  const [msg, SetMsg] = useState();
  const [img, SetImg] = useState();

  useEffect(() => {
    SetMsg(form.message);
    SetImg(form.image);
  }, []);

  const check = () => {
    setForm({ ...form, image: img, message: msg });
    onClick(2);
  };

  return (
    <>
      {/* <img className={cx('out-icon')} src={iconimg} alt="out" /> */}
      <ContentsEditor form={form} img={SetImg} msg={SetMsg} onSend={check} />
    </>
  );
};

export default NewContentsEdit;
