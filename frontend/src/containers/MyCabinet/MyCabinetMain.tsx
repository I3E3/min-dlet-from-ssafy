import { useNavigate } from "react-router-dom";
import styles from "./MyCabinet.module.scss";
import classNames from "classnames/bind";
import gear from "assets/images/gear.png";
import garden from "assets/images/garden.png";
import album from "assets/images/photo-album.png";
const cx = classNames.bind(styles);

function MyCabinetMain() {
  const navigate = useNavigate();

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };
  return (
    <div className={cx("container")}>
      <div>
        <span>기록 보관함</span>
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
          <button onClick={onAlbumClick}>
            <img className={cx("btn")} src={album} alt="앨범" />
          </button>
        </div>
      </div>

      <div>
        <span>내가 작성한 모든 꽃 기록들</span>
      </div>
      <div>
        <span>꽃1</span>
      </div>
      <div>
        <span>꽃2</span>
      </div>
    </div>
  );
}
export default MyCabinetMain;
