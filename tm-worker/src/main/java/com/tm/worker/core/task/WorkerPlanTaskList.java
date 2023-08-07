package com.tm.worker.core.task;


import java.util.ArrayList;
import java.util.List;

public class WorkerPlanTaskList<T extends WorkerPlanTask> {
    private static final Integer MAX_TASKS = 100;
    private final List<T> planTaskList = new ArrayList<>();

    public WorkerPlanTaskList() {
        super();
    }

    public void add(T planTask) {
        synchronized (this) {
            planTaskList.add(planTask);
        }
    }

    public void sort() {
        synchronized (this) {
            planTaskList.sort(WorkerPlanTask.comparator);
        }
    }

    public T get(Integer index) {
        synchronized (this) {
            if(index < planTaskList.size()) {
                return planTaskList.get(index);
            }
        }
        return null;
    }

    public boolean canSubmitPlanTask() {
        synchronized (this) {
            if (planTaskList.size() >= MAX_TASKS) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        synchronized (this) {
            return planTaskList.isEmpty();
        }
    }

    public boolean remove(T planTask) {
        synchronized (this) {
            int index = -1;
            for (int i = 0; i < planTaskList.size(); i++) {
                if(planTaskList.get(i).getPlanExecuteResultId().equals(planTask.getPlanExecuteResultId())) {
                    index = i;
                    break;
                }
            }
            if(index > -1) {
                T remove = planTaskList.remove(index);
                return remove != null;
            }
        }
        return false;
    }

    public boolean remove(Integer planExecuteResultId) {
        synchronized (this) {
            int index = -1;
            for (int i = 0; i < planTaskList.size(); i++) {
                if(planTaskList.get(i).getPlanExecuteResultId().equals(planExecuteResultId)) {
                    index = i;
                    break;
                }
            }
            if(index > -1) {
                T remove = planTaskList.remove(index);
                return remove != null;
            }
        }
        return false;
    }

    public void stop(Integer planExecuteResultId) {
        int index = -1;
        for (int i = 0; i < planTaskList.size(); i++) {
            if(planTaskList.get(i).getPlanExecuteResultId().equals(planExecuteResultId)) {
                index = i;
                break;
            }
        }
        if(index > -1) {
            T t = planTaskList.get(index);
            if(t != null) {
                t.stop();
            }
        }
    }

    public void stopPassive(Integer planExecuteResultId) {
        int index = -1;
        for (int i = 0; i < planTaskList.size(); i++) {
            if(planTaskList.get(i).getPlanExecuteResultId().equals(planExecuteResultId)) {
                index = i;
                break;
            }
        }
        if(index > -1) {
            T t = planTaskList.get(index);
            if(t != null) {
                t.stopPassive();
            }
        }
    }
}
