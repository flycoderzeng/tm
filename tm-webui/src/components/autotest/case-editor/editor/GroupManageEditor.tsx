import React, {useState} from "react";
interface IState {
    groupVariables: string | null | undefined;
}


const GroupManageEditor: React.FC<IState> = (props) => {
    const [groupVariables, setGroupVariables] = useState(props.groupVariables);
    if(groupVariables !== props.groupVariables) {
        setGroupVariables(props.groupVariables);
    }

    return (
        <div></div>
    )
}

export {GroupManageEditor};
