import * as React from "react"
import * as ReactDOM from "react-dom"
import * as echarts from "echarts"

const req: RequestInit = {headers: {"Content-Type": "application/json", "Accept": "application/json"}}

class App extends React.Component {

  public componentDidMount() {
    fetch("./peak-training.json", req)
      .then(res => res.json())
      .then(json => { console.log(json) });
  }

  public render() {
    return (
      <div>
        <div className="frow">
          COOL
        </div>
      </div>
    )
  }

}

ReactDOM.render(<App />, document.getElementById("root"))
