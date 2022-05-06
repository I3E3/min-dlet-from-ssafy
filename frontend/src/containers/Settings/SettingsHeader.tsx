import React from "react";
import { useNavigate } from "react-router-dom";

function SettingsHeader() {
  const navigate = useNavigate();

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
    <div>
      <div>
        <span>설정header</span>
      </div>
      <div>
        <div>
          <button onClick={onGardenClick}> 꽃밭</button>
        </div>
        <div>
          <button onClick={onAlbumClick}>앨범</button>
        </div>
        <div>
          <button onClick={onCabinetClick}>기록 보관함</button>
        </div>
      </div>
    </div>
  );
}

export default SettingsHeader;
