import { useEffect } from 'react';

export default function Fullscreen () {
  document.fullscreenEnabled =
	document.fullscreenEnabled ||
	document.mozFullScreenEnabled ||
	document.documentElement.webkitRequestFullScreen;

  function requestFullscreen(element) {
    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullScreen) {
      element.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
    }
  }

  useEffect(()=> {
    if (document.fullscreenEnabled) {
      console.log("hello")
      requestFullscreen(document.documentElement);
    }
  }, [])
  return null
}