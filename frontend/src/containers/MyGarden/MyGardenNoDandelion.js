import styled from "styled-components";
import sign from "assets/images/sign.png";
import flower from "assets/images/flower.png";
import { useEffect, useState } from "react";
import classNames from "classnames/bind";
import styles from "./MyGardenDandelion.module.scss";
import cancel from "assets/images/cancel.png";
import photo from "assets/images/photo-album.png";
import shovel from "assets/images/shovel.png";
import flower_scissors from "assets/images/flower_scissors.png";
import pencil_check from "assets/images/pencil_check.png";
import axios from "axios";
import Swal from "sweetalert2";

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

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
