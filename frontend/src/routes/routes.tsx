import { useRoutes } from 'react-router';
import LandingPage from 'pages/LandingPage/LandingPage';
import MainPage from 'pages/MainPage/MainPage';
import ContentsEditPage from 'pages/ContentsPage/ContentsEditPage';

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
        { path: 'create', element: <ContentsEditPage /> },
        { path: 'list', element: <ContentsEditPage /> },
        { path: 'write', element: <ContentsEditPage /> },
      ],
    },
  ]);
}
