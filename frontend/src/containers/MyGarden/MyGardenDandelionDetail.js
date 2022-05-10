import axios from "axios";
import { useEffect } from "react";
import { useParams } from "react-router-dom";

function MyGardenDandelionDetail() {
  const { id } = useParams();
  const baseUrl = "http://localhost:8080/";
  const onDeleteDandelionClick = async () => {
    await axios({
      // url: `${baseUrl}/dandelions/${id}`,
      url: `${baseUrl}/dandelions/1`,
      method: "delete",
    })
      .then((res) => {
        console.log("민들레 상세보기 삭제 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("민들레 상세보기 삭제 실패");
        console.log(err);
      });
  };

  const onStoreDandelionClick = async () => {
    await axios({
      // url: `${baseUrl}/dandelions/${id}/status`,
      url: `${baseUrl}/dandelions/1/status`,
      method: "patch",
      data: {
        status: "ALBUM",
      },
    })
      .then((res) => {
        console.log("민들레 보관하기 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("민들레 보관하기 실패");
        console.log(err);
      });
  };

  const getDandelionDetail = async () => {
    await axios({
      // url: `baseUrl/dandelions/${id}`,
      url: `${baseUrl}/dandelions/1`,
      method: "get",
    })
      .then((res) => {
        console.log("민들레 상세보기 조회 성공");
        console.log(res.data);
      })
      .catch((err) => {
        console.log("민들레 상세보기 조회 실패");
        console.log(err);
      });
    getDandelionDetail();
  };
  useEffect(() => {
    // getDandelionDetail();
  }, []);
  return (
    <div>
      <div>민들레 상세보기</div>
      <div>
        <div onClick={onStoreDandelionClick}>간직하기</div>
        <div onClick={onDeleteDandelionClick}>삭제하기</div>
      </div>
    </div>
  );
}
export default MyGardenDandelionDetail;
