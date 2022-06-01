<template>
  <div class="editable-cell">
    <div>
      <a-spin tip="Loading...">
        <div class="spin-content">我的描述文案是自定义的。。。</div>
      </a-spin>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator'
@Component({})
export default class EditableCell extends Vue {
  @Prop({ default: 3 })
  row
}
</script>

<style lang="scss" scoped></style>
