<template>
  <div v-if="modelValue" class="modal-backdrop" @click="onBackdrop">
    <div class="modal-dialog" :class="dialogClass" @click.stop>
      <div class="modal-content">
        <div v-if="$slots.header" class="modal-header">
          <slot name="header" />
          <button type="button" class="btn-close" aria-label="Close" @click="close" />
        </div>
        <div class="modal-body">
          <slot />
        </div>
        <div v-if="$slots.footer" class="modal-footer">
          <slot name="footer" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watchEffect } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  size: { type: String, default: 'md' }, // sm|md|lg
  closeOnBackdrop: { type: Boolean, default: true },
})
const emit = defineEmits(['update:modelValue'])

function close() { emit('update:modelValue', false) }
function onBackdrop() { if (props.closeOnBackdrop) close() }

const dialogClass = computed(() => ({
  'modal-sm': props.size === 'sm',
  'modal-lg': props.size === 'lg',
}))

watchEffect(() => {
  document.body.style.overflow = props.modelValue ? 'hidden' : ''
})
</script>

<style scoped>
.modal-backdrop {
  position: fixed; inset: 0; background: rgba(0,0,0,.4);
  display: flex; align-items: center; justify-content: center;
  z-index: 1050;
}
.modal-dialog { max-width: 600px; width: 92%; }
.modal-sm { max-width: 360px; }
.modal-lg { max-width: 900px; }
</style>

