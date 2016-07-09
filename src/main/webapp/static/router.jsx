/**
 * 前端路由配置，也是所有页面的入口。
 * 注意：require.ensure 实现代码按页面分割，动态加载，详细参考 webpack 文档
 */

import React from 'react'
import { render } from 'react-dom'
import { Router, Route, IndexRoute } from 'react-router'
import { createHistory } from 'history'
import env from './env'
import App from './App'

export default render((
  <Router onUpdate={() => window.scrollTo(0, 0)} history={createHistory()}>
    <Route path={env.basePath} component={App}>
      <IndexRoute getComponent={(location, cb) => {
        require.ensure([], require => {
          cb(null, require('./functions/Data/MeetingList').default)
        })
      }}/>
      <Route path="Data">
        <Route path="MeetingList" getComponent={(location, cb) => {
          require.ensure([], require => {
            cb(null, require('./functions/Data/MeetingList').default)
          })
        }}/>
        <Route path="MeetingDetail/:query" getComponent={(location, cb) => {
          require.ensure([], require => {
            cb(null, require('./functions/Data/MeetingDetail').default)
          })
        }}/>
        <Route path="ContactList" getComponent={(location, cb) => {
          require.ensure([], require => {
            cb(null, require('./functions/Data/ContactList').default)
          })
        }}/>
        <Route path="Stay" getComponent={(location, cb) => {
          require.ensure([], require => {
            cb(null, require('./functions/Data/Stay').default)
          })
        }}/>
        <Route path="Chart" getComponent={(location, cb) => {
          require.ensure([], require => {
            cb(null, require('./functions/Data/Chart').default)
          })
        }}/>
      </Route>

      <Route path="Login" getComponent={(location, cb) => {
        require.ensure([], require => {
          cb(null, require('./functions/login').default)
        })
      }}/>
    
      <Route path="*" getComponent={(location, cb) => {
        require.ensure([], require => {
          cb(null, require('./functions/NotFound').default)
        })
      }}/>
    </Route>



  </Router>
), document.getElementById('app'))