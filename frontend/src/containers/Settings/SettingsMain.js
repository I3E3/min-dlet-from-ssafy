import styled from "styled-components";
import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Settings.module.scss";
import classNames from "classnames/bind";
import garden from "assets/images/garden.png";
import pencil from "assets/images/pencil.png";
import album from "assets/images/photo-album.png";
import Swal from "sweetalert2";
import { useRecoilValue } from "recoil";
import memberState from "utils/memberState";
import { useSound } from "use-sound";
import ButtonEffect from "assets/musics/button_effect.wav";

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

const MemberForm = styled.div`
  display: flex;
`;

function SettingsMain() {
  // 변경해야함 recoil로
  const member = useRecoilValue(memberState);
  const [play] = useSound(ButtonEffect, {
    volume: 0.4,
    interrupt: true,
  });

  const soundOnOff = async (sound) => {
    const memberSeq = member.seq;
    await axios({
      url: `${BaseURL}/${memberSeq}/sound`,
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

  const navigate = useNavigate();
  const onGardenClick = () => {
    if (!member.soundOff) {
      play();
    }
    navigate(`/mygarden`);
  };

  const onAlbumClick = () => {
    if (!member.soundOff) {
      play();
    }
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    if (!member.soundOff) {
      play();
    }
    navigate(`/mygarden/cabinet`);
  };

  const onDeleteClick = () => {
    Swal.fire({
      title: "회원탈퇴 하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "탈퇴",
      cancelButtonText: "취소",
    }).then((res) => {
      if (res.isConfirmed) {
        deleteMember();
      }
    });
  };

  const onLogoutClick = () => {
    Swal.fire({
      title: "로그아웃 하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "아웃",
      cancelButtonText: "취소",
    }).then((res) => {
      if (res.isConfirmed) {
        if (localStorage.getItem("token")) {
          localStorage.removeItem("token");
          navigate("/");
        }
      }
    });
  };

  async function deleteMember() {
    const memberSeq = member.seq;
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      // 회원 seq받아와야함
      url: `members/${memberSeq}`,
      method: "delete",
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire("회원탈퇴 성공", "", "success");
        console.log("회원탈퇴 성공");
        navigate("/");
      })
      .catch((err) => {
        Swal.fire("회원탈퇴 실패", "", "success");
        console.log("회원탈퇴 실패");
        console.log(err);
      });
  }

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
        {/* <div>
          <button onClick={onCabinetClick}>
            <img className={cx("btn")} src={pencil} alt="기록보관함" />
          </button>
        </div> */}
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
              <label htmlFor="on" style={{ marginRight: "20px" }}>
                On
              </label>
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

          <MemberForm>
            <div className={cx("delete-btn-box")}>
              <button className={cx("delete-btn")} onClick={onLogoutClick}>
                로그아웃
              </button>
            </div>
            <div className={cx("delete-btn-box")}>
              <button className={cx("delete-btn")} onClick={onDeleteClick}>
                회원탈퇴
              </button>
            </div>
          </MemberForm>
        </div>
      </div>
    </div>
  );
}
export default SettingsMain;
