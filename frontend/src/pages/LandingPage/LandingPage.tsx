import React, { Suspense, useEffect, useState } from "react";
import classNames from "classnames/bind";
import styles from "./LandingPage.module.scss";
import { useNavigate } from "react-router";
import toast, { Toaster } from "react-hot-toast";
import LandingModel from "components/Landing/LandingModel";
import { petalCatchResultList, petalCatchResultSeq } from "atoms/atoms";
import GroupSelection from "components/Landing/GroupSelection";
import { ReactComponent as Menu } from "assets/images/menu.svg";
import { ReactComponent as SeedIcon } from "assets/images/icon/dandelion-icon-white.svg";
import guideDown from "assets/images/handleDown.png";
import guideUp from "assets/images/handleUp.png";
import {
  getContents,
  leftSeedCount,
  resetContentsState,
} from "services/api/Contents";
import { useRecoilValue, useSetRecoilState } from "recoil";
import memberState from "utils/memberState";
import { ReactComponent as Tap } from "assets/images/Landing/tap.svg";
import { ReactComponent as DownArrow } from "assets/images/Landing/down-arrow.svg";
import { ReactComponent as UpArrow } from "assets/images/Landing/up-arrow.svg";
import { ReactComponent as Dandel } from "assets/images/Landing/dandelion-2.svg";
import { useSound } from "use-sound";
import Landing from "assets/musics/Landing2.mp3";
import ButtonEffect from "assets/musics/button_effect.wav";

const cx = classNames.bind(styles);
const musicOn = [false];

const LandingPage = () => {
  const [isShowing, setIsShowing] = useState(true);
  const [isGroupShowing, setIsGroupShowing] = useState(false);
  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0];
  const [loading, setLoading] = useState(false);
  const [seedNum, setSeedNum] = useState(0);
  const [throttle, setThrottle] = useState(false);
  const setPetalData = useSetRecoilState(petalCatchResultList);
  const setPetalSeq = useSetRecoilState(petalCatchResultSeq);
  const petaldata = useRecoilValue(petalCatchResultList);
  const patalseq = useRecoilValue(petalCatchResultSeq);
  const member = useRecoilValue(memberState);
  const [isGuideShowing, setIsGuideShowing] = useState(false);
  const [soundEnabled, setSoundEnabled] = useState(member.soundOff);
  const [play1, obj1] = useSound(Landing, {
    volume: 0.5,
    interrupt: true,
  });
  const sound1 = obj1.sound
  const [play2, obj2] = useSound(ButtonEffect, {
    volume: 0.4,
    interrupt: true,
  });
  const sound2 = obj2.sound

  let howManyTouches = 0;
  const navigate = useNavigate();

  const moveListPage = async () => {
    try {
      setThrottle(true);
      const result = await getContents();
      console.log(result);
      navigate("/contents/list");
      if (result.status === 204) {
        toast("현재 잡을 수 있는 씨앗이 없습니다.", {
          icon: "🌼",
          style: {
            borderRadius: "10px",
            background: "#333",
            color: "#fff",
          },
        });
        navigate("/");
      } else if (result.status === 200) {
        setPetalData(result.data.data.petalInfos.reverse());
        setPetalSeq(result.data.data.dandelionSeq);
        navigate("/contents/list");
        setThrottle(false);
        setLoading(true);
      } else {
        navigate("/");
      }
      //const result = mocklist;
      //console.log(result);

      //setList(mocklist);
      //setSeq(2);
    } catch (error) {
      console.log(error);
    }

    console.log("swipe down");
  };

  const moveCreatePage = async () => {
    try {
      const result = await leftSeedCount();
      setIsShowing(true);
      if (result.data.leftSeedCount > 0) {
        console.log(result.data.leftSeedCount);
        console.log("action: swipe up");
        navigate("/contents/create");
      } else {
        toast("남은 씨앗 수가 없습니다.", {
          icon: "🌼",
          style: {
            borderRadius: "10px",
            background: "#333",
            color: "#fff",
          },
        });
      }
    } catch (error) {
      console.log(error);
    }
  };
  const handleTouchStart = (e: TouchEvent) => {
    howManyTouches = e.touches.length;
    if (howManyTouches > 1) {
      return;
    }
    xStart = e.touches[0].clientX;
    yStart = e.touches[0].clientY;
  };
  const handleTouchEnd = (e: TouchEvent) => {
    if (howManyTouches > 1) {
      return;
    }
    xEnd = e.changedTouches[0].clientX;
    yEnd = e.changedTouches[0].clientY;
    if (Math.abs(xEnd - xStart) < 30) {
      if (yEnd - yStart > 50 && !throttle) {
        moveListPage();
        //setIsShowing(false);
      } else if (yEnd - yStart < -50) {
        moveCreatePage();
      }
    }
  };

  const resetState = async () => {
    const response = await resetContentsState(patalseq);
    console.log(response);
    setPetalData([{}]);
    setPetalSeq(0);
  };

  const seedApi = async (pagemove: boolean) => {
    const Seedresult = await leftSeedCount();
    setSeedNum(Seedresult.data.leftSeedCount);
  };

  useEffect(() => {
    setTimeout(() => {
      setIsGuideShowing(true);
    }, 4000);
    setTimeout(() => {
      setIsGuideShowing(false);
    }, 10000);
    if (!localStorage.getItem("token")) {
      navigate("/login");
    }
    console.log(patalseq);
    if (patalseq !== 0) {
      resetState();
    }
    seedApi(false);

    window.addEventListener("touchstart", handleTouchStart);
    window.addEventListener("touchend", handleTouchEnd);

    return () => {
      window.removeEventListener("touchstart", handleTouchStart);
      window.removeEventListener("touchend", handleTouchEnd);
    };
  }, []);

  return (
    <section
      style={{
        width: "100%",
        height: "100vh",
        // minHeight: '100vh',
        minHeight: "-webkit-fill-available",
        overflow: "hidden",
      }}
      onClick={() => {
        if (!musicOn[0] && !member.soundOff) {
          // 브금이 아직 재생 안 되었고 member의 soundoff가 false여야 재생
          if (sound1) {
            play1();
            musicOn[0] = true;
          }
        }
      }}
    >
      {/* <h1>제발!!</h1> */}
      <button
        className={cx("menu-button")}
        onClick={(e) => {
          e.stopPropagation();
          play2()
          setIsGroupShowing((isGroupShowing) => !isGroupShowing);
        }}
      >
        <Menu className={cx("menu-svg")} />
      </button>
      <div className={cx('leftseed')}>
        <SeedIcon className={cx('leftseedicon')} width={30} height={30} />
        {seedNum} / 5
      </div>
      {isShowing && <LandingModel />}
      {isGroupShowing && (
        <GroupSelection setIsGroupShowing={setIsGroupShowing} />
      )}

      {isGuideShowing && (
        <>
          <div
            style={{
              height: "min(80px, 10vh)",
              position: "fixed",
              top: "15vh",
              left: "12px",
              objectFit: "contain",
              display: "flex",
            }}
          >
            <DownArrow
              className={`${cx("swipe-guide")} ${cx("swipe-guide__arrow2")}`}
              style={{ height: "100%", width: "auto" }}
            />
            <Tap
              className={`${cx("swipe-guide")} ${cx("swipe-guide__second")}`}
            />
          </div>
          <div
            style={{
              height: "min(80px, 10vh)",
              position: "fixed",
              bottom: "15vh",
              objectFit: "contain",
              left: "12px",
              display: "flex",
            }}
          >
            <UpArrow
              className={`${cx("swipe-guide")} ${cx("swipe-guide__arrow1")}`}
              style={{ height: "100%", width: "auto" }}
            />
            <Tap className={cx("swipe-guide")} />
          </div>
        </>
      )}
      <div
        className={cx('garden')}
        style={{}}
        onClick={(e) => {
          e.stopPropagation()
          navigate('/mygarden');
        }}
      >
        내 꽃밭
      </div>
      <button onClick={(e) => {
        e.stopPropagation();
        console.log('되는디...')
        console.log(document.querySelectorAll("audio"))
        console.log(sound1)
        sound1.mute(true)
      }} style={{position: "fixed", bottom: "10px", fontSize: "50px"}}>얍!</button>
      <button onClick={(e) => {
        e.stopPropagation();
        // console.log('되는디...')
        // console.log(document.querySelectorAll("audio"))
        // console.log(sound)
        sound1.mute(false)
      }} style={{position: "fixed", bottom: "150px", fontSize: "50px"}}>얍!</button>
      {/* <button onClick={(e) => {
        console.log('눌림')
        sound._muted = false
      }} style={{position: "fixed", bottom: "10px", fontSize: "50px", right: "10px"}}>호우!!</button> */}
    </section>
  );
};

export default LandingPage;
