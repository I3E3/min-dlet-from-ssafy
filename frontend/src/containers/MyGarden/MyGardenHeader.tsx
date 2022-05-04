import React from "react";
import { useNavigate } from "react-router-dom";

function MyGardenHeader() {
  const navigate = useNavigate();

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };

  return (
    <div>
      <span>꽃밭header</span>
      <div>
        <div>
          <button onClick={onSettingsClick}>설정</button>
        </div>
        <div>
          <button onClick={onAlbumClick}>앨범</button>
        </div>
        <div>
          <button onClick={onCabinetClick}>꽃 보관함</button>
        </div>
      </div>
    </div>
  );
}

export default MyGardenHeader;
