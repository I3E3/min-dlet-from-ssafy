import { useQuery } from "react-query";
import { getGarden } from "services/api/MyGardenApi";

function MyGardenBody() {
  const { isLoading, data } = useQuery(["getGarden"], () => getGarden());
  return (
    <div>
      <div>
        <span>꽃밭 body</span>
      </div>
      <div>
        {isLoading ? <div>꽃밭을 불러오는 중 ...</div> : <div>{data}</div>}
      </div>

      <div>
        <button>팻말1</button>
      </div>
      <div>
        <button>팻말2</button>
      </div>
      <div>
        <button>팻말3</button>
      </div>
      <div>
        <button>팻말4</button>
      </div>
      <div>
        <button>팻말5</button>
      </div>
    </div>
  );
}
export default MyGardenBody;
