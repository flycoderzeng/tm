import React, { Component } from 'react';
import { HashRouter } from 'react-router-dom';
import { routes } from "./config/route.config";
import { renderRoutes } from 'react-router-config';


class App extends Component {
  componentDidMount() {

  }
  render() {
    return (
        <HashRouter>
          {renderRoutes(routes)}
        </HashRouter>
    )
  }
}

export default App;
