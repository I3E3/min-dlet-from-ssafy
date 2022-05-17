import styled from "styled-components";
import sign from "assets/images/sign.png";
import classNames from "classnames/bind";
import styles from "./MyGardenDandelion.module.scss";

const cx = classNames.bind(styles);

const Sign = styled.div`
  color: white;
  font-size: 20px;
  position: relative;
  img {
    position: relative;
    height: 190px;
    width: 190px;
  }
`;

function MyGardenNoDandelion() {
  return (
    <div className={cx("container")}>
      <Sign>
        <img src={sign} alt="팻말" />
      </Sign>
    </div>
  );
}
export default MyGardenNoDandelion;
