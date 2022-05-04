import { useRoutes } from "react-router";
import LandingPage from "pages/LandingPage/LandingPage";
import MainPage from "pages/MainPage/MainPage";
import MyGardenPage from "pages/MyGardenPage/MyGardenPage";
import SettingsPage from "pages/SettingsPage/SettingsPage";
import MyAlbumPage from "pages/MyAlbumPage/MyAlbumPage";
import MyCabinetPage from "pages/MyCabinetPage/MyCabinetPage";

export default function Router() {
  return useRoutes([
    {
      path: "/",
      element: <LandingPage />,
    },
    {
      path: "/main",
      element: <MainPage />,
    },
    {
      path: "/mygarden",
      element: <MyGardenPage />,
    },
    {
      path: "/settings",
      element: <SettingsPage />,
    },
    {
      path: "/mygarden/album",
      element: <MyAlbumPage />,
    },
    {
      path: "/mygarden/cabinet",
      element: <MyCabinetPage />,
    },
  ]);
}
