import React, { PropTypes } from 'react'
import { Link } from 'react-router'
import Button from 'bfd-ui/lib/Button'
import { Select, Option } from 'bfd-ui/lib/Select'
import Fetch from 'bfd-ui/lib/Fetch'
import { Tabs, TabList, Tab, TabPanel } from 'bfd-ui/lib/Tabs'

import Task from 'public/Task'
import env from '../../../env'
import './index.less'

export default React.createClass({
  contextTypes: {
    history: PropTypes.object.isRequired
  },
	getInitialState() {
    return {
     url: "data/stayList.json",
     data:[]
    };
	},
  handleSuccess(res) {
    const data = res;
    this.setState({
      data:data
    });
  },
  handleJump(item) {
    this.context.history.push('/Data/stayDetail/'+item.id);
  },
  render() {
    const _this = this;
    function Jump(item) {
      _this.handleJump(item);
    }
    return (
      <div className="fun-stay">
        <h2>待办</h2>
          <label id="stay">待办</label><label id="done">已完成</label>
          <div className="fun-content">
              <div className="fun-stay-content">
                <Fetch style={{minHeight:30}} url={this.state.url} onSuccess={this.handleSuccess}>
                  {this.state.data.length?this.state.data.map((item, i) => 
                    <div key={i} className="stayDetail" style={i%2==0?{background: 'rgba(236, 240, 241, 0.43)'}:{background: '#fff'}}>
                      <div>
                        {item.type==0?<div className="imgstatus0"></div>:<div className="imgstatus1"></div>}
                        <div className="detail">
                          <p>{item.name}</p>
                          <div className="labelclass"><label>{item.time}</label><label>{item.title}</label></div>
                        </div>
                      </div>                      
                    </div>
                    ):null}
                </Fetch>
              </div>
            </div>        
      </div>
    )
  }
})