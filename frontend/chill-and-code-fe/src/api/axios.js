import axios from 'axios'
import router from '@/router'

// Central axios instance for session-based auth
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor: JWT 토큰 자동 첨부
api.interceptors.request.use(
  (config) => {
    // 회원가입, 로그인, 토큰 재발급은 토큰 불필요
    const publicEndpoints = ['/users', '/users/login', '/auth/refresh']
    const isPublicEndpoint = publicEndpoints.some(endpoint => 
      config.url?.includes(endpoint) && config.method === 'post'
    )
    
    if (!isPublicEndpoint) {
      const token = sessionStorage.getItem('accessToken')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Refresh 중인지 추적하는 플래그 (무한 루프 방지)
let isRefreshing = false
let failedQueue = []

const processQueue = (error, token = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error)
    } else {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

// Response interceptor: 401 에러 처리 (토큰 재발급)
api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const originalRequest = err.config
    
    // Refresh API 자체가 실패한 경우 무한 루프 방지
    if (originalRequest.url?.includes('/auth/refresh')) {
      isRefreshing = false
      processQueue(err, null)
      
      // 로그아웃 처리
      sessionStorage.removeItem('accessToken')
      const currentPath = router.currentRoute.value.path
      if (currentPath !== '/login' && currentPath !== '/signup') {
        router.push('/login')
      }
      
      return Promise.reject(err)
    }
    
    // 401 Unauthorized: refresh 시도
    if (err?.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // 이미 refresh 중이면 대기열에 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        }).then(token => {
          originalRequest.headers.Authorization = `Bearer ${token}`
          return api(originalRequest)
        }).catch(err => {
          return Promise.reject(err)
        })
      }
      
      originalRequest._retry = true
      isRefreshing = true
      
      try {
        // /api/auth/refresh 호출 (withCredentials로 RT 쿠키 전송)
        const refreshRes = await api.post('/auth/refresh', null, { 
          withCredentials: true 
        })
        
        const newToken = refreshRes.data.data.accessToken
        
        if (!newToken) {
          throw new Error('Access Token을 받지 못했습니다.')
        }
        
        // sessionStorage에 새 토큰 저장
        sessionStorage.setItem('accessToken', newToken)
        
        // 대기 중인 요청들에 새 토큰 전파
        processQueue(null, newToken)
        
        // 원래 요청에 새 토큰 적용
        originalRequest.headers.Authorization = `Bearer ${newToken}`
        
        isRefreshing = false
        
        // 원래 요청 재시도
        return api(originalRequest)
        
      } catch (refreshError) {
        // refresh 실패: 로그아웃 처리
        console.error('Token refresh failed:', refreshError)
        processQueue(refreshError, null)
        isRefreshing = false
        
        sessionStorage.removeItem('accessToken')
        
        // 로그인/회원가입 페이지에서는 리다이렉트하지 않음
        const currentPath = router.currentRoute.value.path
        if (currentPath !== '/login' && currentPath !== '/signup') {
          router.push('/login')
        }
        
        return Promise.reject(refreshError)
      }
    }
    
    // simple error logging without mutation
    // eslint-disable-next-line no-console
    console.error('API error:', err?.response?.data || err.message)
    return Promise.reject(err)
  }
)

export default api

