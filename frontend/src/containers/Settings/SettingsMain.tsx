import { useNavigate } from "react-router-dom";

function SettingsMain() {
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
        <span>Music : </span>
        <span>on/off</span>
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
export default SettingsMain;
