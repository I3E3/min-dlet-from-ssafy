import { useState } from 'react';
import { useNavigate } from "react-router-dom";
import styles from "./MyAlbum.module.scss";
import classNames from "classnames/bind";
import gear from "assets/images/gear.png";
import garden from "assets/images/garden.png";
import pencil from "assets/images/pencil.png";

const cx = classNames.bind(styles);

function MyAlbumMain() {
  const [sides, setSides] = useState([1, 2, 3]) // 앨범이 총 몇 페이지 있는지 저장
  const [nowSide, setNowSide] = useState(1) // 현재 앨범 페이지

  const handleNowSide = (side: number, isRightFlip: boolean) => {
    if (isRightFlip) {
      if (side < sides.length) {
        setNowSide(side + 1)
      } else {
        return
      }
    } else {
      if (side > 1) {
        setNowSide(side - 1)
      } else {
        return
      }
    }
  }

  const navigate = useNavigate();

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };
  return (
    <div className={cx("container")}>
      <div>
        <span>앨범</span>
      </div>
      <div className={cx("btns")}>
        <div>
          <button onClick={onSettingsClick}>
            <img className={cx("btn")} src={gear} alt="설정" />
          </button>
        </div>
        <div>
          <button onClick={onGardenClick}>
            <img className={cx("btn")} src={garden} alt="꽃밭" />
          </button>
        </div>
        <div>
          <button onClick={onCabinetClick}>
            <img className={cx("btn")} src={pencil} alt="기록보관함" />
          </button>
        </div>
      </div>

      {/* <div>
      </div> */}
      <div className={cx('cover')} style={{position: "relative", width: "80%"}}>
        <div className={cx('book')}>
          <input style={{width: "20px", height: "20px", backgroundColor: "black"}} type="radio" value="good" name="page" id="page-2"/>
          <div style={{zIndex: 2}} className={cx('book__page')}>
            <span>내가 따로 저장한 꽃들</span>
            <div style={{display: "grid", gridTemplateColumns: "1fr 1fr"}}>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>호잇</span>
              </div>
            </div>
          </div>

          <div className={cx('book__page')}>
            <span>내가 따로 저장한 꽃들</span>
            <div style={{display: "grid", gridTemplateColumns: "1fr 1fr"}}>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>헤이</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <span style={{position: "absolute", bottom: "15px", right: "20px"}}>아ㅓㅁㄴ임나</span>
            </div>
          </div>

        </div>
      </div>

    </div>
  );
}
export default MyAlbumMain;
