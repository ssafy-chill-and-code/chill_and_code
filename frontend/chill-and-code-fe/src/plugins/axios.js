import axios from 'axios'

export function setupAxios(router) {
  axios.defaults.baseURL = 'http://localhost:8080/api'
  axios.defaults.withCredentials = true

  axios.interceptors.response.use(
    (res) => res,
    (error) => {
      if (error?.response?.status === 401) {
        alert('로그인이 필요합니다.')
        if (router) router.push('/login')
        else window.location.href = '/login'
      }
      return Promise.reject(error)
    },
  )
}

