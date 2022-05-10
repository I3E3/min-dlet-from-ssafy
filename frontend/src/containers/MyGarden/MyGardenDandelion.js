import { useNavigate } from "react-router-dom";

function MyGardenDandelion({ dandelion }) {
  const navigate = useNavigate();
  const onDandelionClick = (dandelionId) => {
    // navigate(`/mygarden/dandelions/${dandelionId}`, { state: dandelionId });
    navigate(`/mygarden/dandelions/1`, { state: 1 });
  };

  return (
    <div>
      <div
        onClick={() => {
          onDandelionClick(dandelion.id);
        }}
      >
        {dandelion}
      </div>
    </div>
  );
}
export default MyGardenDandelion;
