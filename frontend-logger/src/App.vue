<template>
  <div style="padding: 30px">
    <a-range-picker
      :show-time="{
        hideDisabledOptions: true,
        defaultValue: [
          moment('00:00:00', 'HH:mm:ss'),
          moment('23:59:59', 'HH:mm:ss'),
        ],
      }"
      format="YYYY-MM-DD HH:mm:ss"
      @change="onChange"
    />

    <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane key="jfxj" tab="银行存款现金">
        <a-button type="primary" @click="click1">刷新</a-button>
        <div>
          <a-spin :spinning="spinning1" tip="Loading...">
            <div class="spin-content" v-html="res1"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="jfjhzf" tab="银行存款聚合支付" force-render>
        <a-button type="primary" @click="click2">刷新</a-button>
        <div>
          <a-spin :spinning="spinning2" tip="Loading...">
            <div class="spin-content" v-html="res2"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="jfczj" tab="借方储值金">
        <a-button type="primary" @click="click3">刷新</a-button>
        <div>
          <a-spin :spinning="spinning3" tip="Loading...">
            <div class="spin-content" v-html="res3"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="ar" tab="借方医保金额">
        <a-button type="primary" @click="click4">刷新</a-button>
        <div>
          <a-spin :spinning="spinning4" tip="Loading...">
            <div class="spin-content" v-html="res4"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="jfjmje" tab="借方减免">
        <a-button type="primary" @click="click5">刷新</a-button>
        <div>
          <a-spin :spinning="spinning5" tip="Loading...">
            <div class="spin-content" v-html="res5"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="dfghf" tab="贷方挂号收入">
        <a-button type="primary" @click="click6">刷新</a-button>
        <div>
          <a-spin :spinning="spinning6" tip="Loading...">
            <div class="spin-content" v-html="res6"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="dfsf" tab="贷方收费收入">
        <a-button type="primary" @click="click7">刷新</a-button>
        <div>
          <a-spin :spinning="spinning7" tip="Loading...">
            <div class="spin-content" v-html="res7"></div>
          </a-spin>
        </div>
      </a-tab-pane>
      <a-tab-pane key="dfczj" tab="贷方储值金">
        <a-button type="primary" @click="click8">刷新</a-button>
        <div>
          <a-spin :spinning="spinning8" tip="Loading...">
            <div class="spin-content" v-html="res8"></div>
          </a-spin>
        </div>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import moment from 'moment'
@Component({
  components: {},
})
export default class App extends Vue {
  dateString = []
  moment = moment
  timeChanged = false
  spinning1 = false
  spinning2 = false
  spinning3 = false
  spinning4 = false
  spinning5 = false
  spinning6 = false
  spinning7 = false
  spinning8 = false

  res1 = ''
  res2 = ''
  res3 = ''
  res4 = ''
  res5 = ''

  res6 = ''
  res7 = ''
  res8 = ''

  async click1() {
    if (this.spinning1) {
      return
    }
    this.spinning1 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/jfxj', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning1 = false
    }

    this.res1 = res.data.replace(/\n/g, '<br/>')
  }

  async click2() {
    if (this.spinning2) {
      return
    }
    this.spinning2 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/jfjhzf', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning2 = false
    }

    this.res2 = res.data.replace(/\n/g, '<br/>')
  }

  async click3() {
    if (this.spinning3) {
      return
    }
    this.spinning3 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/jfczj', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning3 = false
    }

    this.res3 = res.data.replace(/\n/g, '<br/>')
  }

  async click4() {
    if (this.spinning4) {
      return
    }
    this.spinning4 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/ar', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning4 = false
    }

    this.res4 = res.data.replace(/\n/g, '<br/>')
  }

  async click5() {
    if (this.spinning5) {
      return
    }
    this.spinning5 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/jfjmje', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning5 = false
    }

    this.res5 = res.data.replace(/\n/g, '<br/>')
  }

  async click6() {
    if (this.spinning6) {
      return
    }
    this.spinning6 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/dfghf', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning6 = false
    }

    this.res6 = res.data.replace(/\n/g, '<br/>')
  }

  async click7() {
    if (this.spinning7) {
      return
    }
    this.spinning7 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/dfsf', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning7 = false
    }

    this.res7 = res.data.replace(/\n/g, '<br/>')
  }

  async click8() {
    if (this.spinning8) {
      return
    }
    this.spinning8 = true
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/dfczj', {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    } finally {
      this.spinning8 = false
    }

    this.res8 = res.data.replace(/\n/g, '<br/>')
  }

  async callback(key) {
    if (this.dateString.length < 2) {
      alert('请输入时间范围')
      return
    }
    switch (key) {
      case 'jfxj':
        if (this.spinning1 == true || this.res1 != '') {
          return
        }
        this.spinning1 = true
        break
      case 'jfjhzf':
        if (this.spinning2 == true || this.res2 != '') {
          return
        }
        this.spinning2 = true
        break
      case 'jfczj':
        if (this.spinning3 == true || this.res3 != '') {
          return
        }
        this.spinning3 = true
        break
      case 'ar':
        if (this.spinning4 == true || this.res4 != '') {
          return
        }
        this.spinning4 = true
        break
      case 'jfjmje':
        if (this.spinning5 == true || this.res5 != '') {
          return
        }
        this.spinning5 = true
        break
      case 'dfghf':
        if (this.spinning6 == true || this.res6 != '') {
          return
        }

        this.spinning6 = true
        break
      case 'dfsf':
        if (this.spinning7 == true || this.res7 != '') {
          return
        }
        this.spinning7 = true
        break
      case 'dfczj':
        if (this.spinning8 == true || this.res8 != '') {
          return
        }
        this.spinning8 = true
        break
      default:
        break
    }
    var res: any = {}
    try {
      res = await this.axios.post('/report' + '/' + key, {
        start: this.dateString[0],
        end: this.dateString[1],
      })
    } catch {
      res = { data: '' }
    }
    switch (key) {
      case 'jfxj':
        this.spinning1 = false
        this.res1 = res.data.replace(/\n/g, '<br/>')
        break
      case 'jfjhzf':
        this.spinning2 = false
        this.res2 = res.data.replace(/\n/g, '<br/>')
        break
      case 'jfczj':
        this.spinning3 = false
        this.res3 = res.data.replace(/\n/g, '<br/>')
        break
      case 'ar':
        this.spinning4 = false
        this.res4 = res.data.replace(/\n/g, '<br/>')
        break
      case 'jfjmje':
        this.spinning5 = false
        this.res5 = res.data.replace(/\n/g, '<br/>')
        break
      case 'dfghf':
        this.spinning6 = false

        this.res6 = res.data.replace(/\n/g, '<br/>')
        break
      case 'dfsf':
        this.spinning7 = false

        this.res7 = res.data.replace(/\n/g, '<br/>')
        break
      case 'dfczj':
        this.spinning8 = false
        this.res8 = res.data.replace(/\n/g, '<br/>')
        break
      default:
        break
    }
  }
  onChange(date, dateString) {
    this.res1 = ''
    this.res2 = ''
    this.res3 = ''
    this.res4 = ''
    this.res5 = ''
    this.res6 = ''
    this.res7 = ''
    this.res8 = ''
    this.dateString = dateString
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.input_st {
  width: 400px;
  padding: 20px;
  position: absolute;
  top: 0;
  right: 0;
}
.app {
  font-family: sans-serif;
  padding: 0;
  display: flex;
  padding: 16px 8px;
}

.app-content {
  flex: 1;
  height: 300px;
  margin-left: 8px;
  margin-right: 8px;
  box-shadow: 0 0 10px 1px #e9e9e9;
}
</style>
