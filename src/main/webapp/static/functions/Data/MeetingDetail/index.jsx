import React from 'react'
import Fetch from 'bfd-ui/lib/Fetch'

import env from '../../../env'
import Task from 'public/Task'
import './index.less'

export default React.createClass({
	getInitialState() {
    return {
      url: "data/meetingDetail.json",
   		data:[] 
    };
	},
	componentWillMount() {
	  const key = this.props.params.query;
	  console.log(key);
	},
	handleSuccess(res) {
		this.setState({data:res});
	},
  render() {
  	const data = this.state.data;
    return (
      <div className="function-detail">
        <Fetch style={{minHeight:30}} url={this.state.url} onSuccess={this.handleSuccess}>
        </Fetch>
        {data.id?<div className="fun-detail-content">
        	<div className="fun-detail-title">
 						<div className="pull-left">
 							<a className="fun-title-main">{data.name}</a>
              <span className={data.type==0?"green":"origin"}>{data.typeName}</span>
             
 						</div>
 						<div className="pull-right">
 							<span>{data.status==0?"未开始":data.status == 1?"进行中":"结束"}</span>
 						</div>
 						<div style={{clear:"both"}}></div>
        	</div>
        	<div className="fun-detail-main">
        		<ul>
        			<li>
        				<span>发起：</span>
        				<span>{data.Promoter}</span>
        			</li>
        			<li>
        				<span>参与：</span>
        				<span>{data.people.length?data.people.join(" , "):null}
        				</span>
        			</li>
        			<li>
        				<span>时间：</span>
        				<span>{data.time}</span>
        			</li>
        			<li>
        				<span>地点：</span>
        				<span>{data.place}</span>
        			</li>
        		</ul>
        		<div>
        			<h3>详细内容</h3>
        			<ul>
	        			<li>
	        				<span>内容：</span>
	        				<span>{data.content}</span>
	        			</li>
	        			
        			</ul>
        		</div>
        		<div>
        			<h3>文字记录</h3>
        			<ul>
	        			<li>
	        				<span>内容：</span>
	        				<span>{data.content}</span>
	        			</li>
        			</ul>
        		</div>
        	</div>
        </div>:null}
      </div>
    )
  }
})