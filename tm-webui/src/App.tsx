import React, { Component } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { routes } from "./config/route.config";
import { renderRoutes } from 'react-router-config';


class App extends Component {
  componentDidMount() {

  }
  render() {
    return (
        <BrowserRouter>
          {renderRoutes(routes)}
        </BrowserRouter>
    )
  }
}

export default App;
