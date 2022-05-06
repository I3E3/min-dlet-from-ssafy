import React from "react";
import { useNavigate } from "react-router-dom";

function MyCabinetHeader() {
  const navigate = useNavigate();

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };

  return (
    <div>
      <div>
        <span>기록 보관함header</span>
      </div>
      <div>
        <div>
          <button onClick={onSettingsClick}>설정</button>
        </div>
        <div>
          <button onClick={onGardenClick}>꽃밭</button>
        </div>
        <div>
          <button onClick={onAlbumClick}>앨범</button>
        </div>
      </div>
    </div>
  );
}

export default MyCabinetHeader;
