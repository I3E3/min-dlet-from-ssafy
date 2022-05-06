import React from "react";
import { useNavigate } from "react-router-dom";

function MyAlbumHeader() {
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
    <div>
      <span>앨범 header</span>
      <div>
        <div>
          <button onClick={onSettingsClick}>설정</button>
        </div>
        <div>
          <button onClick={onGardenClick}>꽃밭</button>
        </div>
        <div>
          <button onClick={onCabinetClick}>기록 보관함</button>
        </div>
      </div>
    </div>
  );
}

export default MyAlbumHeader;
