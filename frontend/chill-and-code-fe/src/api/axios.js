import axios from 'axios'

// Central axios instance for session-based auth
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Minimal interceptors (optional): log response errors
api.interceptors.response.use(
  (res) => res,
  (err) => {
    // simple error logging without mutation
    // eslint-disable-next-line no-console
    console.error('API error:', err?.response?.data || err.message)
    return Promise.reject(err)
  }
)

export default api

