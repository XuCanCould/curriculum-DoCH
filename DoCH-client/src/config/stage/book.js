const bookRouter = {
  route: null,
  name: null,
  // title: '图书管理',
  title: '文化遗产数字化管理',
  type: 'folder', // 类型: folder, tab, view
  icon: 'iconfont icon-tushuguanli',
  isElementIcon: false,
  filePath: 'view/book/', // 文件路径
  order: null,
  inNav: true,
  // children: [
  //   {
  //     title: '图书列表',
  //     type: 'view',
  //     name: 'BookCreate',
  //     route: '/book/list',
  //     filePath: 'view/book/book-list.vue',
  //     inNav: true,
  //     icon: 'iconfont icon-tushuguanli',
  //     isElementIcon: false,
  //   },
  //   {
  //     title: '添加图书',
  //     type: 'view',
  //     name: 'BookCreate',
  //     route: '/book/add',
  //     filePath: 'view/book/book.vue',
  //     inNav: true,
  //     icon: 'iconfont icon-add',
  //     isElementIcon: false,
  //   },
  children: [
    {
      title: '文化遗产列表',
      type: 'view',
      name: 'BookCreate',
      route: '/book/list',
      filePath: 'view/book/book-list.vue',
      inNav: true,
      icon: 'iconfont icon-tushuguanli',
      isElementIcon: false,
    },
    {
      title: '数字化项目列表',
      type: 'view',
      name: 'digitalAchives',
      route: '/digital-archives/list',
      filePath: 'view/book/digital-archives.vue',
      inNav: true,
      icon: 'iconfont icon-tushuguanli',
      isElementIcon: false,
    },
    {
      title: '添加文化遗产',
      type: 'view',
      name: 'BookCreate',
      route: '/heritage/add',
      filePath: 'view/book/book.vue',
      inNav: true,
      icon: 'iconfont icon-add',
      isElementIcon: false,
    },
  ],
}

export default bookRouter
