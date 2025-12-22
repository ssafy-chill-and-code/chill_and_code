<template>
  <span :class="['badge rounded-pill', colorClass, inline ? '' : 'position-absolute top-0 start-100 translate-middle']">
    <slot />
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  color: { type: String, default: 'primary' }, // primary|success|warning|danger|info|secondary
  inline: { type: Boolean, default: true },
})

const colorClass = computed(() => `text-bg-${props.color}`)
</script>

