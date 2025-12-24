<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostStore } from '../stores/post'
import { useCommentStore } from '../stores/comment'
import { useScheduleStore } from '../stores/schedule'
import { useThemeStore } from '../stores/theme'

const router = useRouter()
const userStore = useUserStore()
const postStore = usePostStore()
const commentStore = useCommentStore()
const scheduleStore = useScheduleStore()
const themeStore = useThemeStore()

// 다크모드 상태 (전역 store에서 가져오기)
const isDarkMode = computed(() => themeStore.isDarkMode)

const activeTab = ref('home')
const mobileMenuOpen = ref(false)
const loading = ref(false)
const error = ref(null)
const showProfileEditModal = ref(false)
const showPasswordChangeModal = ref(false)
const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const profileForm = ref({
  nickname: '',
  region: '',
  profileImageUrl: '',
})

const showWithdrawalModal = ref(false)
const withdrawalConfirm = ref(false)
const scheduleTypeFilter = ref(null)

// 파일 업로드 관련
const selectedFile = ref(null)
const previewUrl = ref(null)
const uploading = ref(false)
const fileInputRef = ref(null)

const user = computed(() => userStore.user)
const myPosts = computed(() => postStore.myPosts)
const myComments = computed(() => commentStore.myComments)
const schedules = computed(() => {
  if (!scheduleTypeFilter.value) return scheduleStore.schedules
  return scheduleStore.schedules.filter(s => s.type === scheduleTypeFilter.value)
})

const tabs = [
  { id: 'home', label: '홈', icon: 'home' },
  { id: 'posts', label: '내 게시글', icon: 'document' },
  { id: 'comments', label: '내 댓글', icon: 'chat' },
  { id: 'schedules', label: '내 일정', icon: 'calendar' },
  { id: 'settings', label: '계정 설정', icon: 'cog' },
  { id: 'withdrawal', label: '회원 탈퇴', icon: 'user-minus' },
]

// 아이콘 SVG 경로 정의
const getIconSvg = (iconName) => {
  const icons = {
    home: 'M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25',
    document: 'M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z',
    chat: 'M20.25 8.511c.884.284 1.5 1.128 1.5 2.097v4.286c0 1.136-.847 2.1-1.98 2.193-.34.027-.68.052-1.02.072v3.091l-3-3c-1.354 0-2.694-.055-4.02-.163a2.115 2.115 0 01-.825-.242m9.345-8.334a2.126 2.126 0 00-.476-.095 48.64 48.64 0 00-8.048 0c-1.131.094-1.976 1.057-1.976 2.192v4.286c0 .837.46 1.58 1.155 1.951m9.345-8.334V6.637c0-1.621-1.152-3.026-2.76-3.235A48.455 48.455 0 0011.25 3c-2.115 0-4.198.137-6.24.402-1.608.209-2.76 1.614-2.76 3.235v6.226c0 1.621 1.152 3.026 2.76 3.235.577.075 1.157.14 1.74.194V21l4.155-4.155',
    calendar: 'M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-9-9.75h.008v.008H12V9.75zm-3 0h.008v.008H9V9.75zm-3 0h.008v.008H6V9.75zm9 3h.008v.008H15v-.008zm-3 0h.008v.008H12v-.008zm-3 0h.008v.008H9v-.008zm9 3h.008v.008H15v-.008zm-3 0h.008v.008H12v-.008zm-3 0h.008v.008H9v-.008z',
    cog: 'M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.24-.438.613-.431.992a6.759 6.759 0 010 .255c-.007.378.138.75.43.99l1.005.828c.424.35.534.954.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.992a6.932 6.932 0 010-.255c.007-.378-.138-.75-.43-.99l-1.004-.828a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.281z',
    'user-minus': 'M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632zM13.5 10.5h-3v6h3v-6z'
  }
  return icons[iconName] || ''
}

const regions = [
  '서울', '부산', '대구', '인천', '광주', '대전', '울산', '세종',
  '경기', '강원', '충북', '충남', '전북', '전남', '경북', '경남', '제주'
]

const scheduleTypes = [
  { value: 'WORK', label: '업무', color: 'bg-blue-500' },
  { value: 'PERSONAL', label: '개인', color: 'bg-green-500' },
  { value: 'WORKATION', label: '워케이션', color: 'bg-purple-500' },
]

const changeTab = (tabId) => {
  activeTab.value = tabId
  mobileMenuOpen.value = false
  
  if (tabId === 'posts' && myPosts.value.length === 0) loadMyPosts()
  else if (tabId === 'comments' && myComments.value.length === 0) loadMyComments()
  else if (tabId === 'schedules' && schedules.value.length === 0) loadMySchedules()
}

const loadMyPosts = async () => {
  loading.value = true
  error.value = null
  try {
    await postStore.fetchMyPosts()
  } catch (e) {
    error.value = '게시글을 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

const loadMyComments = async () => {
  loading.value = true
  error.value = null
  try {
    await commentStore.fetchMyComments()
  } catch (e) {
    error.value = '댓글을 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

const loadMySchedules = async () => {
  loading.value = true
  error.value = null
  try {
    const now = new Date()
    const month = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    await scheduleStore.fetchSchedules(month)
  } catch (e) {
    error.value = '일정을 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

const openProfileEdit = () => {
  profileForm.value.nickname = user.value?.nickname || ''
  profileForm.value.region = user.value?.region || ''
  profileForm.value.profileImageUrl = user.value?.profileImageUrl || ''
  selectedFile.value = null
  previewUrl.value = null
  showProfileEditModal.value = true
}

const openPasswordChange = () => {
  passwordForm.value.currentPassword = ''
  passwordForm.value.newPassword = ''
  passwordForm.value.confirmPassword = ''
  showPasswordChangeModal.value = true
}

const handleFileSelect = (event) => {
  const file = event.target.files?.[0]
  if (!file) return

  // 파일 타입 검증
  const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']
  if (!allowedTypes.includes(file.type)) {
    alert('이미지 파일만 업로드 가능합니다. (JPEG, PNG, WebP)')
    return
  }

  // 파일 크기 검증 (10MB 제한)
  if (file.size > 10 * 1024 * 1024) {
    alert('파일 크기는 10MB 이하여야 합니다.')
    return
  }

  selectedFile.value = file
  previewUrl.value = URL.createObjectURL(file)
}

const uploadFile = async () => {
  if (!selectedFile.value) return

  uploading.value = true
  error.value = null
  try {
    const response = await userStore.uploadFile(selectedFile.value)
    if (response?.data?.url) {
      profileForm.value.profileImageUrl = response.data.url
      selectedFile.value = null
      if (previewUrl.value) {
        URL.revokeObjectURL(previewUrl.value)
        previewUrl.value = null
      }
      alert('이미지가 업로드되었습니다.')
    } else {
      throw new Error('업로드된 파일의 URL을 받지 못했습니다.')
    }
  } catch (e) {
    const errorMessage = e?.response?.data?.message || '파일 업로드에 실패했습니다.'
    error.value = errorMessage
    alert(errorMessage)
  } finally {
    uploading.value = false
  }
}

const saveProfile = async () => {
  loading.value = true
  error.value = null
  try {
    // 빈 문자열을 trim하고, 빈 문자열이면 null로 변환
    const payload = {
      nickname: profileForm.value.nickname?.trim() || null,
      region: profileForm.value.region?.trim() || null,
      profileImageUrl: profileForm.value.profileImageUrl?.trim() || null,
    }
    
    // 최소한 하나의 필드는 값이 있어야 함
    if (!payload.nickname && !payload.region && !payload.profileImageUrl) {
      alert('수정할 정보를 입력해주세요.')
      loading.value = false
      return
    }
    
    await userStore.updateProfile(payload)
    
    showProfileEditModal.value = false
    // 미리보기 URL 정리
    if (previewUrl.value) {
      URL.revokeObjectURL(previewUrl.value)
      previewUrl.value = null
    }
    selectedFile.value = null
    alert('프로필이 수정되었습니다.')
  } catch (e) {
    const errorMessage = e?.response?.data?.message || '프로필 수정에 실패했습니다.'
    error.value = errorMessage
    alert(errorMessage)
  } finally {
    loading.value = false
  }
}

const savePassword = async () => {
  // 유효성 검사
  if (!passwordForm.value.currentPassword || !passwordForm.value.newPassword) {
    alert('모든 필드를 입력해주세요.')
    return
  }
  
  if (passwordForm.value.newPassword.length < 8) {
    alert('새 비밀번호는 8자 이상이어야 합니다.')
    return
  }
  
  if (passwordForm.value.newPassword.includes(' ')) {
    alert('새 비밀번호에 공백을 포함할 수 없습니다.')
    return
  }
  
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.')
    return
  }
  
  if (passwordForm.value.currentPassword === passwordForm.value.newPassword) {
    alert('현재 비밀번호와 새 비밀번호가 동일합니다.')
    return
  }
  
  loading.value = true
  error.value = null
  try {
    await userStore.changePassword({
      currentPassword: passwordForm.value.currentPassword,
      newPassword: passwordForm.value.newPassword,
    })
    showPasswordChangeModal.value = false
    passwordForm.value.currentPassword = ''
    passwordForm.value.newPassword = ''
    passwordForm.value.confirmPassword = ''
    
    // 비밀번호 변경 성공 안내
    alert('비밀번호가 변경되었습니다. 보안을 위해 다시 로그인해주세요.')
    
    // 비밀번호 변경 시 Refresh Token이 이미 무효화되었으므로
    // 서버 logout API 호출 없이 로컬 인증 상태만 정리
    userStore.clearLocalAuth()
    router.push('/login')
  } catch (e) {
    const errorMessage = e?.response?.data?.message || '비밀번호 변경에 실패했습니다.'
    error.value = errorMessage
    alert(errorMessage)
  } finally {
    loading.value = false
  }
}

const deletePost = async (postId) => {
  if (!confirm('이 게시글을 삭제하시겠습니까?')) return
  try {
    await postStore.deletePost(postId)
    await loadMyPosts()
    alert('게시글이 삭제되었습니다.')
  } catch (e) {
    alert('게시글 삭제에 실패했습니다.')
  }
}

const deleteComment = async (commentId) => {
  if (!confirm('이 댓글을 삭제하시겠습니까?')) return
  try {
    await commentStore.deleteComment(commentId)
    await loadMyComments()
    alert('댓글이 삭제되었습니다.')
  } catch (e) {
    alert('댓글 삭제에 실패했습니다.')
  }
}

const confirmWithdrawal = async () => {
  if (!withdrawalConfirm.value) {
    alert('회원 탈퇴 확인 체크박스를 선택해주세요.')
    return
  }
  if (!confirm('정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) return
  
  try {
    await userStore.$axios?.delete('/users/me')
    alert('회원 탈퇴가 완료되었습니다.')
    userStore.user = null
    userStore.isLoggedIn = false
    userStore.accessToken = null
    sessionStorage.removeItem('accessToken')
    router.push('/')
  } catch (e) {
    alert('회원 탈퇴에 실패했습니다.')
  }
}

onMounted(async () => {
  loading.value = true
  try {
    await userStore.fetchProfile()
  } catch (e) {
    router.push('/login')
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div 
    :class="[
      'min-h-screen relative transition-colors duration-300',
      isDarkMode 
        ? 'dark' 
        : ''
    ]"
    :style="{
      backgroundColor: isDarkMode ? '#0f172a' : '#f0f4f8'
    }"
  >
    <!-- 통일된 배경 패턴 -->
    <div 
      class="fixed inset-0 pointer-events-none"
      :style="{
        backgroundImage: isDarkMode
          ? 'radial-gradient(circle at 2px 2px, rgba(99, 102, 241, 0.15) 1px, transparent 0), radial-gradient(circle at 20px 20px, rgba(139, 92, 246, 0.1) 1px, transparent 0)'
          : 'radial-gradient(circle at 2px 2px, rgba(99, 102, 241, 0.08) 1px, transparent 0), radial-gradient(circle at 20px 20px, rgba(139, 92, 246, 0.05) 1px, transparent 0)',
        backgroundSize: '40px 40px, 60px 60px',
        backgroundPosition: '0 0, 20px 20px'
      }"
    ></div>
    <!-- 미묘한 그라데이션 오버레이 (글래스 효과 강조용) -->
    <div 
      class="fixed inset-0 pointer-events-none opacity-40"
      :style="{
        background: isDarkMode
          ? 'radial-gradient(ellipse at top, rgba(99, 102, 241, 0.1) 0%, transparent 50%), radial-gradient(ellipse at bottom, rgba(139, 92, 246, 0.1) 0%, transparent 50%)'
          : 'radial-gradient(ellipse at top, rgba(99, 102, 241, 0.05) 0%, transparent 50%), radial-gradient(ellipse at bottom, rgba(139, 92, 246, 0.05) 0%, transparent 50%)'
      }"
    ></div>
    
    <div class="flex relative z-10">
      <!-- Desktop Sidebar -->
      <aside class="hidden lg:block w-80 border-r border-white/20 fixed left-0 top-16 bottom-0 overflow-y-auto glass-card">
        <div class="p-10">
          <h2 
            :class="[
              'text-2xl font-bold mb-10 transition-colors',
              isDarkMode ? 'text-white' : 'text-gray-900'
            ]"
          >
            마이페이지
          </h2>
          <nav class="space-y-3">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="changeTab(tab.id)"
              :class="[
                'w-full text-left px-6 py-4 rounded-xl transition-all text-base font-medium flex items-center gap-3',
                activeTab === tab.id
                  ? 'glass-button-primary text-white shadow-lg'
                  : isDarkMode
                    ? 'glass-button-secondary text-gray-200 hover:text-white'
                    : 'text-gray-700 hover:bg-gray-100'
              ]"
            >
              <svg 
                class="w-5 h-5 flex-shrink-0" 
                fill="none" 
                stroke="currentColor" 
                viewBox="0 0 24 24"
                :class="activeTab === tab.id ? 'stroke-current' : 'stroke-gray-600'"
              >
                <path 
                  stroke-linecap="round" 
                  stroke-linejoin="round" 
                  stroke-width="2" 
                  :d="getIconSvg(tab.icon)"
                />
              </svg>
              <span>{{ tab.label }}</span>
            </button>
          </nav>
        </div>
      </aside>

      <!-- Mobile Menu Button -->
      <button
        @click="mobileMenuOpen = !mobileMenuOpen"
        class="glass-button-primary lg:hidden fixed bottom-8 right-8 w-16 h-16 rounded-full flex items-center justify-center z-30 transition-all"
      >
        <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
        </svg>
      </button>

      <!-- Mobile Drawer -->
      <div 
        v-if="mobileMenuOpen" 
        class="fixed inset-0 bg-black/30 backdrop-blur-sm z-40 lg:hidden"
        @click="mobileMenuOpen = false"
      ></div>
      <div 
        :class="[
          'glass-drawer fixed top-0 left-0 bottom-0 w-80 shadow-2xl transform transition-transform duration-300 z-50 lg:hidden overflow-y-auto',
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        ]"
      >
        <div 
          :class="[
            'p-8 border-b flex items-center justify-between transition-colors',
            isDarkMode ? 'border-gray-700' : 'border-gray-200'
          ]"
        >
          <h2 
            :class="[
              'text-xl font-bold transition-colors',
              isDarkMode ? 'text-white' : 'text-gray-900'
            ]"
          >
            마이페이지
          </h2>
          <button 
            @click="mobileMenuOpen = false"
            class="glass-button-icon p-2 rounded-lg transition-colors"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>
        <nav class="p-8 space-y-3">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="changeTab(tab.id)"
            :class="[
              'w-full text-left px-6 py-4 rounded-xl transition-all text-base font-medium flex items-center gap-3',
              activeTab === tab.id
                ? 'glass-button-primary text-white shadow-lg'
                : isDarkMode
                  ? 'glass-button-secondary text-gray-200 hover:text-white'
                  : 'text-gray-700 hover:bg-gray-100'
            ]"
          >
            <svg 
              class="w-5 h-5 flex-shrink-0" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
              :class="activeTab === tab.id ? 'stroke-current' : 'stroke-gray-600'"
            >
              <path 
                stroke-linecap="round" 
                stroke-linejoin="round" 
                stroke-width="2" 
                :d="getIconSvg(tab.icon)"
              />
            </svg>
            <span>{{ tab.label }}</span>
          </button>
        </nav>
      </div>

      <!-- Main Content -->
      <main class="flex-1 lg:ml-80">
        <!-- Error Message -->
        <div v-if="error" class="p-8 lg:p-12">
          <div class="glass-error max-w-4xl mx-auto p-4 border-l-4 border-red-500 rounded-xl text-red-700 text-sm">
            {{ error }}
          </div>
        </div>

        <!-- Home Tab -->
        <section v-show="activeTab === 'home'" class="p-8 lg:p-12">
          <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
            <div 
              :class="[
                'animate-spin rounded-full h-12 w-12 border-4 transition-colors',
                isDarkMode 
                  ? 'border-gray-700 border-t-indigo-400' 
                  : 'border-gray-200 border-t-indigo-600'
              ]"
            ></div>
            <span 
              :class="[
                'font-medium transition-colors',
                isDarkMode ? 'text-gray-300' : 'text-gray-600'
              ]"
            >
              불러오는 중...
            </span>
          </div>

          <div v-else class="max-w-4xl mx-auto space-y-6">
            <!-- Profile Card -->
            <div class="glass-card p-6 lg:p-8">
              <h2 
                :class="[
                  'text-2xl font-bold mb-6 transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                프로필
              </h2>
              
              <div class="flex flex-col lg:flex-row items-center lg:items-start gap-6 mb-6">
                <div class="relative flex-shrink-0">
                  <img
                    v-if="user?.profileImageUrl"
                    :src="user.profileImageUrl"
                    :alt="user?.nickname || '프로필'"
                    class="w-20 h-20 rounded-full object-cover shadow-lg border-2 border-gray-200"
                  />
                  <div
                    v-else
                    class="w-20 h-20 bg-gradient-to-br from-indigo-600 to-indigo-800 rounded-full flex items-center justify-center text-white text-3xl font-bold shadow-lg"
                  >
                    {{ user?.nickname?.charAt(0)?.toUpperCase() || 'U' }}
                  </div>
                </div>
                <div class="flex-1 text-center lg:text-left">
                  <h3 
                    :class="[
                      'text-2xl font-bold mb-2 transition-colors',
                      isDarkMode ? 'text-white' : 'text-gray-900'
                    ]"
                  >
                    {{ user?.nickname || '사용자' }}
                  </h3>
                  <p 
                    :class="[
                      'mb-1 transition-colors',
                      isDarkMode ? 'text-gray-300' : 'text-gray-600'
                    ]"
                  >
                    {{ user?.email }}
                  </p>
                  <p 
                    :class="[
                      'text-sm transition-colors',
                      isDarkMode ? 'text-gray-400' : 'text-gray-500'
                    ]"
                  >
                    {{ user?.region || '지역 정보 없음' }}
                  </p>
                </div>
              </div>

              <button
                @click="openProfileEdit"
                class="glass-button-primary w-full lg:w-auto px-6 py-3 rounded-xl transition-all font-semibold"
              >
                프로필 수정
              </button>
            </div>

            <!-- Stats (Coming Soon) -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="glass-card p-6 text-center opacity-40">
                <div 
                  :class="[
                    'text-xs mb-1 font-medium transition-colors',
                    isDarkMode ? 'text-gray-400' : 'text-gray-500'
                  ]"
                >
                  워케이션 생성
                </div>
                <div 
                  :class="[
                    'text-lg font-bold transition-colors',
                    isDarkMode ? 'text-gray-500' : 'text-gray-400'
                  ]"
                >
                  준비중
                </div>
              </div>
              <div class="glass-card p-6 text-center opacity-40">
                <div 
                  :class="[
                    'text-xs mb-1 font-medium transition-colors',
                    isDarkMode ? 'text-gray-400' : 'text-gray-500'
                  ]"
                >
                  추천 이용
                </div>
                <div 
                  :class="[
                    'text-lg font-bold transition-colors',
                    isDarkMode ? 'text-gray-500' : 'text-gray-400'
                  ]"
                >
                  준비중
                </div>
              </div>
              <div class="glass-card p-6 text-center opacity-40">
                <div 
                  :class="[
                    'text-xs mb-1 font-medium transition-colors',
                    isDarkMode ? 'text-gray-400' : 'text-gray-500'
                  ]"
                >
                  워케이션 스타일
                </div>
                <div 
                  :class="[
                    'text-lg font-bold transition-colors',
                    isDarkMode ? 'text-gray-500' : 'text-gray-400'
                  ]"
                >
                  준비중
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- My Posts Tab -->
        <section v-show="activeTab === 'posts'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-8">
              <h2 
                :class="[
                  'text-3xl font-bold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                내 게시글
              </h2>
              <button
                @click="$router.push('/posts')"
                class="glass-button-primary w-full lg:w-auto px-6 py-3 rounded-xl transition-all font-semibold"
              >
                커뮤니티 둘러보기
              </button>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div 
                :class="[
                  'animate-spin rounded-full h-12 w-12 border-4 transition-colors',
                  isDarkMode 
                    ? 'border-gray-700 border-t-indigo-400' 
                    : 'border-gray-200 border-t-indigo-600'
                ]"
              ></div>
              <span 
                :class="[
                  'font-medium transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                불러오는 중...
              </span>
            </div>

            <div v-else-if="myPosts.length === 0" class="glass-card p-16 text-center">
              <h3 
                :class="[
                  'text-2xl font-bold mb-3 transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                작성한 게시글이 없습니다
              </h3>
              <p 
                :class="[
                  'mb-8 text-lg transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                첫 게시글을 작성해보세요!
              </p>
              <button
                @click="$router.push('/posts')"
                class="glass-button-primary px-8 py-4 rounded-xl transition-all font-semibold"
              >
                게시판 둘러보기
              </button>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="post in myPosts"
                :key="post.postId"
                class="glass-card p-6 hover:shadow-xl transition-all cursor-pointer"
                @click="$router.push(`/posts/${post.postId}`)"
              >
                <h3 
                  :class="[
                    'text-lg font-bold mb-2 transition-colors',
                    isDarkMode ? 'text-white' : 'text-gray-900'
                  ]"
                >
                  {{ post.title }}
                </h3>
                <p 
                  :class="[
                    'mb-3 line-clamp-2 leading-relaxed text-sm transition-colors',
                    isDarkMode ? 'text-gray-300' : 'text-gray-600'
                  ]"
                >
                  {{ post.content }}
                </p>
                <div class="flex flex-wrap items-center justify-between gap-4">
                  <div 
                    :class="[
                      'flex items-center gap-3 text-xs transition-colors',
                      isDarkMode ? 'text-gray-400' : 'text-gray-500'
                    ]"
                  >
                    <span>{{ post.region || '지역 미지정' }}</span>
                    <span>•</span>
                    <span>조회 {{ post.viewCount || 0 }}</span>
                    <span>•</span>
                    <span>{{ new Date(post.createdAt).toLocaleDateString() }}</span>
                  </div>
                  <button
                    @click.stop="deletePost(post.postId)"
                    class="px-4 py-1.5 text-red-600 hover:bg-red-50 rounded-lg transition-colors font-semibold text-sm"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- My Comments Tab -->
        <section v-show="activeTab === 'comments'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <h2 
              :class="[
                'text-3xl font-bold mb-8 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              내 댓글
            </h2>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div 
                :class="[
                  'animate-spin rounded-full h-12 w-12 border-4 transition-colors',
                  isDarkMode 
                    ? 'border-gray-700 border-t-indigo-400' 
                    : 'border-gray-200 border-t-indigo-600'
                ]"
              ></div>
              <span 
                :class="[
                  'font-medium transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                불러오는 중...
              </span>
            </div>

            <div v-else-if="myComments.length === 0" class="glass-card p-16 text-center">
              <h3 
                :class="[
                  'text-2xl font-bold mb-3 transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                작성한 댓글이 없습니다
              </h3>
              <p 
                :class="[
                  'text-lg transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                커뮤니티에서 활동해보세요!
              </p>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="comment in myComments"
                :key="comment.commentId"
                class="glass-card p-6 hover:shadow-xl transition-all"
              >
                <p 
                  :class="[
                    'mb-3 leading-relaxed transition-colors',
                    isDarkMode ? 'text-gray-200' : 'text-gray-900'
                  ]"
                >
                  {{ comment.content }}
                </p>
                <div class="flex items-center justify-between gap-4">
                  <span 
                    :class="[
                      'text-xs transition-colors',
                      isDarkMode ? 'text-gray-400' : 'text-gray-500'
                    ]"
                  >
                    {{ new Date(comment.createdAt).toLocaleDateString() }}
                  </span>
                  <div class="flex items-center gap-3">
                    <button
                      v-if="comment.postId"
                      @click="$router.push(`/posts/${comment.postId}`)"
                      :class="[
                        'text-xs font-semibold transition-colors',
                        isDarkMode ? 'text-slate-300 hover:text-white' : 'text-slate-700 hover:text-slate-900'
                      ]"
                    >
                      원문 보기 →
                    </button>
                    <button
                      @click="deleteComment(comment.commentId)"
                      class="px-4 py-1.5 text-red-600 hover:bg-red-50 rounded-lg transition-colors font-semibold text-sm"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- My Schedules Tab -->
        <section v-show="activeTab === 'schedules'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-8">
              <h2 
                :class="[
                  'text-3xl font-bold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                내 일정
              </h2>
              <button
                @click="$router.push('/schedule')"
                class="glass-button-primary w-full lg:w-auto px-6 py-3 rounded-xl transition-all font-semibold"
              >
                일정 캘린더로 이동
              </button>
            </div>

            <!-- Filter -->
            <div class="mb-6 flex gap-2 flex-wrap">
              <button
                @click="scheduleTypeFilter = null"
                :class="[
                  'px-5 py-2 rounded-xl font-semibold transition-all text-sm',
                  scheduleTypeFilter === null
                    ? 'glass-button-primary text-white shadow-lg'
                    : isDarkMode
                      ? 'glass-button-secondary text-gray-200 hover:text-white'
                      : 'glass-button-secondary text-gray-700 hover:text-gray-900'
                ]"
              >
                전체
              </button>
              <button
                v-for="type in scheduleTypes"
                :key="type.value"
                @click="scheduleTypeFilter = type.value"
                :class="[
                  'px-5 py-2 rounded-xl font-semibold transition-all text-sm',
                  scheduleTypeFilter === type.value
                    ? 'glass-button-primary text-white shadow-lg'
                    : isDarkMode
                      ? 'glass-button-secondary text-gray-200 hover:text-white'
                      : 'glass-button-secondary text-gray-700 hover:text-gray-900'
                ]"
              >
                {{ type.label }}
              </button>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div 
                :class="[
                  'animate-spin rounded-full h-12 w-12 border-4 transition-colors',
                  isDarkMode 
                    ? 'border-gray-700 border-t-indigo-400' 
                    : 'border-gray-200 border-t-indigo-600'
                ]"
              ></div>
              <span 
                :class="[
                  'font-medium transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                불러오는 중...
              </span>
            </div>

            <div v-else-if="schedules.length === 0" class="glass-card p-16 text-center">
              <h3 
                :class="[
                  'text-2xl font-bold mb-3 transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                등록된 일정이 없습니다
              </h3>
              <p 
                :class="[
                  'mb-8 text-lg transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                새로운 일정을 추가해보세요!
              </p>
              <button
                @click="$router.push('/schedule')"
                class="glass-button-primary px-8 py-4 rounded-xl transition-all font-semibold"
              >
                일정 추가하기
              </button>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="schedule in schedules"
                :key="schedule.scheduleId"
                class="glass-card p-6 hover:shadow-xl transition-all"
              >
                <div class="flex items-start gap-5">
                  <div 
                    :class="[
                      'w-1.5 h-16 rounded-full flex-shrink-0',
                      scheduleTypes.find(t => t.value === schedule.type)?.color || 'bg-gray-400'
                    ]"
                  ></div>
                  <div class="flex-1">
                    <span 
                      :class="[
                        'inline-block px-3 py-1 rounded-lg text-xs font-semibold text-white mb-2',
                        scheduleTypes.find(t => t.value === schedule.type)?.color || 'bg-gray-400'
                      ]"
                    >
                      {{ scheduleTypes.find(t => t.value === schedule.type)?.label || schedule.type }}
                    </span>
                    <h3 
                      :class="[
                        'text-lg font-bold mb-2 transition-colors',
                        isDarkMode ? 'text-white' : 'text-gray-900'
                      ]"
                    >
                      {{ schedule.title }}
                    </h3>
                    <p 
                      :class="[
                        'mb-2 leading-relaxed text-sm transition-colors',
                        isDarkMode ? 'text-gray-300' : 'text-gray-600'
                      ]"
                    >
                      {{ schedule.description }}
                    </p>
                    <p 
                      :class="[
                        'text-xs transition-colors',
                        isDarkMode ? 'text-gray-400' : 'text-gray-500'
                      ]"
                    >
                      {{ schedule.startDate }} ~ {{ schedule.endDate }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Settings Tab -->
        <section v-show="activeTab === 'settings'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <h2 
              :class="[
                'text-3xl font-bold mb-8 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              계정 설정
            </h2>

            <div class="glass-card p-6 lg:p-8">
              <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
                <div>
                  <h3 
                    :class="[
                      'text-xl font-bold mb-2 transition-colors',
                      isDarkMode ? 'text-white' : 'text-gray-900'
                    ]"
                  >
                    비밀번호 변경
                  </h3>
                  <p 
                    :class="[
                      'transition-colors',
                      isDarkMode ? 'text-gray-300' : 'text-gray-600'
                    ]"
                  >
                    계정의 비밀번호를 변경합니다
                  </p>
                </div>
                <button
                  @click="openPasswordChange"
                  class="glass-button-primary px-6 py-3 rounded-xl transition-all font-semibold"
                >
                  비밀번호 변경
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- Withdrawal Tab -->
        <section v-show="activeTab === 'withdrawal'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <h2 
              :class="[
                'text-3xl font-bold mb-8 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              회원 탈퇴
            </h2>

            <div class="glass-card p-6 lg:p-8">
              <h3 class="text-lg font-bold text-red-600 mb-5">⚠️ 회원 탈퇴 안내</h3>
              <ul 
                :class="[
                  'space-y-2 mb-6 text-sm transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-700'
                ]"
              >
                <li>• 회원 탈퇴 시 모든 개인 정보가 삭제됩니다.</li>
                <li>• 작성한 게시글과 댓글은 삭제되지 않을 수 있습니다.</li>
                <li>• 탈퇴 후 동일 이메일로 재가입이 제한될 수 있습니다.</li>
                <li>• 이 작업은 되돌릴 수 없습니다.</li>
              </ul>

              <div class="mb-6 p-5 bg-red-50 border-2 border-red-200 rounded-xl">
                <label class="flex items-start gap-3 cursor-pointer">
                  <input 
                    type="checkbox" 
                    v-model="withdrawalConfirm" 
                    class="mt-0.5 w-4 h-4 text-red-600 rounded focus:ring-red-500"
                  />
                  <span 
                    :class="[
                      'font-semibold text-sm transition-colors',
                      isDarkMode ? 'text-gray-200' : 'text-gray-900'
                    ]"
                  >
                    위 내용을 모두 확인했으며, 회원 탈퇴에 동의합니다.
                  </span>
                </label>
              </div>

              <button
                @click="showWithdrawalModal = true"
                :disabled="!withdrawalConfirm"
                class="w-full px-6 py-3 bg-red-600 text-white rounded-xl hover:bg-red-700 hover:shadow-lg transition-all font-bold disabled:bg-gray-300 disabled:cursor-not-allowed"
              >
                회원 탈퇴하기
              </button>
            </div>
          </div>
        </section>
      </main>
    </div>

    <!-- Profile Edit Modal -->
    <div
      v-if="showProfileEditModal"
      class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50 p-4"
      @click.self="showProfileEditModal = false"
    >
      <div class="glass-modal rounded-2xl shadow-2xl max-w-2xl w-full p-10">
        <h3 
          :class="[
            'text-3xl font-bold mb-8 transition-colors',
            isDarkMode ? 'text-white' : 'text-gray-900'
          ]"
        >
          프로필 수정
        </h3>
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">이메일 (변경 불가)</label>
            <input 
              type="text" 
              :value="user?.email" 
              disabled 
              class="w-full px-5 py-4 border border-gray-300 rounded-xl bg-gray-50 text-gray-500"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">닉네임</label>
            <input 
              v-model="profileForm.nickname" 
              type="text" 
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">지역</label>
            <select 
              v-model="profileForm.region" 
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all appearance-none bg-white"
            >
              <option value="">지역 선택</option>
              <option v-for="r in regions" :key="r" :value="r">{{ r }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-4">프로필 이미지</label>
            
            <!-- 프로필 이미지 섹션: 좌측 이미지, 우측 상단 버튼, 우측 하단 URL 입력 -->
            <div class="flex gap-6 items-start">
              <!-- 좌측: 동그라미 이미지 -->
              <div class="flex-shrink-0">
                <div class="w-36 h-36 rounded-full overflow-hidden border-2 border-gray-200 bg-gray-100 flex items-center justify-center shadow-md">
                  <img
                    v-if="previewUrl"
                    :src="previewUrl"
                    alt="미리보기"
                    class="w-full h-full object-cover"
                  />
                  <img
                    v-else-if="profileForm.profileImageUrl"
                    :src="profileForm.profileImageUrl"
                    alt="현재 프로필 이미지"
                    class="w-full h-full object-cover"
                  />
                  <div
                    v-else
                    class="w-full h-full bg-gradient-to-br from-indigo-600 to-indigo-800 flex items-center justify-center text-white text-5xl font-bold"
                  >
                    {{ user?.nickname?.charAt(0)?.toUpperCase() || 'U' }}
                  </div>
                </div>
              </div>
              
              <!-- 우측: 상단 버튼, 하단 URL 입력 -->
              <div class="flex-1 flex flex-col gap-4">
                <!-- 우측 상단: 파일 선택 및 업로드 버튼 -->
                <div class="flex flex-col gap-3">
                  <input
                    ref="fileInputRef"
                    type="file"
                    accept="image/jpeg,image/png,image/webp"
                    @change="handleFileSelect"
                    class="hidden"
                  />
                  <div class="flex gap-3">
                    <button
                      type="button"
                      @click="fileInputRef?.click()"
                      :disabled="uploading"
                      class="flex-1 px-5 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 hover:shadow-md transition-all font-medium text-sm disabled:bg-gray-300 disabled:cursor-not-allowed"
                    >
                      {{ uploading ? '업로드 중...' : '파일 선택' }}
                    </button>
                    <button
                      v-if="selectedFile"
                      type="button"
                      @click="uploadFile"
                      :disabled="uploading"
                      class="flex-1 px-5 py-3 bg-slate-800 text-white rounded-lg hover:bg-slate-900 hover:shadow-md transition-all font-medium text-sm disabled:bg-gray-300 disabled:cursor-not-allowed"
                    >
                      {{ uploading ? '업로드 중...' : '업로드' }}
                    </button>
                  </div>
                </div>
                
                <!-- 우측 하단: URL 직접 입력 -->
                <div>
                  <label class="block text-xs font-medium text-gray-600 mb-2">또는 이미지 URL 직접 입력</label>
                  <input 
                    v-model="profileForm.profileImageUrl" 
                    type="url" 
                    placeholder="https://i.pravatar.cc/150"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all text-sm"
                  />
                  <p class="text-xs text-gray-500 mt-2">
                    로컬 파일을 업로드하거나 이미지 URL을 직접 입력할 수 있습니다.<br>
                    비워두면 기본 아바타가 표시됩니다.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <div class="flex gap-3 pt-4">
            <button
              @click="showProfileEditModal = false"
              class="flex-1 px-6 py-3 border-2 border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all font-semibold"
            >
              취소
            </button>
            <button
              @click="saveProfile"
              :disabled="loading"
              class="flex-1 px-6 py-3 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold disabled:bg-gray-300"
            >
              저장
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Password Change Modal -->
    <div
      v-if="showPasswordChangeModal"
      class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50 p-4"
      @click.self="showPasswordChangeModal = false"
    >
      <div class="glass-modal rounded-2xl shadow-2xl max-w-md w-full p-10">
        <h3 
          :class="[
            'text-3xl font-bold mb-8 transition-colors',
            isDarkMode ? 'text-white' : 'text-gray-900'
          ]"
        >
          비밀번호 변경
        </h3>
        <div class="space-y-5">
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">현재 비밀번호</label>
            <input 
              v-model="passwordForm.currentPassword" 
              type="password" 
              placeholder="현재 비밀번호를 입력하세요"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">새 비밀번호</label>
            <input 
              v-model="passwordForm.newPassword" 
              type="password" 
              placeholder="8자 이상, 공백 없이"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all"
            />
            <p class="text-xs text-gray-500 mt-2">8자 이상이며 공백이 없어야 합니다.</p>
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">새 비밀번호 확인</label>
            <input 
              v-model="passwordForm.confirmPassword" 
              type="password" 
              placeholder="새 비밀번호를 다시 입력하세요"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all"
            />
          </div>
          <div class="flex gap-3 pt-6">
            <button
              @click="showPasswordChangeModal = false"
              class="flex-1 px-6 py-4 border-2 border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all font-semibold"
            >
              취소
            </button>
            <button
              @click="savePassword"
              :disabled="loading"
              class="flex-1 px-6 py-4 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold disabled:bg-gray-300"
            >
              변경
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Withdrawal Modal -->
    <div
      v-if="showWithdrawalModal"
      class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50 p-4"
      @click.self="showWithdrawalModal = false"
    >
      <div class="glass-modal rounded-2xl shadow-2xl max-w-md w-full p-10">
        <h3 
          :class="[
            'text-3xl font-bold mb-6 transition-colors',
            isDarkMode ? 'text-white' : 'text-gray-900'
          ]"
        >
          정말로 탈퇴하시겠습니까?
        </h3>
        <p 
          :class="[
            'mb-8 text-base leading-relaxed transition-colors',
            isDarkMode ? 'text-gray-300' : 'text-gray-600'
          ]"
        >
          이 작업은 되돌릴 수 없습니다. 모든 데이터가 영구적으로 삭제됩니다.
        </p>
        <div class="flex gap-3">
          <button
            @click="showWithdrawalModal = false"
            class="glass-button-secondary flex-1 px-6 py-4 rounded-xl transition-all font-semibold"
          >
            취소
          </button>
          <button
            @click="confirmWithdrawal"
            class="flex-1 px-6 py-4 bg-red-600 text-white rounded-xl hover:bg-red-700 hover:shadow-lg transition-all font-semibold"
          >
            탈퇴하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 글래스모피즘 스타일 클래스 - 라이트 모드 기본값 */
.glass-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-primary {
  background: rgba(30, 41, 59, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.glass-button-primary:hover {
  background: rgba(15, 23, 42, 0.9);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  transform: translateY(-1px);
}

.glass-button-secondary {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: #374151;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-secondary:hover {
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.glass-button-icon {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #4b5563;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-icon:hover {
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.glass-error {
  background: rgba(254, 242, 242, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-left: 4px solid #ef4444;
  box-shadow: 0 4px 16px rgba(239, 68, 68, 0.2);
  transition: all 0.3s ease;
}

.glass-drawer {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.glass-modal {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(30px);
  -webkit-backdrop-filter: blur(30px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

/* 다크모드 스타일 */
:deep(.dark) .glass-card,
.dark .glass-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

:deep(.dark) .glass-button-secondary,
.dark .glass-button-secondary {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #e5e7eb;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

:deep(.dark) .glass-button-secondary:hover,
.dark .glass-button-secondary:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

:deep(.dark) .glass-button-icon,
.dark .glass-button-icon {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: #d1d5db;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.dark) .glass-button-icon:hover,
.dark .glass-button-icon:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.25);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

:deep(.dark) .glass-error,
.dark .glass-error {
  background: rgba(239, 68, 68, 0.15);
  box-shadow: 0 4px 16px rgba(239, 68, 68, 0.3);
}

:deep(.dark) .glass-drawer,
.dark .glass-drawer {
  background: rgba(15, 23, 42, 0.95);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.dark) .glass-modal,
.dark .glass-modal {
  background: rgba(15, 23, 42, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
