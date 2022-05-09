import React, { useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsSetDate.module.scss';
import Calendar from 'react-calendar';
import { fDateDash } from 'utils/formatTime';
import 'react-calendar/dist/Calendar.css';
import moment from 'moment';
const cx = classNames.bind(styles);

const ContentsSetDate = ({ onClick, form, setForm }: any) => {
  const date: Date = new Date();
  date.setDate(date.getDate() + 2);
  const back = () => {
    onClick(2);
  };

  const setData = () => {
    navigator.geolocation.getCurrentPosition((position) => {
      setForm({
        ...form,
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
      });
    });

    console.log(form);
    onClick(4);
  };
  const [dateState, setDateState] = useState(date);
  const changeDate = (e: React.SetStateAction<Date>) => {
    setForm({ ...form, date: fDateDash(e.toString()) });
    setDateState(e);
  };
  return (
    <div className={cx('container')}>
      <div className={cx('calendar')}>
        <div className={cx('calendar-font')}>도착 할 날짜를 입력해주세요.</div>
        <Calendar
          className={cx('react-calendar')}
          formatDay={(locale, date) => moment(date).format('DD')}
          minDate={date}
          showNeighboringMonth={false}
          value={dateState}
          onChange={changeDate}
        />
        <div>Selected: {moment(dateState).format('MMMM Do, YYYY')}</div>
        <button className={cx('back')} onClick={back}>
          Back
        </button>
        <button className={cx('calendar-btn')} onClick={setData}>
          완료
        </button>
      </div>
    </div>
  );
};

export default ContentsSetDate;
