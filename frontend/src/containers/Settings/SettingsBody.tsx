import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function SettingsBody() {
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
  const navigate = useNavigate();
  const onDeleteClick = () => {
    // 회원 탈퇴 로직

    // 탈퇴 후 홈으로 이동
    navigate("/");
  };
  return (
    <div>
      <div>설정 페이지</div>
      <div>
        <span>Music</span>
        <div>
          <input
            type="radio"
            name="sound"
            id="on"
            value="false"
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
      <div>
        <span>Languages : </span>
        <select name="languages">
          <option disabled selected>
            languages
          </option>
          <option value="korean">한국어</option>
          <option value="english">English</option>
          <option value="chinese">中國語</option>
          <option value="japanese">日本語</option>
        </select>
      </div>
      <div>
        <button onClick={onDeleteClick}>회원탈퇴</button>
      </div>
    </div>
  );
}
export default SettingsBody;
