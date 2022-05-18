import sign from "assets/images/sign.png";
import classNames from "classnames/bind";
import styles from "./MyGardenNoDandelion.module.scss";

const cx = classNames.bind(styles);

function MyGardenNoDandelion() {
  return (
    <div className={cx("container")}>
      <div className={cx("sign-flower")}>
        <div className={cx("normal-sign")}>
          <img style={{ width: "100%" }} src={sign} alt="팻말" />
        </div>
      </div>
    </div>
  );
}
export default MyGardenNoDandelion;
