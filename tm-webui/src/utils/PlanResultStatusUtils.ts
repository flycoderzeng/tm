export class PlanResultStatusUtils {
    // 结果状态1-初始化中2-任务初始化完毕，等待调度执行3-执行中4-执行完成5-暂停中6-用户停止执行 99-异常结束
    public static PLAN_RESULT_STATUS_MAP: any = {
        1: '初始化中', 2: '任务初始化完毕，等待调度执行', 3: '执行中', 4: '执行完成', 5: '暂停中', 6: '用户停止执行',
        7: '任务队列已满', 8: '计划前用例执行失败', 9: '执行计划前用例中', 10: '有用例执行失败,停止执行', 99: '异常结束'
    }

    // 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止
    public static CASE_RESULT_STATUS_MAP: any = {
        1: '执行中', 2: '成功', 200: '失败', 190: '执行超时', 100: '用户停止'
    }

    // 1-执行中 2-执行成功（没有发生异常，断言都成功） 300-执行失败（发生异常或断言失败） 270-执行超时，强制结束

    public static getPlanResultStatusDescription(resultStatus: number): string {
        return PlanResultStatusUtils.PLAN_RESULT_STATUS_MAP[resultStatus];
    }

    public static getCaseResultStatusDescription(resultStatus: number): string {
        return PlanResultStatusUtils.CASE_RESULT_STATUS_MAP[resultStatus];
    }
}
