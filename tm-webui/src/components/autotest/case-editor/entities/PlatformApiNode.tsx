import {BaseNode} from "./BaseNode";
import {BaseNameValue} from "../../../common/entities/BaseNameValue";

export interface PlatformApiNode extends BaseNode {
    platformApiId: number;
    parametricList: BaseNameValue[];
}
