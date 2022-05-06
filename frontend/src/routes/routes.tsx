import { useRoutes } from 'react-router';
import LandingPage from 'pages/LandingPage/LandingPage';
import MainPage from 'pages/MainPage/MainPage';
import ContentsCreatePage from 'pages/ContentsCreatePage/ContentsCreatePage';
import ContentsListPage from 'pages/ContentsListPage/ContentsListPage';

export default function Router() {
  return useRoutes([
    {
      path: '/',
      element: <LandingPage />,
    },
    {
      path: '/main',
      element: <MainPage />,
    },
    {
      path: '/contents',
      children: [
        { path: 'create', element: <ContentsCreatePage /> },
        { path: 'list', element: <ContentsListPage /> },
      ],
    },
  ]);
}
