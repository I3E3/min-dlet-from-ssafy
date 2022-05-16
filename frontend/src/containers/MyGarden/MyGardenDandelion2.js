import styled from "styled-components";
import sign from "assets/images/sign.png";
import { useEffect, useState } from "react";
import classNames from "classnames/bind";
import styles from "./MyGardenDandelion.module.scss";
import cancel from "assets/images/cancel.png";
import photo from "assets/images/photo-album.png";
import flower_scissors from "assets/images/flower_scissors.png";
import pencil_check from "assets/images/pencil_check.png";
import axios from "axios";
import Swal from "sweetalert2";

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

const Sign = styled.div`
  color: white;
  font-size: 20px;
  position: relative;
  img {
    position: relative;
    height: 190px;
    width: 190px;
  }
`;
const IconBox = styled.div`
  display: flex;
  text-align: center;
`;

const IconCover = styled.div`
  display: flex;
  background-color: white;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  justify-content: center;
  align-items: center;
  margin-right: 5px;
`;

const Icons = styled.img`
  width: 30px !important;
  height: 30px !important;
`;

const Blank = styled.div`
  height: 40px;
`;

const Dday = styled.span`
  position: absolute;
  left: 23px;
  top: 23px;
  font-size: 15px;
`;

function MyGardenDandelion2({ dandelion }) {
  const [show, setShow] = useState(false);
  const [record, setRecord] = useState(false);
  const onOptionsClick = () => {
    setShow((prev) => !prev);
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
      if (res.isConfirmed) {
        registerDescription(dandelionId, res.value);
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
        saveDandelion(dandelionId);
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
        deleteDandelion(dandelionId);
      }
    });
  };

  async function registerDescription(dandelionId, description) {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      url: `dandelions/${dandelionId}/description`,
      method: "patch",
      data: { description: description },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire({
          title: `[ ${description} ]를 등록하였습니다.`,
          confirmButtonText: "확인",
        });
        console.log("팻말 꽃말 등록 성공");
      })
      .catch((err) => {
        Swal.fire({
          title: `꽃말 등록을 실패했습니다.`,
          confirmButtonText: "확인",
        });

        console.log("팻말 꽃말 등록 성공 실패");
        console.log(err);
      });
  }

  async function saveDandelion(dandelionId) {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      url: `dandelions/${dandelionId}/status`,
      method: "patch",
      data: {
        status: "ALBUM",
      },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire("보관함에 저장 성공!", "", "success");
        console.log("보관함에 저장 성공");
      })
      .catch((err) => {
        Swal.fire("보관함에 저장 실패!", "", "danger");
        console.log("보관함에 저장 실패");
        console.log(err);
      });
  }

  async function deleteDandelion(dandelionId) {
    const token = localStorage.getItem("token");
    const config = {
      Authorization: "Bearer " + token,
    };
    await axios({
      url: `dandelions/${dandelionId}`,
      method: "delete",
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire("민들레 삭제 완료!", "", "success");
        console.log("민들레 삭제 성공");
      })
      .catch((err) => {
        Swal.fire("민들레 삭제 실패!", "", "danger");
        console.log("민들레 삭제 실패");
        console.log(err);
      });
  }
  useEffect(() => {
    if (dandelion.status === "FLYING") {
      // setRecord(true);
      setRecord(false);
    } else if (dandelion.status === "RETURN") {
      setRecord(false);
    }
  }, []);
  return (
    <div className={cx("container")}>
      <div onClick={onOptionsClick}>
        {show ? (
          <div>
            <IconBox>
              <IconCover>
                <Icons src={cancel} alt="취소" />
              </IconCover>

              {record ? (
                <span>아직은 작성할 수 없습니다.</span>
              ) : (
                <IconCover
                  onClick={() => {
                    onRecordClick(dandelion.seq);
                  }}
                >
                  <Icons src={pencil_check} alt="꽃말" />
                </IconCover>
              )}
              <IconCover
                onClick={() => {
                  onAlbumClick(dandelion.seq);
                }}
              >
                <Icons src={photo} alt="보관함" />
              </IconCover>
              <IconCover
                onClick={() => {
                  onDeleteClick(dandelion.seq);
                }}
              >
                <Icons src={flower_scissors} alt="삭제" />
              </IconCover>
            </IconBox>
            <div>
              <Sign>
                <img src={sign} alt="팻말" />
                <Dday>{dandelion.blossomedDate}</Dday>
              </Sign>
            </div>
          </div>
        ) : (
          <div>
            <Blank></Blank>
            <Sign>
              <img src={sign} alt="팻말" />
              <Dday>{dandelion.blossomedDate}</Dday>
            </Sign>
          </div>
        )}
      </div>
    </div>
  );
}

export default MyGardenDandelion2;
