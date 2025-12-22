<template>
  <button
    :class="[
      'btn',
      variantClass,
      sizeClass,
      { 'w-100': block, disabled: disabled || loading },
    ]"
    :aria-busy="loading ? 'true' : 'false'"
    :disabled="disabled || loading"
    type="button"
    @click="$emit('click')"
  >
    <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
    <slot />
  </button>
  
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  variant: { type: String, default: 'primary' }, // primary | outline-primary | text
  size: { type: String, default: 'md' }, // sm | md | lg
  block: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  loading: { type: Boolean, default: false },
})

defineEmits(['click'])

const variantClass = computed(() => {
  if (props.variant === 'text') return 'btn-link'
  if (props.variant.startsWith('outline-')) return `btn-${props.variant}`
  return `btn-${props.variant}`
})

const sizeClass = computed(() => {
  switch (props.size) {
    case 'sm':
      return 'btn-sm'
    case 'lg':
      return 'btn-lg'
    default:
      return ''
  }
})
</script>

