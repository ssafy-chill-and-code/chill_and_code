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
  { id: 'home', label: '홈' },
  { id: 'posts', label: '내 게시글' },
  { id: 'comments', label: '내 댓글' },
  { id: 'schedules', label: '내 일정' },
  { id: 'settings', label: '계정 설정' },
  { id: 'withdrawal', label: '회원 탈퇴' },
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
  <div class="min-h-screen bg-white">
    <div class="flex">
      <!-- Desktop Sidebar -->
      <aside class="hidden lg:block w-80 border-r border-gray-200 fixed left-0 top-16 bottom-0 overflow-y-auto bg-white">
        <div class="p-10">
          <h2 class="text-2xl font-bold text-gray-900 mb-10">마이페이지</h2>
          <nav class="space-y-3">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="changeTab(tab.id)"
              :class="[
                'w-full text-left px-6 py-4 rounded-xl transition-all text-base font-medium',
                activeTab === tab.id
                  ? 'bg-slate-800 text-white shadow-lg'
                  : 'text-gray-700 hover:bg-gray-100'
              ]"
            >
              {{ tab.label }}
            </button>
          </nav>
        </div>
      </aside>

      <!-- Mobile Menu Button -->
      <button
        @click="mobileMenuOpen = !mobileMenuOpen"
        class="lg:hidden fixed bottom-8 right-8 w-16 h-16 bg-slate-800 text-white rounded-full shadow-2xl flex items-center justify-center z-30 hover:bg-slate-900 transition-all"
      >
        <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
        </svg>
      </button>

      <!-- Mobile Drawer -->
      <div 
        v-if="mobileMenuOpen" 
        class="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
        @click="mobileMenuOpen = false"
      ></div>
      <div 
        :class="[
          'fixed top-0 left-0 bottom-0 w-80 bg-white shadow-2xl transform transition-transform duration-300 z-50 lg:hidden overflow-y-auto',
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        ]"
      >
        <div class="p-8 border-b border-gray-200 flex items-center justify-between">
          <h2 class="text-xl font-bold text-gray-900">마이페이지</h2>
          <button 
            @click="mobileMenuOpen = false"
            class="p-2 rounded-lg hover:bg-gray-100 transition-colors"
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
              'w-full text-left px-6 py-4 rounded-xl transition-all text-base font-medium',
              activeTab === tab.id
                ? 'bg-slate-800 text-white shadow-lg'
                : 'text-gray-700 hover:bg-gray-100'
            ]"
          >
            {{ tab.label }}
          </button>
        </nav>
      </div>

      <!-- Main Content -->
      <main class="flex-1 lg:ml-80">
        <!-- Error Message -->
        <div v-if="error" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm">
            {{ error }}
          </div>
        </div>

        <!-- Home Tab -->
        <section v-show="activeTab === 'home'" class="p-8 lg:p-12">
          <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
            <span class="text-gray-600 font-medium">불러오는 중...</span>
          </div>

          <div v-else class="max-w-4xl mx-auto space-y-6">
            <!-- Profile Card -->
            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 lg:p-8">
              <h2 class="text-2xl font-bold text-gray-900 mb-6">프로필</h2>
              
              <div class="flex flex-col lg:flex-row items-center lg:items-start gap-6 mb-6">
                <div class="w-20 h-20 bg-gradient-to-br from-indigo-600 to-indigo-800 rounded-full flex items-center justify-center text-white text-3xl font-bold shadow-lg flex-shrink-0">
                  {{ user?.nickname?.charAt(0)?.toUpperCase() || 'U' }}
                </div>
                <div class="flex-1 text-center lg:text-left">
                  <h3 class="text-2xl font-bold text-gray-900 mb-2">{{ user?.nickname || '사용자' }}</h3>
                  <p class="text-gray-600 mb-1">{{ user?.email }}</p>
                  <p class="text-gray-500 text-sm">{{ user?.region || '지역 정보 없음' }}</p>
                </div>
              </div>

              <button
                @click="openProfileEdit"
                class="w-full lg:w-auto px-6 py-3 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold"
              >
                프로필 수정
              </button>
            </div>

            <!-- Stats (Coming Soon) -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 text-center opacity-40">
                <div class="text-xs text-gray-500 mb-1 font-medium">워케이션 생성</div>
                <div class="text-lg font-bold text-gray-400">준비중</div>
              </div>
              <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 text-center opacity-40">
                <div class="text-xs text-gray-500 mb-1 font-medium">추천 이용</div>
                <div class="text-lg font-bold text-gray-400">준비중</div>
              </div>
              <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 text-center opacity-40">
                <div class="text-xs text-gray-500 mb-1 font-medium">워케이션 스타일</div>
                <div class="text-lg font-bold text-gray-400">준비중</div>
              </div>
            </div>
          </div>
        </section>

        <!-- My Posts Tab -->
        <section v-show="activeTab === 'posts'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-8">
              <h2 class="text-3xl font-bold text-gray-900">내 게시글</h2>
              <button
                @click="$router.push('/posts')"
                class="w-full lg:w-auto px-6 py-3 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white rounded-xl hover:from-indigo-700 hover:to-indigo-800 transition-all font-semibold shadow-sm hover:shadow-md"
              >
                커뮤니티 둘러보기
              </button>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
              <span class="text-gray-600 font-medium">불러오는 중...</span>
            </div>

            <div v-else-if="myPosts.length === 0" class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-16 text-center">
              <h3 class="text-2xl font-bold text-gray-900 mb-3">작성한 게시글이 없습니다</h3>
              <p class="text-gray-600 mb-8 text-lg">첫 게시글을 작성해보세요!</p>
              <button
                @click="$router.push('/posts')"
                class="px-8 py-4 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white rounded-xl hover:from-indigo-700 hover:to-indigo-800 hover:shadow-lg transition-all font-semibold"
              >
                게시판 둘러보기
              </button>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="post in myPosts"
                :key="post.postId"
                class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 hover:shadow-xl transition-all cursor-pointer"
                @click="$router.push(`/posts/${post.postId}`)"
              >
                <h3 class="text-lg font-bold text-gray-900 mb-2">{{ post.title }}</h3>
                <p class="text-gray-600 mb-3 line-clamp-2 leading-relaxed text-sm">{{ post.content }}</p>
                <div class="flex flex-wrap items-center justify-between gap-4">
                  <div class="flex items-center gap-3 text-xs text-gray-500">
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
            <h2 class="text-3xl font-bold text-gray-900 mb-8">내 댓글</h2>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
              <span class="text-gray-600 font-medium">불러오는 중...</span>
            </div>

            <div v-else-if="myComments.length === 0" class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-16 text-center">
              <h3 class="text-2xl font-bold text-gray-900 mb-3">작성한 댓글이 없습니다</h3>
              <p class="text-gray-600 text-lg">커뮤니티에서 활동해보세요!</p>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="comment in myComments"
                :key="comment.commentId"
                class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 hover:shadow-xl transition-all"
              >
                <p class="text-gray-900 mb-3 leading-relaxed">{{ comment.content }}</p>
                <div class="flex items-center justify-between gap-4">
                  <span class="text-xs text-gray-500">{{ new Date(comment.createdAt).toLocaleDateString() }}</span>
                  <div class="flex items-center gap-3">
                    <button
                      v-if="comment.postId"
                      @click="$router.push(`/posts/${comment.postId}`)"
                      class="text-xs text-slate-700 hover:text-slate-900 font-semibold transition-colors"
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
              <h2 class="text-3xl font-bold text-gray-900">내 일정</h2>
              <button
                @click="$router.push('/schedule')"
                class="w-full lg:w-auto px-6 py-3 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white rounded-xl hover:from-indigo-700 hover:to-indigo-800 transition-all font-semibold shadow-sm hover:shadow-md"
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
                    ? 'bg-slate-800 text-white shadow-lg'
                    : 'bg-white border-2 border-gray-300 text-gray-700 hover:bg-gray-50'
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
                    ? 'bg-slate-800 text-white shadow-lg'
                    : 'bg-white border-2 border-gray-300 text-gray-700 hover:bg-gray-50'
                ]"
              >
                {{ type.label }}
              </button>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center gap-4 py-32">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
              <span class="text-gray-600 font-medium">불러오는 중...</span>
            </div>

            <div v-else-if="schedules.length === 0" class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-16 text-center">
              <h3 class="text-2xl font-bold text-gray-900 mb-3">등록된 일정이 없습니다</h3>
              <p class="text-gray-600 mb-8 text-lg">새로운 일정을 추가해보세요!</p>
              <button
                @click="$router.push('/schedule')"
                class="px-8 py-4 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white rounded-xl hover:from-indigo-700 hover:to-indigo-800 hover:shadow-lg transition-all font-semibold"
              >
                일정 추가하기
              </button>
            </div>

            <div v-else class="space-y-3">
              <div
                v-for="schedule in schedules"
                :key="schedule.scheduleId"
                class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 hover:shadow-xl transition-all"
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
                    <h3 class="text-lg font-bold text-gray-900 mb-2">{{ schedule.title }}</h3>
                    <p class="text-gray-600 mb-2 leading-relaxed text-sm">{{ schedule.description }}</p>
                    <p class="text-xs text-gray-500">{{ schedule.startDate }} ~ {{ schedule.endDate }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Settings Tab -->
        <section v-show="activeTab === 'settings'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <h2 class="text-3xl font-bold text-gray-900 mb-8">계정 설정</h2>

            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 lg:p-8">
              <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
                <div>
                  <h3 class="text-xl font-bold text-gray-900 mb-2">비밀번호 변경</h3>
                  <p class="text-gray-600">계정의 비밀번호를 변경합니다</p>
                </div>
                <div class="px-6 py-3 bg-gray-100 text-gray-500 rounded-xl font-semibold">
                  준비중
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Withdrawal Tab -->
        <section v-show="activeTab === 'withdrawal'" class="p-8 lg:p-12">
          <div class="max-w-4xl mx-auto">
            <h2 class="text-3xl font-bold text-gray-900 mb-8">회원 탈퇴</h2>

            <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-6 lg:p-8">
              <h3 class="text-lg font-bold text-red-600 mb-5">⚠️ 회원 탈퇴 안내</h3>
              <ul class="space-y-2 text-gray-700 mb-6 text-sm">
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
                  <span class="text-gray-900 font-semibold text-sm">
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
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="showProfileEditModal = false"
    >
      <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full p-10">
        <h3 class="text-3xl font-bold text-gray-900 mb-8">프로필 수정</h3>
        <div class="space-y-5">
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
          <div class="flex gap-3 pt-6">
            <button
              @click="showProfileEditModal = false"
              class="flex-1 px-6 py-4 border-2 border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all font-semibold"
            >
              취소
            </button>
            <button
              @click="saveProfile"
              :disabled="loading"
              class="flex-1 px-6 py-4 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold disabled:bg-gray-300"
            >
              저장
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Withdrawal Modal -->
    <div
      v-if="showWithdrawalModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click.self="showWithdrawalModal = false"
    >
      <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full p-10">
        <h3 class="text-3xl font-bold text-gray-900 mb-6">정말로 탈퇴하시겠습니까?</h3>
        <p class="text-gray-600 mb-8 text-base leading-relaxed">
          이 작업은 되돌릴 수 없습니다. 모든 데이터가 영구적으로 삭제됩니다.
        </p>
        <div class="flex gap-3">
          <button
            @click="showWithdrawalModal = false"
            class="flex-1 px-6 py-4 border-2 border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all font-semibold"
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
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
