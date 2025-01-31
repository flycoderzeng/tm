import Index from "../components/index/Index";
import Login from "../components/Login";
import Main from "../components/index/Main";
import MenuList from "../components/manage/menu/MenuList";
import MenuEdit from "../components/manage/menu/MenuEdit";
import UserList from "../components/manage/user/UserList";
import UserEdit from "../components/manage/user/UserEdit";
import ProjectList from "../components/manage/project/ProjectList";
import ProjectEdit from "../components/manage/project/ProjectEdit";
import RightList from "../components/manage/right/RightList";
import RightEdit from "../components/manage/right/RightEdit";
import RoleList from "../components/manage/role/RoleList";
import RoleEdit from "../components/manage/role/RoleEdit";
import RoleRightSetting from "../components/manage/role/RoleRightSetting";
import UserRoleSetting from "../components/manage/user/UserRoleSetting";
import ProjectUserSetting from "../components/manage/project/ProjectUserSetting";
import CommonNodeManageLayout from "../components/common/CommonNodeManageLayout";
import ShellEdit from "../components/autotest/shell/ShellEdit";
import CronJobList from "../components/testmanage/cronjob/CronJobList";
import RunEnvList from "../components/testmanage/runenv/RunEnvList";
import RunEnvEdit from "../components/testmanage/runenv/RunEnvEdit";
import {CronJobEdit} from "../components/testmanage/cronjob/CronJobEdit";
import UrlConfigList from "../components/testmanage/urlconfig/UrlConfigList";
import {UrlConfigEdit} from "../components/testmanage/urlconfig/UrlConfigEdit";
import {DbConfigEdit} from "../components/testmanage/dbconfig/DbConfigEdit";
import DbConfigList from "../components/testmanage/dbconfig/DbConfigList";
import {PlanResultPage} from "../components/autotest/planresult/PlanResultPage";
import MockInstancesList from "../components/mock/instances/MockInstancesList";
import HttpMockRuleList from "../components/mock/rules/HttpMockRuleList";
import {HttpMockRuleEdit} from "../components/mock/rules/HttpMockRuleEdit";
import DcnConfigList from "../components/testmanage/dcnconfig/DcnConfigList";
import {DcnConfigEdit} from "../components/testmanage/dcnconfig/DcnConfigEdit";
import TagList from "../components/testmanage/tags/TagList";
import TagEdit from "../components/testmanage/tags/TagEdit";
import {CoverageList} from "../components/testmanage/coverage/CoverageList";
import {CoverageInfo} from "../components/testmanage/coverage/CoverageInfo";

const routes = [
    {
        path: "/login",
        component: Login,
        exact: true,
    },
    {
        path: "/coverage/:id",
        component: CoverageInfo
    },
    {
        path: "/",
        component: Index,
        children: [
            {
                path: "/",
                component: Main,
                exact: true,
            },
            {
                path: "/main",
                component: Main
            },
            {
                path: "/menulist",
                component: MenuList
            },
            {
                path: "/menuedit/:id",
                component: MenuEdit
            },
            {
                path: "/userlist",
                component: UserList
            },
            {
                path: "/useredit/:id",
                component: UserEdit
            },
            {
                path: "/projectlist",
                component: ProjectList
            },
            {
                path: "/projectedit/:id",
                component: ProjectEdit
            },
            {
                path: "/rightlist",
                component: RightList
            },
            {
                path: "/rightedit/:id",
                component: RightEdit
            },
            {
                path: "/rolelist",
                component: RoleList
            },
            {
                path: "/roleedit/:id",
                component: RoleEdit
            },
            {
                path: "/rolerightsetting/:id",
                component: RoleRightSetting
            },
            {
                path: "/userrolesetting/:id",
                component: UserRoleSetting
            },
            {
                path: "/projectusersetting/:id",
                component: ProjectUserSetting
            },
            {
                path: '/nodemanage/:dataTypeId',
                component: CommonNodeManageLayout,
                children: [
                    {path: '/script/:id', component: ShellEdit}
                ]
            },
            {
                path: "/cronjoblist",
                component: CronJobList
            },
            {
                path: "/coveragelist",
                component: CoverageList
            },
            {
                path: "/cronjobedit/:id",
                component: CronJobEdit
            },
            {
                path: "/runenvlist",
                component: RunEnvList
            },
            {
                path: "/runenvedit/:id",
                component: RunEnvEdit
            },
            {
                path: "/urlconfig",
                component: UrlConfigList
            },
            {
                path: "/urlconfigedit/:id/:copy",
                component: UrlConfigEdit
            },
            {
                path: "/urlconfigedit/:id",
                component: UrlConfigEdit
            },
            {
                path: "/dbconfig",
                component: DbConfigList
            },
            {
                path: "/dbconfigedit/:id/:copy",
                component: DbConfigEdit
            },
            {
                path: "/dbconfigedit/:id",
                component: DbConfigEdit
            },
            {
                path: "/dcnlist",
                component: DcnConfigList
            },
            {
                path: "/dcnconfigedit/:id",
                component: DcnConfigEdit
            },
            {
                path: "/tags",
                component: TagList
            },
            {
                path: "/tagedit/:id",
                component: TagEdit
            },
            {
                path: "/planresult/:planOrCaseId/:fromType/:planCaseType/:planResultId",
                component: PlanResultPage
            },
            {
                path: "/planresult/:planOrCaseId/:fromType/:planCaseType",
                component: PlanResultPage
            },
            {
                path: '/mockinstances',
                component: MockInstancesList
            },
            {
                path: '/mockrules',
                component: HttpMockRuleList
            },
            {
                path: '/httpmockruleedit/:id',
                component: HttpMockRuleEdit
            },
        ]
    },
];
export {routes};
