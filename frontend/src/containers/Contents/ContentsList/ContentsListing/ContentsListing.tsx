import React, { useState } from 'react';
import { useSprings, animated, to as interpolate } from '@react-spring/web';
import petal from 'assets/images/img_petal_1.png';
import test from 'assets/images/ImsiImage.jpg';
import classNames from 'classnames/bind';
import { useDrag } from '@use-gesture/react';
import iconimg from 'assets/images/icon/icon_dandelion.png';
import styles from './ContentsList.module.scss';
import { useNavigate } from 'react-router-dom';
import { fDateDash } from 'utils/formatTime';
const cx = classNames.bind(styles);
const cards = [petal, petal, petal, petal, petal, petal];

// These two are just helpers, they curate spring data, values that are later being interpolated into css

const to = (i: number) => ({
  x: 0,
  y: i * -4,
  scale: 1,
  rot: -10 + Math.random() * 20,
  delay: i * 100,
});
const from = (_i: number) => ({ x: 0, rot: 0, scale: 1.5, y: -1000 });
// This is being used down there in the view, it interpolates rotation and scale into a css transform
const trans = (r: number, s: number) =>
  `perspective(1500px) rotateX(30deg) rotateY(${
    r / 10
  }deg) rotateZ(${r}deg) scale(${s})`;

const ContentsList = ({ onClick, form, setForm }: any) => {
  const navigate = useNavigate();
  const [text, SetText] = useState('안녕하세요🎃');
  const [imgFile, SetImg] = useState(test);
  const date: string = new Date().toString();
  const [gone] = useState(() => new Set()); // The set flags all the cards that are flicked out
  const [props, api] = useSprings(cards.length, (i) => ({
    ...to(i),
    from: from(i),
  })); // Create a bunch of springs using the helpers above
  // Create a gesture, we're interested in down-state, delta (current-pos - click-pos), direction and velocity

  const home = () => {
    navigate('/');
  };

  const send = () => {
    onClick(2);
  };

  const bind = useDrag(
    ({
      args: [index],
      active,
      movement: [mx],
      direction: [xDir],
      velocity: [vx],
    }) => {
      const trigger = vx > 0.2; // If you flick hard enough it should trigger the card to fly out
      if (!active && trigger) gone.add(index); // If button/finger's up and trigger velocity is reached, we flag the card ready to fly out
      api.start((i) => {
        if (index !== i) return; // We're only interested in changing spring-data for the current spring
        const isGone = gone.has(index);
        const x = isGone ? (200 + window.innerWidth) * xDir : active ? mx : 0; // When a card is gone it flys out left or right, otherwise goes back to zero
        const rot = mx / 100 + (isGone ? xDir * 10 * vx : 0); // How much the card tilts, flicking it harder makes it rotate faster
        const scale = active ? 1.1 : 1; // Active cards lift up a bit
        return {
          x,
          rot,
          scale,
          delay: undefined,
          config: { friction: 50, tension: active ? 800 : isGone ? 200 : 500 },
        };
      });
      if (!active && gone.size === cards.length)
        setTimeout(() => {
          gone.clear();
          api.start((i) => to(i));
        }, 600);
    }
  );
  // Now we're just mapping the animated values to our view, that's it. Btw, this component only renders once. :-)
  return (
    <div className={cx('container')}>
      <img className={cx('home-btn')} src={iconimg} onClick={home} alt="home" />
      <div className={cx('petal-img ')}>
        {props.map(({ x, y, rot, scale }, i) => (
          <animated.div className={cx('deck')} key={i} style={{ x, y }}>
            {/* This is the card itself, we're binding our gesture to it (and inject its index so we know which is which) */}
            <animated.div
              className={cx('petal')}
              {...bind(i)}
              style={{
                transform: interpolate([rot, scale], trans),
              }}
            >
              <img className={cx('petals')} src={`${cards[i]}`} alt="petals" />
              <div className={cx('contents')}>
                <div className={cx('petal-img')}>
                  <div className={cx('editor')}>
                    <div className={cx('date')}> {fDateDash(date)}</div>
                    <div className={cx('scrollBar')}>
                      <div className={cx('thumbnail')}>
                        <div className={cx('default')}>
                          {imgFile ? (
                            <>
                              <div className={cx('preview-img')}>
                                <img src={imgFile} alt="preview" />
                              </div>
                            </>
                          ) : null}
                        </div>
                      </div>
                      <textarea value={text} disabled />
                    </div>
                    {/* {i + 1} */}
                  </div>
                </div>
              </div>
            </animated.div>
          </animated.div>
        ))}
      </div>
      <div className={cx('send-btn')} onClick={send}>
        Send
      </div>
    </div>
  );
};

export default ContentsList;
