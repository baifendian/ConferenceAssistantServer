/**
 * 开发环境、线上环境的不同配置
 */

var env = {}

if (process.env.NODE_ENV === 'production') {

  /**
   * 线上环境
   */
  
  // 数据接口基础 URL
  env.baseUrl = '/api'

  // 页面根路径
  env.basePath = '/web-demo'

} else {

  /**
   * 开发环境
   */

  // 数据接口基础 URL
  env.baseUrl = 'http://localhost:4001'

  //env.localUrl = "http://localhost:4001/"

  // 页面根路径
  env.basePath = '/'

}

module.exports = env