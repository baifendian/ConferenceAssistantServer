import React, { PropTypes } from 'react'
import { Link } from 'react-router'
import Button from 'bfd-ui/lib/Button'
import { Select, Option } from 'bfd-ui/lib/Select'
import Fetch from 'bfd-ui/lib/Fetch'
import ClearableInput from 'bfd-ui/lib/ClearableInput'
import Icon from 'bfd-ui/lib/Icon'
import Task from 'public/Task'
import env from '../../../env'
import './index.less'

export default React.createClass({
  contextTypes: {
    history: PropTypes.object.isRequired
  },
	getInitialState() {
    return {
     url: "data/ContactList.json",
     detailUrl:"data/contactDetail.json",
     data:[],
     detailData:[]
    };
	},
  handleSuccess(res) {
    const data = res;
    this.setState({
      data:data
    })
  },
  handleDetailSuccess(res) {
    const detaildata = res;
    this.setState({
      detailData:res
    })
  },
  handleJump(item) {
    this.context.history.push('/Data/ContactList/'+item.id);
  },
  hanleChange(item) {
    this.setState({
      url:"data/ContactList.json?name="+item
    })
  },
  hanleClick(item) {
    this.setState({
      detailData:item
    })
  },
  render() {
    const _this = this;
    const detailData = this.state.detailData;
    function Jump(item) {
      _this.handleJump(item);
    }
    return (
      <div className="fun-contract-page">
        <h2>通讯录</h2>
        <div className="fun-left">
          <div className="find-condition">
          	<ClearableInput id="sousuo" className="sousuo" placeholder="输入联系人名称进行搜索" onChange={this.hanleChange}></ClearableInput>
          </div>
          <Fetch style={{minHeight:30}} url={this.state.url} onSuccess={this.handleSuccess}>
              {this.state.data.length?this.state.data.map((item, i) => 
                <li key={i} className="contract-list" onClick={this.hanleClick.bind(this, item)}>
		          	<img src={require('public/c.png')}/>
		          	<label>{item.name}</label>
		          	<hr/>
		        </li>):null}
          </Fetch>
        </div>
        <div className="fun-right">
        <Fetch style={{minHeight:100}} url={this.state.detailUrl} onSuccess={this.handleDetailSuccess}>
          <img src={require('public/4.png')}/>
              <div className="userDetailInfo">
                <div className="userName"><label>姓名:</label><label>{detailData.name}</label></div>
                <div className="userEmail"><label>邮箱:</label><label>{detailData.email}</label></div>
                <div className="userQQ"><label>  QQ:</label><label>{detailData.QQ}</label></div>
              </div>
              <hr/>
              <div>
                <label id="title">备注:</label><label id="remark">{detailData.remark}</label>
              </div>
        </Fetch>              
        </div>               
      </div>
    )
  }
})