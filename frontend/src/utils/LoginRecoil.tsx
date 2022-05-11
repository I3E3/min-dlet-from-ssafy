import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router';
import memberState from 'utils/memberState';

const BaseURL = process.env.REACT_APP_BASE_URL

// 로그인하고 recoil state member를 만드는 함수
function LoginRecoil() {
  const navigate = useNavigate()
  const location = useLocation()
  const [, setMember] = useRecoilState(memberState)

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      if (!['/login', '/signup'].includes(location.pathname)) {
        navigate('login')
      }
      return
    } else {
      const checkData = {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }

      // 회원정보 조회
      fetch(`${BaseURL}members`, checkData)
        .then((res) => res.json())
        .then((result) => {
          if (result.status !== 200) {
            localStorage.removeItem('token')
            navigate('/login')
            return
          }
          const data = result.data
          setMember(data)
        })
        .catch((err) => {console.error(err)} )
  }}, [])


  return null;
}

export default LoginRecoil;