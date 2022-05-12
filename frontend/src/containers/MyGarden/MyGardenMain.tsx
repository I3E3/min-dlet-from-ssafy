import { useQuery } from "react-query";
import { getGarden } from "services/api/MyGardenApi";
import { useNavigate } from "react-router-dom";
import gear from "assets/images/gear.png";
import pencil from "assets/images/pencil.png";
import album from "assets/images/photo-album.png";
import styles from "./MyGarden.module.scss";
import classNames from "classnames/bind";
import { useEffect, useState } from "react";
import MyGardenDandelion from "./MyGardenDandelion";

const cx = classNames.bind(styles);

function MyGardenMain() {
  const navigate = useNavigate();
  // const { isLoading, data } = useQuery(["getGarden"], () => getGarden());
  // console.log(data);

  const [dandelions, setDandelions] = useState(["A", "B", "C", "D", "E"]);
  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };

  const onDandelionClick = () => {
    navigate(`/mygarden/`);
  };

  return (
    <div className={cx("container")}>
      <div>
        <span>꽃밭</span>
      </div>
      <div className={cx("btns")}>
        <div>
          <button type="button" onClick={onSettingsClick}>
            <img className={cx("btn")} src={gear} alt="설정" />
          </button>
        </div>
        <div>
          <button onClick={onAlbumClick}>
            <img className={cx("btn")} src={album} alt="앨범" />
          </button>
        </div>
        <div>
          <button onClick={onCabinetClick}>
            <img className={cx("btn")} src={pencil} alt="기록보관함" />
          </button>
        </div>
      </div>

      <div>
        {/* {isLoading ? <div>꽃밭을 불러오는 중 ...</div> : <div>{data}</div>} */}
      </div>

      <div>
        <span>민들레 꽃밭</span>
        {dandelions.map((dandelion, index) => (
          <MyGardenDandelion dandelion={dandelion} key={index} />
        ))}
      </div>
    </div>
  );
}
export default MyGardenMain;
