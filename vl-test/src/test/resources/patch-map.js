var data = [%s];

option = {
  tooltip: {},
  xAxis: {type: "category"},
  yAxis: {type: "category"},
  visualMap: {
    min: %s,  max: %s,
    calculable: true,
    realtime: false,
    inRange: {
      color: [
        "#313695", "#4575b4", "#74add1", "#abd9e9",
        "#e0f3f8", "#ffffbf", "#fee090", "#fdae61",
        "#f46d43", "#d73027", "#a50026"
      ]
    }
  },
  series: [{
    name: "Patch",
    type: "heatmap",
    data: data,
    emphasis: {itemStyle: {borderColor: "#333", borderWidth: 1}},
    progressive: 1000,
    animation: false
  }]
};
