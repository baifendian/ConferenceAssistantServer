import React from 'react'
import Task from 'public/Task'
import './index.less'
import ColumnChart from 'bfd-ui/lib/ColumnChart'
import Button from 'bfd-ui/lib/Button'

export default React.createClass({
  getInitialState() {
    return {
      url:"data/columnChart1.json",
      url2:"data/columnChart2.json",
      data:[],
      dayactive:1,
      weekactive:0,
      mouthactive:0,
      yearactive:0
    };
  },
  hanleClick(item) {
    if (item==1) {
      this.setState({
        dayactive:1,
        weekactive:0,
        mouthactive:0,
        yearactive:0        
      })
    }
    if (item==2) {
      this.setState({
        dayactive:0,
        weekactive:1,
        mouthactive:0,
        yearactive:0
      })
    }
    if (item==3) {
      this.setState({
        dayactive:0,
        weekactive:0,
        mouthactive:1,
        yearactive:0
      })
    }
    if (item==4) {
      this.setState({
        dayactive:0,
        weekactive:0,
        mouthactive:0,
        yearactive:1
      })
    }
  },
  render() {
    return (
      <div className="function-data-moduleA">
        <h1>报表</h1>
        <div className="buttons">
        	<Button type="primary" className={this.state.dayactive==1?'btngreen':'btnwrite'} onClick={this.hanleClick.bind(this, 1)}>日</Button>
          <Button type="primary" className={this.state.weekactive==1?'btngreen':'btnwrite'} id="week" onClick={this.hanleClick.bind(this, 2)}>周</Button>
          <Button type="primary" className={this.state.mouthactive==1?'btngreen':'btnwrite'} id="mouth" onClick={this.hanleClick.bind(this, 3)}>月</Button>
          <Button type="primary" className={this.state.yearactive==1?'btngreen':'btnwrite'} id="year" onClick={this.hanleClick.bind(this, 4)}>年</Button>
        </div>
        <div className="fun-chart">
        	<ColumnChart style={{height: 400}} category="name" cols={{x: '会议时常'}} url={this.state.url}/>
        </div>
        <div className="fun-chart">
        	<ColumnChart style={{height: 400}} category="name" cols={{x: '会议次数'}} url={this.state.url2}/>
        </div>
      </div>
    )
  }
})