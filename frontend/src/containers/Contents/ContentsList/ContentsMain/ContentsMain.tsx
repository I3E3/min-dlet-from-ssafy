import React, { useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsMain.module.scss';
import Plant from '../Plant/Plant';
import Blow from '../Blow/Blow';
import ContentsListing from '../ContentsListing/ContentsListing';
import ContentsEdit from '../ContentsEdit/ContentsEdit';
import ContentsCheck from '../ContentsCheck/ContentsCheck';
const cx = classNames.bind(styles);

const ContentsMain = () => {
  const [formSteps, setFormStep] = useState(0);
  const [form, setForm] = useState({ image: '', message: '', date: '' });
  const handleformStep = (step: number) => {
    setFormStep(step);
  };

  const handleFormSet = (data: any) => {
    setForm({ ...form, ...data });
  };

  return (
    <div>
      {(() => {
        switch (formSteps) {
          case 0:
            return <Plant onClick={handleformStep} />;
          case 1:
            return (
              <ContentsListing
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 2:
            return (
              <ContentsEdit
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 3:
            return (
              <ContentsCheck
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 4:
            return (
              <Blow
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
        }
      })()}
    </div>
  );
};

export default ContentsMain;
