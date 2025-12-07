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
  <div>
    <h2 class="page-title">Edit Post</h2>
    <div v-if="message">{{ message }}</div>

    <div class="form-group">
      <label class="form-label">Title</label>
      <input class="form-input" v-model="title" />
    </div>

    <div class="form-group">
      <label class="form-label">Region</label>
      <input class="form-input" v-model="region" />
    </div>

    <div class="form-group">
      <label class="form-label">Content</label>
      <textarea class="form-textarea" v-model="content"></textarea>
    </div>

    <div class="form-group">
      <button class="btn" @click="save">Save</button>
      <button class="btn" @click="cancel">Cancel</button>
    </div>
  </div>
</template>

