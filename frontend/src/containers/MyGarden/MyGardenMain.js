import { useNavigate } from "react-router-dom";
import gear from "assets/images/gear.png";
import pencil from "assets/images/pencil.png";
import album from "assets/images/photo-album.png";
import sign from "assets/images/sign.png";
import styles from "./MyGarden.module.scss";
import classNames from "classnames/bind";
import { useEffect, useState } from "react";
import MyGardenDandelion2 from "./MyGardenDandelion2";
import MyGardenDandelion3 from "./MyGardenDandelion3";
import axios from "axios";

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

function MyGardenMain() {
  const navigate = useNavigate();

  const [dandelions, setDandelions] = useState([]);
  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };

  async function getGarden() {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      url: `garden`,
      method: "get",
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        console.log("꽃밭 가져오기 성공");
        console.log(res.data.data);
        setDandelions(res.data.data);
      })
      .catch((err) => {
        console.log("꽃밭 가져오기 실패");
        console.log(err);
      });
  }

  useEffect(() => {
    getGarden();
  }, []);

  return (
    <div className={cx("container")}>
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

      <div className={cx("blank")}></div>

      <div className={cx("sign_boxs")}>
        <div className={cx("sign_box_1")}>
          {dandelions.slice(0, 1).map((dandelion, index) => (
            dandelion?
            <MyGardenDandelion3 dandelion={dandelion} key={index} />
          : null))}
          {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
        </div>
        <div className={cx("sign_box_2")}>
          {dandelions.slice(1, 2).map((dandelion, index) => (
            dandelion?
            <MyGardenDandelion3 dandelion={dandelion} key={index} />
          : null))}
          {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
        </div>
        <div className={cx("sign_box_3")}>
          {dandelions.slice(2, 3).map((dandelion, index) => (
            dandelion?
            <MyGardenDandelion3 dandelion={dandelion} key={index} />
          : null))}
          {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
        </div>
        <div className={cx("sign_box_4")}>
          {dandelions.slice(3, 4).map((dandelion, index) => (
            dandelion?
            <MyGardenDandelion3 dandelion={dandelion} key={index} />
          : null))}
          {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
        </div>
        <div className={cx("sign_box_5")}>
          {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            )
          ))}
          {dandelions.slice(4, 5).map((dandelion, index) => (
            dandelion?
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
          : null))}
        </div>
      </div>
    </div>
  );
}
export default MyGardenMain;
