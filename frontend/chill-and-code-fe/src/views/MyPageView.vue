<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { usePostStore } from '../stores/post'
import { useCommentStore } from '../stores/comment'
import { useScheduleStore } from '../stores/schedule'

const router = useRouter()
const userStore = useUserStore()
const postStore = usePostStore()
const commentStore = useCommentStore()
const scheduleStore = useScheduleStore()

const activeTab = ref('home')
const mobileMenuOpen = ref(false)
const loading = ref(false)
const error = ref(null)
const showProfileEditModal = ref(false)

const profileForm = ref({
  nickname: '',
  region: '',
})

const showWithdrawalModal = ref(false)
const withdrawalConfirm = ref(false)
const scheduleTypeFilter = ref(null)

const user = computed(() => userStore.user)
const myPosts = computed(() => postStore.myPosts)
const myComments = computed(() => commentStore.myComments)
const schedules = computed(() => {
  if (!scheduleTypeFilter.value) return scheduleStore.schedules
  return scheduleStore.schedules.filter(s => s.type === scheduleTypeFilter.value)
})

const tabs = [
  { id: 'home', label: '홈', icon: '🏠' },
  { id: 'posts', label: '내 게시글', icon: '📝' },
  { id: 'comments', label: '내 댓글', icon: '💬' },
  { id: 'schedules', label: '내 일정', icon: '📅' },
  { id: 'settings', label: '계정 설정', icon: '⚙️' },
  { id: 'withdrawal', label: '회원 탈퇴', icon: '🚪' },
]

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
  showProfileEditModal.value = true
}

const saveProfile = async () => {
  loading.value = true
  error.value = null
  try {
    await userStore.updateProfile(profileForm.value)
    showProfileEditModal.value = false
    alert('프로필이 수정되었습니다.')
  } catch (e) {
    error.value = '프로필 수정에 실패했습니다.'
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
  <!-- Full-width layout (Global NavBar rendered by App.vue) -->
  <div class="min-h-screen">
    <div class="flex">
      <!-- Desktop Sidebar -->
      <aside class="hidden lg:block w-64 bg-white fixed left-0 top-16 bottom-0 overflow-y-auto z-10 border-r border-gray-200">
        <div class="p-6">
          <h2 class="text-xl font-bold text-gray-900 mb-6">마이페이지</h2>
          <nav class="space-y-1">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="changeTab(tab.id)"
              :class="[
                'w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-all text-left',
                activeTab === tab.id
                  ? 'bg-slate-800 text-white font-semibold shadow-sm'
                  : 'text-gray-700 hover:bg-gray-100 active:bg-gray-200'
              ]"
            >
              <span class="text-lg">{{ tab.icon }}</span>
              <span>{{ tab.label }}</span>
            </button>
          </nav>
        </div>
      </aside>

      <!-- Mobile Floating Menu Button -->
      <button
        @click="mobileMenuOpen = !mobileMenuOpen"
        class="lg:hidden fixed bottom-6 right-6 w-14 h-14 bg-slate-800 text-white rounded-full shadow-2xl flex items-center justify-center z-30 hover:bg-slate-900 active:bg-slate-950 transition-colors"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
        </svg>
      </button>

      <!-- Mobile Drawer Overlay -->
      <div 
        v-if="mobileMenuOpen" 
        class="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
        @click="mobileMenuOpen = false"
      ></div>

      <!-- Mobile Drawer -->
      <div 
        :class="[
          'fixed top-0 left-0 bottom-0 w-72 bg-white shadow-2xl transform transition-transform duration-300 z-50 lg:hidden overflow-y-auto',
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        ]"
      >
        <div class="p-4 border-b border-gray-200 flex items-center justify-between">
          <h2 class="text-lg font-bold text-gray-900">마이페이지 메뉴</h2>
          <button 
            @click="mobileMenuOpen = false"
            class="p-2 rounded-lg hover:bg-gray-100 active:bg-gray-200 transition-colors"
          >
            <svg class="w-5 h-5 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>
        <nav class="p-4 space-y-1">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="changeTab(tab.id)"
            :class="[
              'w-full flex items-center gap-3 px-4 py-4 rounded-lg transition-all text-left',
              activeTab === tab.id
                ? 'bg-slate-800 text-white font-semibold'
                : 'text-gray-700 hover:bg-gray-100 active:bg-gray-200'
            ]"
          >
            <span class="text-xl">{{ tab.icon }}</span>
            <span>{{ tab.label }}</span>
          </button>
        </nav>
      </div>

      <!-- Main Content Area -->
      <main class="flex-1 lg:ml-64 min-h-screen">
        <!-- Error Message -->
        <div v-if="error" class="px-8 lg:px-12 py-8">
          <div class="p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
            {{ error }}
          </div>
        </div>

        <!-- Content Sections -->
            <!-- Home Tab -->
            <section v-show="activeTab === 'home'" class="min-h-screen">
              <!-- Loading State -->
              <div v-if="loading" class="flex items-center justify-center py-20">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-slate-800"></div>
              </div>

              <div v-else class="px-8 lg:px-12 py-10 lg:py-12">
                <h2 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-8 lg:mb-10">내 프로필</h2>
                
                <!-- Profile Section -->
                <div class="mb-10 lg:mb-12">
                  <!-- Profile Card -->
                  <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 lg:p-10">
                    <div class="flex flex-col lg:flex-row lg:items-start gap-6">
                      <div class="w-20 h-20 lg:w-24 lg:h-24 bg-gradient-to-br from-slate-700 to-slate-900 rounded-full flex items-center justify-center text-white text-3xl lg:text-4xl font-bold mx-auto lg:mx-0 flex-shrink-0">
                        {{ user?.nickname?.charAt(0) || 'U' }}
                      </div>
                      <div class="flex-1 text-center lg:text-left">
                        <h3 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-2">{{ user?.nickname || '사용자' }}</h3>
                        <p class="text-gray-600 mb-1">{{ user?.email }}</p>
                        <p class="text-gray-500 text-sm">📍 {{ user?.region || '지역 정보 없음' }}</p>
                      </div>
                    </div>

                    <!-- Edit Button -->
                    <div class="mt-6 lg:mt-4">
                      <button
                        @click="openProfileEdit"
                        class="w-full lg:w-auto px-6 py-3 lg:py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-900 active:bg-slate-950 transition-colors font-semibold"
                      >
                        프로필 수정
                      </button>
          </div>
        </div>
      </div>

                <!-- Stats Section -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 lg:gap-8">
                  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center opacity-60">
                    <div class="text-4xl mb-2">📊</div>
                    <div class="text-sm text-gray-500 mb-1">워케이션 생성</div>
                    <div class="text-lg font-bold text-gray-400">COMING SOON</div>
                  </div>
                  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center opacity-60">
                    <div class="text-4xl mb-2">💡</div>
                    <div class="text-sm text-gray-500 mb-1">추천 이용</div>
                    <div class="text-lg font-bold text-gray-400">COMING SOON</div>
                  </div>
                  <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center opacity-60">
                    <div class="text-4xl mb-2">🎨</div>
                    <div class="text-sm text-gray-500 mb-1">워케이션 스타일</div>
                    <div class="text-lg font-bold text-gray-400">COMING SOON</div>
                  </div>
                </div>
              </div>
            </section>

            <!-- My Posts Tab -->
            <section v-show="activeTab === 'posts'" class="min-h-screen px-8 lg:px-12 py-10 lg:py-12">
              <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-8 lg:mb-10">
                <h2 class="text-2xl lg:text-3xl font-bold text-gray-900">내 게시글</h2>
                <button
                  @click="$router.push('/posts')"
                  class="w-full lg:w-auto px-6 py-3 lg:py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 active:bg-gray-100 transition-colors font-medium"
                >
                  커뮤니티 둘러보기
                </button>
              </div>

              <div v-if="loading" class="flex items-center justify-center py-20">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-slate-800"></div>
              </div>

              <div v-else-if="myPosts.length === 0" class="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
                <div class="text-6xl mb-4">📝</div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">작성한 게시글이 없습니다</h3>
                <p class="text-gray-600 mb-6">첫 게시글을 작성해보세요!</p>
                <button
                  @click="$router.push('/posts')"
                  class="px-6 py-3 bg-slate-800 text-white rounded-lg hover:bg-slate-900 active:bg-slate-950 transition-colors font-semibold"
                >
                  게시판 둘러보기
                </button>
              </div>

              <div v-else class="space-y-4">
                <div
                  v-for="post in myPosts"
                  :key="post.postId"
                  class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 lg:p-8 hover:shadow-md transition-shadow"
                >
                  <div class="flex flex-col lg:flex-row gap-4">
                    <div class="flex-1 cursor-pointer" @click="$router.push(`/posts/${post.postId}`)">
                      <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ post.title }}</h3>
                      <p class="text-gray-600 mb-3 line-clamp-2">{{ post.content }}</p>
                      <div class="flex flex-wrap items-center gap-3 text-sm text-gray-500">
                        <span>{{ post.region || '지역 미지정' }}</span>
                        <span>•</span>
                        <span>조회 {{ post.viewCount || 0 }}</span>
                        <span>•</span>
                        <span>{{ new Date(post.createdAt).toLocaleDateString() }}</span>
                      </div>
                    </div>
                    <button
                      @click.stop="deletePost(post.postId)"
                      class="px-4 py-2 text-red-600 hover:bg-red-50 active:bg-red-100 rounded-lg transition-colors font-medium whitespace-nowrap self-start"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
            </section>

            <!-- My Comments Tab -->
            <section v-show="activeTab === 'comments'" class="min-h-screen px-8 lg:px-12 py-10 lg:py-12">
              <h2 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-8 lg:mb-10">내 댓글</h2>

              <div v-if="loading" class="flex items-center justify-center py-20">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-slate-800"></div>
              </div>

              <div v-else-if="myComments.length === 0" class="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
                <div class="text-6xl mb-4">💬</div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">작성한 댓글이 없습니다</h3>
                <p class="text-gray-600">커뮤니티에서 활동해보세요!</p>
              </div>

              <div v-else class="space-y-4">
                <div
                  v-for="comment in myComments"
                  :key="comment.commentId"
                  class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 lg:p-8 hover:shadow-md transition-shadow"
                >
                  <div class="flex flex-col lg:flex-row gap-4">
                    <div class="flex-1">
                      <p class="text-gray-900 mb-3">{{ comment.content }}</p>
                      <div class="flex flex-wrap items-center gap-3 text-sm text-gray-500 mb-2">
                        <span>{{ new Date(comment.createdAt).toLocaleDateString() }}</span>
                      </div>
                      <button
                        v-if="comment.postId"
                        @click="$router.push(`/posts/${comment.postId}`)"
                        class="text-sm text-slate-700 hover:text-slate-900 font-medium py-2"
                      >
                        원문 게시글 보기 →
                      </button>
                    </div>
                    <button
                      @click="deleteComment(comment.commentId)"
                      class="px-4 py-2 text-red-600 hover:bg-red-50 active:bg-red-100 rounded-lg transition-colors font-medium whitespace-nowrap self-start"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
            </section>

            <!-- My Schedules Tab -->
            <section v-show="activeTab === 'schedules'" class="min-h-screen px-8 lg:px-12 py-10 lg:py-12">
              <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-8 lg:mb-10">
                <h2 class="text-2xl lg:text-3xl font-bold text-gray-900">내 일정</h2>
                <button
                  @click="$router.push('/schedule')"
                  class="w-full lg:w-auto px-6 py-3 lg:py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 active:bg-gray-100 transition-colors font-medium"
                >
                  일정 캘린더로 이동
                </button>
              </div>

              <!-- Type Filter -->
              <div class="mb-6 flex gap-2 flex-wrap">
                <button
                  @click="scheduleTypeFilter = null"
                  :class="[
                    'px-4 py-2 rounded-lg font-medium transition-colors',
                    scheduleTypeFilter === null
                      ? 'bg-slate-800 text-white'
                      : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 active:bg-gray-100'
                  ]"
                >
                  전체
                </button>
                <button
                  v-for="type in scheduleTypes"
                  :key="type.value"
                  @click="scheduleTypeFilter = type.value"
                  :class="[
                    'px-4 py-2 rounded-lg font-medium transition-colors',
                    scheduleTypeFilter === type.value
                      ? 'bg-slate-800 text-white'
                      : 'bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 active:bg-gray-100'
                  ]"
                >
                  {{ type.label }}
                </button>
              </div>

              <div v-if="loading" class="flex items-center justify-center py-20">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-slate-800"></div>
          </div>

              <div v-else-if="schedules.length === 0" class="bg-white rounded-2xl shadow-sm border border-gray-200 p-12 text-center">
                <div class="text-6xl mb-4">📅</div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">등록된 일정이 없습니다</h3>
                <p class="text-gray-600 mb-6">새로운 일정을 추가해보세요!</p>
                <button
                  @click="$router.push('/schedule')"
                  class="px-6 py-3 bg-slate-800 text-white rounded-lg hover:bg-slate-900 active:bg-slate-950 transition-colors font-semibold"
                >
                  일정 추가하기
                </button>
          </div>

              <div v-else class="space-y-4">
                <div
                  v-for="schedule in schedules"
                  :key="schedule.scheduleId"
                  class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 lg:p-8 hover:shadow-md transition-shadow"
                >
                  <div class="flex items-start gap-4">
                    <div 
                      :class="[
                        'w-2 h-full rounded-full flex-shrink-0',
                        scheduleTypes.find(t => t.value === schedule.type)?.color || 'bg-gray-400'
                      ]"
                    ></div>
                    <div class="flex-1">
                      <div class="flex items-center gap-2 mb-2">
                        <span 
                          :class="[
                            'px-3 py-1 rounded-lg text-sm font-medium text-white',
                            scheduleTypes.find(t => t.value === schedule.type)?.color || 'bg-gray-400'
                          ]"
                        >
                          {{ scheduleTypes.find(t => t.value === schedule.type)?.label || schedule.type }}
                        </span>
          </div>
                      <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ schedule.title }}</h3>
                      <p class="text-gray-600 mb-2">{{ schedule.description }}</p>
                      <div class="text-sm text-gray-500">
                        📅 {{ schedule.startDate }} ~ {{ schedule.endDate }}
          </div>
                    </div>
                  </div>
                </div>
              </div>
            </section>

            <!-- Account Settings Tab -->
            <section v-show="activeTab === 'settings'" class="min-h-screen px-8 lg:px-12 py-10 lg:py-12">
              <h2 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-6">계정 설정</h2>

              <div class="space-y-4">
                <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                  <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
                    <div>
                      <h3 class="text-lg font-semibold text-gray-900 mb-1">비밀번호 변경</h3>
                      <p class="text-gray-600 text-sm">계정의 비밀번호를 변경합니다</p>
                    </div>
                    <div class="px-4 py-2 bg-gray-100 text-gray-500 rounded-lg text-sm font-medium text-center lg:text-left">
                      COMING SOON
                    </div>
                  </div>
                </div>
              </div>
            </section>

            <!-- Withdrawal Tab -->
            <section v-show="activeTab === 'withdrawal'" class="min-h-screen px-8 lg:px-12 py-10 lg:py-12">
              <h2 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-6">회원 탈퇴</h2>

              <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 lg:p-8">
                <div class="mb-6">
                  <h3 class="text-xl font-semibold text-red-600 mb-4">⚠️ 회원 탈퇴 안내</h3>
                  <ul class="space-y-2 text-gray-700">
                    <li class="flex items-start gap-2">
                      <span class="text-red-500 mt-1">•</span>
                      <span>회원 탈퇴 시 모든 개인 정보가 삭제됩니다.</span>
                    </li>
                    <li class="flex items-start gap-2">
                      <span class="text-red-500 mt-1">•</span>
                      <span>작성한 게시글과 댓글은 삭제되지 않을 수 있습니다.</span>
                    </li>
                    <li class="flex items-start gap-2">
                      <span class="text-red-500 mt-1">•</span>
                      <span>탈퇴 후 동일 이메일로 재가입이 제한될 수 있습니다.</span>
                    </li>
                    <li class="flex items-start gap-2">
                      <span class="text-red-500 mt-1">•</span>
                      <span>이 작업은 되돌릴 수 없습니다.</span>
                    </li>
                  </ul>
                </div>

                <div class="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                  <label class="flex items-start gap-3 cursor-pointer">
                    <input 
                      type="checkbox" 
                      v-model="withdrawalConfirm" 
                      class="mt-1 w-5 h-5 text-red-600 rounded focus:ring-red-500"
                    />
                    <span class="text-gray-900 font-medium">
                      위 내용을 모두 확인했으며, 회원 탈퇴에 동의합니다.
                    </span>
                  </label>
                </div>

                <button
                  @click="showWithdrawalModal = true"
                  :disabled="!withdrawalConfirm"
                  class="w-full px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 active:bg-red-800 transition-colors font-semibold disabled:bg-gray-300 disabled:cursor-not-allowed"
                >
                  회원 탈퇴하기
                </button>
              </div>
            </section>
      </main>
    </div>

    <!-- Profile Edit Modal -->
    <div
      v-if="showProfileEditModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="showProfileEditModal = false"
    >
      <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 lg:p-8">
        <h3 class="text-2xl font-bold text-gray-900 mb-6">프로필 수정</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">이메일 (변경 불가)</label>
            <input 
              type="text" 
              :value="user?.email" 
              disabled 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg bg-gray-50 text-gray-500"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">닉네임</label>
            <input 
              v-model="profileForm.nickname" 
              type="text" 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-transparent"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">지역</label>
            <select 
              v-model="profileForm.region" 
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-transparent"
            >
              <option value="">지역 선택</option>
              <option v-for="r in regions" :key="r" :value="r">{{ r }}</option>
            </select>
          </div>
          <div class="flex gap-3 pt-4">
            <button
              @click="showProfileEditModal = false"
              class="flex-1 px-4 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 active:bg-gray-100 transition-colors font-semibold"
            >
              취소
            </button>
            <button
              @click="saveProfile"
              :disabled="loading"
              class="flex-1 px-4 py-3 bg-slate-800 text-white rounded-lg hover:bg-slate-900 active:bg-slate-950 transition-colors font-semibold disabled:bg-gray-300"
            >
              저장
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Withdrawal Confirmation Modal -->
    <div
      v-if="showWithdrawalModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="showWithdrawalModal = false"
    >
      <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 lg:p-8">
        <h3 class="text-2xl font-bold text-gray-900 mb-4">정말로 탈퇴하시겠습니까?</h3>
        <p class="text-gray-600 mb-6">
          이 작업은 되돌릴 수 없습니다. 모든 데이터가 영구적으로 삭제됩니다.
        </p>
        <div class="flex gap-3">
          <button
            @click="showWithdrawalModal = false"
            class="flex-1 px-4 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 active:bg-gray-100 transition-colors font-semibold"
          >
            취소
          </button>
          <button
            @click="confirmWithdrawal"
            class="flex-1 px-4 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 active:bg-red-800 transition-colors font-semibold"
          >
            탈퇴하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
