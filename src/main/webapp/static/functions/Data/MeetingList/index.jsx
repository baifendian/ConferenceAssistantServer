import React, { PropTypes } from 'react'
import { Link } from 'react-router'
import Button from 'bfd-ui/lib/Button'
import { Select, Option } from 'bfd-ui/lib/Select'
import Fetch from 'bfd-ui/lib/Fetch'


import Task from 'public/Task'
import env from '../../../env'
import './index.less'

export default React.createClass({
  contextTypes: {
    history: PropTypes.object.isRequired
  },
	getInitialState() {
    return {
     url: "Data/MeetingList.json",
     data:[],
     type:0,
     time:0
    };
	},
  handleSuccess(res) {
    const data = res;
    this.setState({
      data:data
    });
  },
  handleJump(item) {
    this.context.history.push('/Data/MeetingDetail/'+item.id);
  },
  hanleChangeType(item){
    this.setState({
      type:item,
      url: "data/meetingList2.json",
    });
  },
  hanleChangeTime(item){
    this.setState({
      time:item,
      url: "data/meetingList3.json",
    });
  },
  render() {
    const _this = this;
    function Jump(item) {
      _this.handleJump(item);
    }
    return (
      <div className="fun-meetList">
        <h2>会议列表</h2>
        <div className="fun-content">
        	<div className="fun-meetList-select">
        		<Select defaultValue="0" onChange={this.hanleChangeType}>
			        <Option value="0" >全部</Option>
			        <Option value="1">我发起的</Option>
			        <Option value="2">我参与的</Option>
			        <Option value="3">分享</Option>
			      </Select>
			      <Select defaultValue="0" onChange={this.hanleChangeTime}>
			        <Option value="0" >最近一周</Option>
			        <Option value="1">最近一月</Option>
			        <Option value="2">最近一年</Option>
			        <Option value="3">全部</Option>
			      </Select>
        	</div>
          <div className="fun-meetList-content">
            <Fetch style={{minHeight:30}} url={this.state.url} onSuccess={this.handleSuccess}>
              {this.state.data.length?this.state.data.map((item, i) => <div key={i} className="fun-meet-list" style={i%2==0?{background: 'rgba(236, 240, 241, 0.43)'}:{background: '#fff'}}>
                <div className="fun-Mlist-title">
                  <a className="fun-title-main" onClick={() => {Jump(item)}}>{item.name}</a>
                  <span className={item.type==0?"green":"origin"}>{item.typeName}</span>
                  <div style={{float:'right'}}>{item.status}</div>
                </div>
                <p onClick={() =>{Jump(item)}}>{item.describe}</p>
                <div className="fun-Mlist-icon">
                  <span>{item.place}</span>
                  <span>{item.personnel}</span>
                  <div style={{float:'right'}}>{item.time}</div>
                </div>
              </div>):null}
            </Fetch>
          </div>
        </div>
      </div>
    )
  }
})