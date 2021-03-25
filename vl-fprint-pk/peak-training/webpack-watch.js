const express = require("express")
const webpack = require("webpack")
const wpc = require("./webpack.config")

const compiler = webpack(wpc)
const app = express()

compiler.watch({}, (err, stats) => {
  console.log(stats.toString())
})
app.use(express.static("."))
app.listen(8080)
