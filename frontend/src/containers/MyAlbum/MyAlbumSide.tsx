import styles from "./MyAlbum.module.scss";
import classNames from "classnames/bind";


const cx = classNames.bind(styles);

function MyAlbumSide() {

  return (
    <>
      <input style={{width: "20px", height: "20px", backgroundColor: "black"}} type="radio" value="good" name="page" id="page-2"/>
      <div className={cx('book__page')}>
        <span>내가 따로 저장한 꽃들</span>
        <div>
          <span>꽃</span>
        </div>
        <div>
          <span>꽃</span>
        </div>
        <div>
          <span>꽃</span>
        </div>
        <div>
          <span>꽃</span>
        </div>
        <span style={{position: "absolute", bottom: "15px", right: "20px"}}>아ㅓㅁㄴ임나</span>
      </div>
    </>

  );
}
export default MyAlbumSide;
