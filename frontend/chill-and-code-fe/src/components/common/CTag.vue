<template>
  <span :class="['badge', variantClass]"><slot /></span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ variant: { type: String, default: 'secondary' } })

const map = {
  info: 'bg-info-subtle text-info-emphasis border border-info-subtle',
  success: 'bg-success-subtle text-success-emphasis border border-success-subtle',
  warn: 'bg-warning-subtle text-warning-emphasis border border-warning-subtle',
  error: 'bg-danger-subtle text-danger-emphasis border border-danger-subtle',
  secondary: 'bg-secondary-subtle text-secondary-emphasis border border-secondary-subtle',
}

const variantClass = computed(() => map[props.variant] || map.secondary)
</script>

