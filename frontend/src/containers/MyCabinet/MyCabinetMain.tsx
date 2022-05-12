import { useNavigate } from "react-router-dom";
import styles from "./MyCabinet.module.scss";
import classNames from "classnames/bind";
import gear from "assets/images/gear.png";
import garden from "assets/images/garden.png";
import album from "assets/images/photo-album.png";
import Swal from "sweetalert2";
import axios from "axios";
const cx = classNames.bind(styles);

function MyCabinetMain() {
  const navigate = useNavigate();
  const baseUrl = "http://localhost:8080/";

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };

  const onRecordClick = (dandelionId: number) => {
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
        registerTag();
        Swal.fire({
          title: `[ ${res.value} ]를 등록하였습니다.`,
          confirmButtonText: "확인",
        });
      }
    });
  };

  // 태그
  const registerTag = async () => {
    await axios({
      // url: `baseUrl/dandelions/{id}/tags`, 나중에 아이디 있는거로 교체
      url: `${baseUrl}/dandelions/1/tags`,
      method: "patch",
    })
      .then((res) => {
        console.log("팻말 태그 등록 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("팻말 태그 등록 성공 실패");
        console.log(err);
      });
    registerTag();
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
        {/* <div onClick={()=>{onRecordClick(id)}}>꽃1</div> */}
        <div
          onClick={() => {
            onRecordClick(1);
          }}
        >
          꽃1
        </div>
      </div>
      <div>
        <div>꽃2</div>
      </div>
    </div>
  );
}
export default MyCabinetMain;
