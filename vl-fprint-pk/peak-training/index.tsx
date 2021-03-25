import * as React from "react"
import * as ReactDOM from "react-dom"
import * as echarts from "echarts"

const req: RequestInit = {headers: {"Content-Type": "application/json", "Accept": "application/json"}}

const load = (path: string) => fetch(path, req).then(res => res.json())
class App extends React.Component<{}, {trainData: any, counts: Map<string, number>}> {

  public componentDidMount() {
    load("./peaks-detected.json").then(trainData => {
      const sources: any[] = trainData.sources
      const anchors = sources.flatMap(src => src.anchors as any[])
      const counts = new Map<string, number>()

      counts.set("Transient", 0)
      counts.set("TonalStraight", 0)
      counts.set("TonalShift", 0)
      anchors.forEach(anc => {
        const i = counts.get(anc.type)
        counts.set(anc.type, i + 1)
      })
      this.setState({trainData, counts})
    })
  }

  private idOf(src: any, anchor: any) {
    return `${src.file}-${anchor.x}-${anchor.y}`
  }

  private renderAnchor(src: any, anchor: any) {
    const y1 = 127 - anchor.y
    return (
      <div>
        <div id={this.idOf(src, anchor)} style="width: 240px; height: 256px; margin-left: auto; margin-right: auto">
          <div style="padding: 0px; margin: 0px; border-width: 0px; cursor: default;">
            <canvas></canvas>
          </div>
        </div>
        <div style="margin-bottom: 32px;">
          {anchor.valid ? `✅` : `❌`} {anchor.x}, {anchor.y}/{y1} - {anchor.type}
        </div>
      </div>
    )
  }

  private renderSource(src: any) {
    const anchors: any[] = src.anchors
    return (
      <div id={src.file} style="text-align: center">
        <h3>{src.file}</h3>
        <div className="frow">
          {anchors.map(anc => (
            <div className="col-md-1-4">
              {this.renderAnchor(src, anc)}
            </div>
          ))}
        </div>
      </div>
    )  
  }

  private init(src: any) {
    const anchors: any[] = src.anchors
    anchors.forEach(anc => {
      const dom = document.getElementById(this.idOf(src, anc)) as HTMLDivElement
      if (dom) {
        const chart: any = echarts.init(dom)
        const data = []
        for (let i = 0; i < anc.region.length; i++) {
          for (let j = 0; j < anc.region[0].length; j++) {
            data.push([i, j, anc.region[i][j]])
          }
        }
        const option = {
          tooltip: {}, xAxis: {type: "category"}, yAxis: {type: "category"},
          visualMap: {
            show: false, min: 0, max: 0.25,
            calculable: true,
            inRange: {
              color: [
                "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026"
              ]
            }
          },  
          series: [{
            name: `${anc.x}, ${anc.y} - ${anc.type}`,
            type: "heatmap", data,
            emphasis: {itemStyle: {borderColor: "#333", borderWidth: 1}},
            animation: false
          }]
        }
        chart.setOption(option)
      }
    })

    const countsDom = document.getElementById("counts") as HTMLDivElement
    if (countsDom) {
      const countsChart: any = echarts.init(countsDom)
      countsChart.setOption({
        xAxis: {type: "category", data: [...this.state.counts.keys()]},
        yAxis: {type: "value"},
        series: [{data: [...this.state.counts.values()], type: "bar"}]
      })  
    }
  }

  public render() {
    const {trainData} = this.state
    if (trainData) {
      const sources: any[] = trainData.sources
      const divs = sources.map(src => this.renderSource(src))
      sources.forEach(src => this.init(src))
      return (
        <div>
          <button style="margin-right: 0px; margin-left: auto" onClick={() => this.forceUpdate()}>
            Update
          </button>
          <div id="counts" style="width: 800px; height: 256px; margin-left: auto; margin-right: auto"/>
          {divs}
        </div>
      )
    }
    return <div>loading</div>
  }

}

ReactDOM.render(<App />, document.getElementById("root"))
