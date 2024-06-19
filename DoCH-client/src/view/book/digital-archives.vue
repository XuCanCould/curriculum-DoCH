<template>
  <div>
    <!-- 列表页面 -->
    <div class="container" v-if="!showEdit">
      <div class="header">
        <div class="title">已收录的数字化文化遗产列表</div>
      </div>
      <!-- 表格 -->
      <el-table :data="books" v-loading="loading">
        <el-table-column type="index" :index="indexMethod" label="序号" width="80"></el-table-column>
        <el-table-column prop="archive_name" label="项目名称"></el-table-column>
        <el-table-column prop="archive_type" label="项目类型"></el-table-column>
        <el-table-column prop="archive_path" label="访问">
          <template #default="scope">
            <a :href="scope.row.archive_path" target="_blank">🖥️📡</a>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="300">
          <template #default="scope">
            <el-button plain size="small" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
            <el-button
              plain
              size="small"
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-permission="{ permission: '删除图书', type: 'disabled' }"
              >删除</el-button>

            <!-- 添加背景图片动画 -->
            <div class="table-row-image-container"></div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑页面 -->
    <DigitalArchiveModify v-else @editClose="editClose" :editBookId="editBookId"></DigitalArchiveModify>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import bookModel from '@/model/digital-archives'
import DigitalArchiveModify from './edit-archives.vue'

export default {
  components: {
    DigitalArchiveModify,
  },
  setup() {
    const books = ref([])
    const editBookId = ref(1)
    const loading = ref(false)
    const showEdit = ref(false)

    onMounted(() => {
      getBooks()
    })

    const getBooks = async () => {
      try {
        loading.value = true
        books.value = await bookModel.getBooks()
        loading.value = false
      } catch (error) {
        loading.value = false
        if (error.code === 10020) {
          books.value = []
        }
      }
    }

    const handleEdit = id => {
      showEdit.value = true
      editBookId.value = id
    }

    const handleDelete = id => {
      ElMessageBox.confirm('此操作将永久删除该信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        const res = await bookModel.deleteBook(id)
        if (res.code < window.MAX_SUCCESS_CODE) {
          getBooks()
          ElMessage.success(`${res.message}`)
        }
      })
    }

    const editClose = () => {
      showEdit.value = false
      getBooks()
    }

    const indexMethod = index => index + 1

    return {
      books,
      loading,
      showEdit,
      editClose,
      handleEdit,
      editBookId,
      indexMethod,
      handleDelete,
    }
  },
}
</script>

<style lang="scss" scoped>
.container {
  padding: 0 30px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      height: 59px;
      line-height: 59px;
      color: $parent-title-color;
      font-size: 16px;
      font-weight: 500;
    }
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    margin: 20px;
  }

  // .el-table .el-table__row {
  //   height: 300px !important;
  // }

  /* 定义动画 */
  @keyframes fadeIn {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }

  /* 为表格行内的一个特定元素应用动画，这里假设为一个新增的div来包含背景图片 */
  .table-row-image-container {
    position: relative; /* 使元素可以承载绝对定位的子元素 */
    width: 260px; /* 背景图片容器的宽度，根据需要调整 */
    height: 50px; /* 背景图片容器的高度，根据需要调整 */
    overflow: hidden; /* 防止图片超出容器 */
  }

  .table-row-image-container::after {
    content: ''; /* 创建一个伪元素作为背景 */
    position: absolute; /* 绝对定位 */
    top: 0; /* 位置调整 */
    right: 0; /* 将背景图片定位在行的右侧 */
    width: 100%; /* 宽度覆盖容器 */
    height: 100%; /* 高度覆盖容器 */
    background-image: url('C:\\Users\\xu\\Pictures\\Screenshots\\谷歌翻译完全看透我的小心思.png'); /* 图片地址 */
    background-size: cover; /* 背景图片尺寸适应容器 */
    opacity: 0; /* 初始透明 */
    animation: fadeIn 1s ease forwards; /* 应用动画 */
  }
}
</style>
