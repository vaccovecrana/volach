const path = require("path")

const nodeModules = path.resolve(__dirname, "node_modules")
const buildPath = path.resolve(__dirname, ".")
const wpc = {
  mode: "development",
  entry: {main: "./index.tsx"},
  devtool: "source-map",
  output: {filename: "[name].js", path: buildPath},
  module: {rules: [{test: /\.tsx?$/, use: ["ts-loader"], exclude: nodeModules}]},
  resolve: {
    extensions: [".js", ".jsx"],
    modules: [nodeModules, path.resolve(__dirname)],
    alias: {
      react: path.resolve(nodeModules, "preact/compat"),
      "react-dom": path.resolve(nodeModules, "preact/compat")
    }
  }
}

module.exports = wpc