import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Settings.module.scss";
import classNames from "classnames/bind";
import garden from "assets/images/garden.png";
import pencil from "assets/images/pencil.png";
import album from "assets/images/photo-album.png";
const cx = classNames.bind(styles);

function SettingsMain() {
  const baseUrl = "http://localhost:8080/";
  // memberSeq는 atom에서 받아와야함
  const [memberSeq, setMemberSeq] = useState(0);
  const soundOnOff = async (sound: string) => {
    await axios({
      url: `${baseUrl}/${memberSeq}/sound`,
      method: "patch",
      data: {
        soundOff: sound,
      },
    })
      .then((response) => {
        console.log("소리설정 성공");
        console.log(response);
      })
      .catch((error) => {
        console.log("소리설정 에러");
        console.log(error);
      });
  };
  const changeLang = async (language: string) => {
    await axios({
      url: `${baseUrl}/${memberSeq}/language`,
      method: "patch",
      data: {
        language: language,
      },
    })
      .then((response) => {
        console.log("언어변경 성공");
        console.log(response);
      })
      .catch((error) => {
        console.log("언어변경 에러");
        console.log(error);
      });
  };

  const navigate = useNavigate();
  const onDeleteClick = () => {
    // 회원 탈퇴 로직

    // 탈퇴 후 홈으로 이동
    navigate("/");
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };

  return (
    <div className={cx("container")}>
      <div className={cx("btns")}>
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
        <div>
          <button onClick={onCabinetClick}>
            <img className={cx("btn")} src={pencil} alt="기록보관함" />
          </button>
        </div>
      </div>

      <div className={cx("inner-container")}>
        <div className={cx("settings-container")}>
          <div className={cx("title")}>
            <span>Settings</span>
          </div>
          <div className={cx("content-box")}>
            <div className={cx("sub-title")}>Music</div>
            <div className={cx("content")}>
              <input
                type="radio"
                name="sound"
                id="on"
                value="false"
                checked
                onChange={(e) => soundOnOff(e.target.value)}
              />
              <label htmlFor="on">On</label>
              <input
                type="radio"
                name="sound"
                id="off"
                value="true"
                onChange={(e) => soundOnOff(e.target.value)}
              />
              <label htmlFor="off">Off</label>
            </div>
          </div>
          <div className={cx("content-box")}>
            <div className={cx("sub-title")}>Languages</div>
            <div className={cx("content")}>
              <select
                name="languages"
                onChange={(e) => {
                  changeLang(e.target.value);
                }}
              >
                <option disabled selected>
                  languages
                </option>
                <option value="KOREAN">한국어</option>
                <option value="ENGLISH">English</option>
              </select>
            </div>
          </div>
          <div className={cx("delete-btn-box")}>
            <button className={cx("delete-btn")} onClick={onDeleteClick}>
              회원탈퇴
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
export default SettingsMain;
