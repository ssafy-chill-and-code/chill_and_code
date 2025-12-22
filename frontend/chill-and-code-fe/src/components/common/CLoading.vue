<template>
  <div :class="['d-flex align-items-center justify-content-center', full ? 'py-5' : 'py-3']">
    <div class="spinner-border text-primary me-2" role="status" aria-hidden="true"></div>
    <span class="text-muted"><slot>로딩 중…</slot></span>
  </div>
</template>

<script setup>
defineProps({ full: { type: Boolean, default: false } })
</script>

