import MyGardenPage from 'pages/MyGardenPage/MyGardenPage';
import SettingsPage from 'pages/SettingsPage/SettingsPage';
import MyAlbumPage from 'pages/MyAlbumPage/MyAlbumPage';
import MyCabinetPage from 'pages/MyCabinetPage/MyCabinetPage';
import { useRoutes } from 'react-router';
import LandingPage from 'pages/LandingPage/LandingPage';
import MainPage from 'pages/MainPage/MainPage';
import ContentsCreatePage from 'pages/ContentsCreatePage/ContentsCreatePage';
import ContentsListPage from 'pages/ContentsListPage/ContentsListPage';
import LoginPage from 'pages/LandingPage/LoginPage';
import SignupPage from 'pages/LandingPage/SignupPage';
import MyGardenDandelionDetail from 'containers/MyGarden/MyGardenDandelionDetail';
import Background from 'layouts/background';

export default function Router() {
  return useRoutes([
    {
      path: '/',
      element: <LandingPage />,
    },
    {
      path: '/main',
      element: <Background />,
      children: [{ element: <MainPage /> }],
    },
    {
      path: '/mygarden',
      element: <MyGardenPage />,
    },
    {
      path: '/settings',
      element: <SettingsPage />,
    },
    {
      path: '/mygarden/album',
      element: <MyAlbumPage />,
    },
    {
      path: '/mygarden/cabinet',
      element: <MyCabinetPage />,
    },
    {
      path: '/mygarden/dandelions/:id',
      element: <MyGardenDandelionDetail />,
    },
    {
      path: '/contents',
      element: <Background />,
      children: [
        { path: 'create', element: <ContentsCreatePage /> },
        { path: 'list', element: <ContentsListPage /> },
      ],
    },
    {
      path: '/login',
      element: <LoginPage />,
    },
    {
      path: '/signup',
      element: <SignupPage />,
    },
    {
      path: '/layout',
      element: <Background />,
    },
  ]);
}