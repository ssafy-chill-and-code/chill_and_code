<template>
  <div :class="['card', clickable ? 'hover-shadow cursor-pointer' : '']" @click="$emit('click')">
    <div v-if="$slots.header" class="card-header bg-white border-0">
      <slot name="header" />
    </div>
    <div class="card-body">
      <slot />
    </div>
    <div v-if="$slots.footer" class="card-footer bg-white border-0">
      <slot name="footer" />
    </div>
  </div>
</template>

<script setup>
defineProps({ clickable: { type: Boolean, default: false } })
defineEmits(['click'])
</script>

<style scoped>
.hover-shadow:hover { box-shadow: 0 0.5rem 1rem rgba(0,0,0,.06); }
.cursor-pointer { cursor: pointer; }
</style>

