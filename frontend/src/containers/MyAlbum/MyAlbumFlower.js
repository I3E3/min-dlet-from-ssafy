import styles from "./MyAlbum.module.scss";
import classNames from "classnames/bind";

const cx = classNames.bind(styles);

function MyAlbumFlower({ dandelionSeq, description }) {

  return (
    <>
      <div className={cx('book__page__content__flower')}>
        <span>🌼꽃</span>
        <span>{description}</span>
        {/* <span>{dandelionSeq}</span> */}
      </div>
    </>

  );
}
export default MyAlbumFlower;
