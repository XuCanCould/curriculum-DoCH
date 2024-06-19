<template>
  <div class="container">
    <div class="title" v-if="!editBookId">新建数字化文化遗产项目{{ editBookId }}</div>
    <div class="title" v-else>
      <span>修改数字化文化遗产项目信息</span> <span class="back" @click="back"> <i class="iconfont icon-fanhui"></i> 返回 </span>
    </div>

    <div class="wrap">
      <el-row>
        <el-col :lg="16" :md="20" :sm="24" :xs="24">
          <el-form :model="heritage" status-icon ref="form"  label-width="100px" @submit.prevent :rules="rules">
              <el-form-item label="关联项目">
                <el-input  placeholder="管理文化遗产项目的名称" :readonly="true"></el-input>
              </el-form-item>
              <el-form-item label="项目名称" prop="name">
                <el-input v-model="heritage.name" placeholder="项目名称"></el-input>
              </el-form-item>
              <el-form-item prop="digital_archives_type">
                <el-select ref="digital_archives_type" v-model="heritage.heritage_type" placeholder="请选择数字化项目类型">
                  <el-option v-for="item in culturalHeritageTypes" :key="item.value" :label="item.label"
                    :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="网页链接" prop="protection_unit">
                <el-input v-model="heritage.protection_unit" placeholder="请给出相关网址"></el-input>
              </el-form-item>
              <el-form-item label="简介" prop="summary">
                <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入简介"
                  v-model="heritage.summary">
                </el-input>
              </el-form-item>

              <el-form-item class="submit">
                <el-button type="primary" @click="submitForm">保 存</el-button>
                <el-button @click="resetForm">重 置</el-button>
              </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import bookModel from '@/model/digital-archives'
// import heritageModel from '@/model/book'

export default {
  props: {
    editBookId: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      culturalHeritageTypes: [
        { value: 1, label: '数字化采集与保护修复' },
        { value: 2, label: '数字文创' },
        { value: 3, label: '数字陈展' },
      ],
    }
  },
  setup(props, context) {
    const form = ref(null)
    const loading = ref(false)
    // const book = reactive({ title: '', author: '', summary: '', image: '' })
    const heritage = reactive({ name: '', location: '', heritage_type: '', summary: '', protection_unit: '', is_digital: false })
    const listAssign = (a, b) => Object.keys(a).forEach(key => {
      a[key] = b[key] || a[key]
    })

    /**
     * 表单规则验证
     */
    const { rules } = getRules()

    onMounted(() => {
      if (props.editBookId) {
        getBook()
      }
    })
    const getBook = async () => {
      loading.value = true
      const res = await bookModel.getBook(props.editBookId)
      // const ent = await heritageModel.getBook(props.editBookId)
      listAssign(heritage, res)
      // listAssign(heritage, entity)
      loading.value = false
    }

    // 重置表单
    const resetForm = () => {
      form.value.resetFields()
    }

    const submitForm = async formName => {
      form.value.validate(async valid => {
        if (valid) {
          let res = {}
          console.log(this.$refs.culture_heritage_type.selected.value)
          heritage.heritage_type = this.$refs.culture_heritage_type.selected.value
          if (props.editBookId) {
            res = await bookModel.editBook(props.editBookId, heritage)
            context.emit('editClose')
          } else {
            res = await bookModel.createBook(heritage)
            resetForm(formName)
          }
          if (res.code < window.MAX_SUCCESS_CODE) {
            ElMessage.success(`${res.message}`)
          }
        } else {
          console.error('error submit!!')
          ElMessage.error('请将信息填写完整')
        }
      })
    }

    const back = () => {
      context.emit('editClose')
    }

    return {
      back,
      // book,
      form,
      rules,
      resetForm,
      submitForm,
      heritage
    }
  },
}

/**
 * 表单验证规则
 */
function getRules() {
  /**
   * 验证回调函数
   */
  const checkInfo = (rule, value, callback) => {
    if (!value) {
      callback(new Error('信息不能为空'))
    }
    callback()
  }
  const rules = {
    // title: [{ validator: checkInfo, trigger: 'blur', required: true }],
    // author: [{ validator: checkInfo, trigger: 'blur', required: true }],
    // summary: [{ validator: checkInfo, trigger: 'blur', required: true }],
    // image: [{ validator: checkInfo, trigger: 'blur', required: true }],
    name: [{ validator: checkInfo, trigger: 'blur', required: true }],
    heritage_type: [{ validator: checkInfo, trigger: 'blur', required: true }],
    // is_digital: [{ validator: checkInfo, trigger: 'blur', required: true }],
    location: [{ validator: checkInfo, trigger: 'blur', required: true }],
    protection_unit: [{ validator: checkInfo, trigger: 'blur', required: true }],
    summary: [{ validator: checkInfo, trigger: 'blur', required: true }]
  }
  return { rules }
}
</script>

<style lang="scss" scoped>
.container {
  .title {
    height: 59px;
    line-height: 59px;
    color: $parent-title-color;
    font-size: 16px;
    font-weight: 500;
    text-indent: 40px;
    border-bottom: 1px solid #dae1ec;

    .back {
      float: right;
      margin-right: 40px;
      cursor: pointer;
    }
  }

  .wrap {
    padding: 20px;
  }

  .submit {
    float: left;
  }
}
</style>
