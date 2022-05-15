import { useState } from "react";
import { useNavigate } from "react-router-dom";
import classNames from "classnames/bind";
import styles from "./MyGardenDandelion.module.scss";
import cancel from "assets/images/cancel.png";
import photo from "assets/images/photo-album.png";
import shovel from "assets/images/shovel.png";
import pencil_check from "assets/images/pencil_check.png";
import sign from "assets/images/sign.png";
import axios from "axios";
import Swal from "sweetalert2";
const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;
function MyGardenDandelion({ dandelion }) {
  const navigate = useNavigate();
  const baseUrl = "http://localhost:8080/";
  const onDandelionClick = (dandelionId) => {
    // navigate(`/mygarden/dandelions/${dandelionId}`, { state: dandelionId });
    navigate(`/mygarden/dandelions/1`, { state: 1 });
  };
  const [show, setShow] = useState(false);
  const onOptionsClick = () => {
    setShow((prev) => !prev);
  };

  const onCancelClick = () => {
    setShow(false);
  };

  const onRecordClick = (dandelionId) => {
    Swal.fire({
      title: "꽃말을 입력하세요",
      input: "text",
      inputAttributes: {
        autocapitalize: "off",
      },
      showCancelButton: true,
      confirmButtonText: "등록",
      cancelButtonText: "취소",
      showLoaderOnConfirm: true,
    }).then((res) => {
      // registerDescription(dandelionId);
      if (res.isConfirmed) {
        registerDescription();
        Swal.fire({
          title: `[ ${res.value} ]를 등록하였습니다.`,
          confirmButtonText: "확인",
        });
      }
    });
  };

  const onAlbumClick = (dandelionId) => {
    Swal.fire({
      title: "보관함에 저장 하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "저장",
      cancelButtonText: "취소",
    }).then((res) => {
      if (res.isConfirmed) {
        saveDandelion();
        Swal.fire("보관함에 저장 성공!", "", "success");
      }
    });
  };

  const onDeleteClick = (dandelionId) => {
    Swal.fire({
      title: "민들레를 삭제 하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
    }).then((res) => {
      if (res.isConfirmed) {
        deleteDandelion();
        Swal.fire("민들레 삭제 완료!", "", "success");
      }
    });
  };

  async function registerDescription() {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      // url: `baseUrl/dandelions/{id}/description`, 나중에 아이디 있는거로 교체
      url: `dandelions/1/description`,
      method: "patch",
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        console.log("팻말 꽃말 등록 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("팻말 꽃말 등록 성공 실패");
        console.log(err);
      });
  }

  async function saveDandelion() {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      // url: `baseUrl/dandelions/{id}/status`, 나중에 아이디 있는거로 교체
      url: `dandelions/1/status`,
      method: "patch",
      data: {
        status: "ALBUM",
      },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        console.log("보관함에 저장 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("보관함에 저장 실패");
        console.log(err);
      });
  }

  async function deleteDandelion() {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      // url: `baseUrl/dandelions/{id}`, 나중에 아이디 있는거로 교체
      url: `dandelions/1`,
      method: "delete",
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        console.log("민들레 삭제 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("민들레 삭제 실패");
        console.log(err);
      });
  }

  return (
    <div className={cx("container")}>
      <div onClick={onOptionsClick}>
        {show ? (
          <div>
            <div>
              <span>
                <img className={cx("btn")} src={cancel} alt="취소" />
              </span>
              <span
                onClick={() => {
                  onRecordClick(1);
                }}
              >
                <img className={cx("btn")} src={pencil_check} alt="꽃말" />
              </span>
              <span
                onClick={() => {
                  onAlbumClick(1);
                }}
              >
                <img className={cx("btn")} src={photo} alt="보관함" />
              </span>
              <span
                onClick={() => {
                  onDeleteClick(1);
                }}
              >
                <img className={cx("btn")} src={shovel} alt="삭제" />
              </span>
            </div>
            <div className={cx("sign_form")}>
              <img className={cx("sign")} src={sign} alt="팻말" />
              {dandelion}
            </div>
          </div>
        ) : (
          <div className={cx("sign_form")}>
            <img className={cx("sign")} src={sign} alt="팻말" />
            {dandelion}
          </div>
        )}
      </div>
    </div>
  );
}
export default MyGardenDandelion;
