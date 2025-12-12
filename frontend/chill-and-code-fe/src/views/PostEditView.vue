<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'

const route = useRoute()
const router = useRouter()
const postStore = usePostStore()

const title = ref('')
const region = ref('')
const content = ref('')
const message = ref('')

onMounted(async () => {
  await postStore.fetchPostDetail(route.params.postId)
  const p = postStore.post
  if (p) {
    title.value = p.title || ''
    region.value = p.region || ''
    content.value = p.content || ''
  }
})

async function save() {
  message.value = ''
  if (!title.value.trim() || !content.value.trim()) {
    message.value = 'title/content is required'
    return
  }
  try {
    await postStore.updatePost(route.params.postId, { title: title.value, region: region.value, content: content.value })
    router.push({ name: 'post-detail', params: { postId: route.params.postId } })
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

function cancel() {
  router.push({ name: 'post-detail', params: { postId: route.params.postId } })
}
</script>

<template>
  <div class="container py-4">
    <h2 class="page-title h4 mb-3">Edit Post</h2>
    <div v-if="message" class="alert alert-warning">{{ message }}</div>

    <div class="card">
      <div class="card-body">
        <div class="form-group">
          <label class="form-label">Title</label>
          <input class="form-input form-control" v-model="title" />
        </div>

        <div class="form-group">
          <label class="form-label">Region</label>
          <select class="form-input form-select" v-model="region">
            <option value="">지역을 선택하세요</option>
            <option value="서울">서울</option>
            <option value="부산">부산</option>
            <option value="제주">제주</option>
            <option value="인천">인천</option>
            <option value="대구">대구</option>
            <option value="대전">대전</option>
            <option value="광주">광주</option>
            <option value="울산">울산</option>
            <option value="경기">경기</option>
            <option value="강원">강원</option>
            <option value="충북">충북</option>
            <option value="충남">충남</option>
            <option value="전북">전북</option>
            <option value="전남">전남</option>
            <option value="경북">경북</option>
            <option value="경남">경남</option>
            <option value="세종">세종</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Content</label>
          <textarea class="form-textarea form-control" v-model="content" rows="6"></textarea>
        </div>

        <div class="d-flex gap-2">
          <button class="btn btn-primary" @click="save">Save</button>
          <button class="btn btn-outline-secondary" @click="cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>
