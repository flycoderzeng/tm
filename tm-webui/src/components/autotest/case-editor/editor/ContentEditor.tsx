import React, {useEffect, useState} from "react";
import MonacoEditor from "react-monaco-editor";
import {AutoCaseVariable} from "../entities/AutoCaseVariable";

interface IState {
    language: string;
    content: string;
    refreshContent: any;
    userDefinedVariables?: AutoCaseVariable[];
}


const ContentEditor: React.FC<IState> = (props) => {
    const [language, setLanguage] = useState(props.language);
    const [content, setContent] = useState(props.content);
    const {refreshContent} = props;

    useEffect(() => {
        setContent(props.content);
        setLanguage(props.language);
    }, [props.content, props.language]);

    const options = {
        selectOnLineNumbers: true,
        automaticLayout: true,
        renderSideBySide: false,
        contextmenu: false, // 禁止右键
        fixedOverflowWidgets: true, // 超出编辑器大小的使用fixed属性显示
        quickSuggestions: true, // 默认的提示关掉
        minimap: {
            // 缩略图
            enabled: false
        },
        wordWrap: 'on' as any,
    };

    function builtInSuggestions(monaco) {
        const suggestions: any = [];
        suggestions.push({
            label: '__request',
            kind: monaco.languages.CompletionItemKind.Variable,
            insertText: '${__request}',
            detail: '上一个请求包内容',
        });
        suggestions.push({
            label: '__response',
            kind: monaco.languages.CompletionItemKind.Variable,
            insertText: '${__response}',
            detail: '上一个响应包内容',
        });
        suggestions.push({
            label: '__response_status',
            kind: monaco.languages.CompletionItemKind.Variable,
            insertText: '${__response_status}',
            detail: '上一个请求的响应码',
        });
        const builtInFunctions: any = [{
            label: 'rand',
            insertText: '${rand()}',
            kind: monaco.languages.CompletionItemKind.Function,
            detail: '随机数',
            documentation: '返回一个介于0-1的随机数，double类型',
        }];

        return suggestions.concat(builtInFunctions);
    }

    function registerCompletionItemProvider(monaco, userDefinedVariables: AutoCaseVariable[]) {
        const suggestions: any[] = [];
        let count = userDefinedVariables.length;
        if(!count) {
            count = 0;
        }
        if(count < 1) {
            return [];
        }

        for (let i = 0; i < count; i++) {
            const temp = {};
            temp['label'] = userDefinedVariables[i].name;
            temp['kind'] = monaco.languages.CompletionItemKind.Variable;
            temp['insertText'] = '${' + userDefinedVariables[i].name + '}';
            temp['detail'] = userDefinedVariables[i].name;
            suggestions.push(temp);
        }
        const data = builtInSuggestions(monaco);
        for (let i = 0; i < data.length; i++) {
            suggestions.push(data[i]);
        }
        for (let i = 0; i < suggestions.length; i++) {
            delete suggestions[i]['range'];
        }
        const result: any[] = [];
        const h = {};
        for (let i = 0; i < suggestions.length; i++) {
            // 如果hash表中没有当前项
            if (!h[suggestions[i]['label']]) {
                // 存入hash表
                h[suggestions[i]['label']] = true;
                // 把当前数组的当前项push到临时数组里面
                result.push(suggestions[i]);
            }
        }
        return result;
    }

    function editorDidMountHandle(editor, monaco) {
        editor.focus();
        if(!props.userDefinedVariables) {
            return ;
        }
        const userDefinedVariables = props.userDefinedVariables || [];
        // const suggestions: any[] = registerCompletionItemProvider(monaco, userDefinedVariables);

        if (!window['isRegisterCompletionItemProvider']) {
            monaco.languages.registerCompletionItemProvider(props.language, {
                provideCompletionItems: function (model, position, context) {
                    return {
                        suggestions: registerCompletionItemProvider(monaco, userDefinedVariables)
                    };
                },
                //triggerCharacters: ['.']
            });
        }
        window['isRegisterCompletionItemProvider'] = true;
    }

    function onChangeHandle(value, e) {
        setContent(value);
        refreshContent(value);
    }

    return (<div style={{width: '100%', height: '600px'}} >
        <div style={{width: '100%', height: '600px'}} >
            <MonacoEditor
                width={'100%'}
                height={'100%'}
                theme="vs-dark"
                language={language}
                value={content}
                options={options}
                onChange={onChangeHandle}
                editorDidMount={editorDidMountHandle}
            />
        </div>
    </div>)
}

export {ContentEditor};
