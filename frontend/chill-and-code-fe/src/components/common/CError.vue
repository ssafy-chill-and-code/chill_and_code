<template>
  <div class="alert alert-danger d-flex align-items-start" role="alert">
    <div class="me-2">⚠️</div>
    <div class="flex-grow-1">
      <div class="fw-semibold">문제가 발생했습니다</div>
      <div class="small">{{ message }}</div>
      <div v-if="$slots.actions" class="mt-2">
        <slot name="actions" />
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({ message: { type: String, default: '잠시 후 다시 시도해주세요.' } })
</script>

