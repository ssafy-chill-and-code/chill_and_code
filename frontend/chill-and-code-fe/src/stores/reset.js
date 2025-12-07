import { useUserStore } from './user'
import { useScheduleStore } from './schedule'

// 모든 Pinia store 상태를 초기화하는 유틸
export function resetAllStores() {
  const user = useUserStore()
  const schedule = useScheduleStore()

  user.$reset()
  schedule.$reset()
}

