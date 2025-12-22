<template>
  <div class="text-center text-muted py-4">
    <div class="mb-2">🗂️</div>
    <div class="mb-2"><slot>표시할 내용이 없습니다</slot></div>
    <div v-if="$slots.cta" class="mt-2">
      <slot name="cta" />
    </div>
  </div>
</template>

<script setup>
// presentational only
</script>

