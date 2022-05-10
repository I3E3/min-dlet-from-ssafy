import { useNavigate } from "react-router-dom";
import styles from "./MyAlbum.module.scss";
import classNames from "classnames/bind";
import gear from "assets/images/gear.png";
import garden from "assets/images/garden.png";
import pencil from "assets/images/pencil.png";
const cx = classNames.bind(styles);

function MyAlbumMain() {
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

      <div>
        <span>내가 따로 저장한 꽃들</span>
      </div>
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
        <span>꽃</span>
      </div>
    </div>
  );
}
export default MyAlbumMain;
