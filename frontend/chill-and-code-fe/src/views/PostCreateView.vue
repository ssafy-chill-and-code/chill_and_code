<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'

const router = useRouter()
const postStore = usePostStore()

const title = ref('')
const region = ref('')
const content = ref('')
const message = ref('')

async function create() {
  message.value = ''
  if (!title.value.trim() || !content.value.trim()) {
    message.value = 'title/content is required'
    return
  }
  try {
    const res = await postStore.createPost({ title: title.value, region: region.value, content: content.value })
    const newId = res?.data?.postId
    if (newId) router.push({ name: 'post-detail', params: { postId: newId } })
    else router.push({ name: 'posts' })
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

function cancel() {
  router.push({ name: 'posts' })
}
</script>

<template>
  <div>
    <h2 class="page-title">Create Post</h2>
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
      <button class="btn" @click="create">Create</button>
      <button class="btn" @click="cancel">Cancel</button>
    </div>
  </div>
</template>

