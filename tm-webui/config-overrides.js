/* config-overrides.js */
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');
module.exports = function override(config, env) {
    //do stuff with the webpack config...
    config.mode = "development";
    config.plugins.push(new MonacoWebpackPlugin(['java', 'json', 'shell', 'plaintext',
        'python', 'javascript', 'sql', 'typescript', 'xml', 'json', 'php', 'lua']));
    return config;
}
