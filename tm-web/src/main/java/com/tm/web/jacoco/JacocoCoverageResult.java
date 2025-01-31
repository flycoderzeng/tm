package com.tm.web.jacoco;

import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Jacoco是从代码指令（Instructions， Coverage），分支（Branches， Coverage），圈复杂度（Cyclomatic Complexity），行（Lines），方法（Methods），类（Classes）等维度进行分析的。
 * Missed Instructions cov指令覆盖，字节码中指令
 * 1、方法里所有的代码行都有覆盖到(都覆盖了并不代表100%覆盖，会存在分支没有覆盖完整的情况)
 * 2、类下面所有方法都有覆盖
 * 3、包下面的类都有覆盖
 * Missed Branches cov分支覆盖率
 * 1、对所有的if和switch指令计算了分支覆盖率
 * 2、用钻石表示。分支覆盖不能看行
 * Missed Cxty圈复杂度
 * 1、Jacoco为每个非抽象方法计算圈复杂度，并也会计算每个类，包，组的复杂度。根据McCabe1996的定义，圈复杂度可以理解为覆盖所有的可能情况最少使用的测试用例数
 * 2、V(G)=区域数=判定节点数+1.
 * while,for,if,switch每个都是一个判定节点。嵌套的都是加1
 * Missed Methods
 * 方法每一个非抽象方法都至少有一条指令。若一个方法至少被执行了一条指令，就认为它被执行过。
 * Missed Classes
 * 类每个类中只要有一个方法被执行，这个类就被认定为被执行。同Missed Methods一样，有些没有在源码声明的方法被执行，也认定该类被执行。
 * Missed Lines
 * 代码行用背景色标识的都算是行统计的目标，变量定义不算行，else也不算。
 * 注：Missed表示未覆盖。
 */
@Data
public class JacocoCoverageResult {
    private String name;
    private List<Counter> counters;
    private List<PackageCoverageResult> packageCoverageResults;

    public JacocoCoverageResult() {}

    public JacocoCoverageResult(String name) {
        this.name = name;
    }

    public Integer getMissedInstructions() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.INSTRUCTION) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredInstructions() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.INSTRUCTION) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    public Integer getMissedBranches() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.BRANCH) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredBranches() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.BRANCH) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    public Integer getMissedCxty() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.COMPLEXITY) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredCxty() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.COMPLEXITY) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    public Integer getMissedClasses() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.CLASS) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredClasses() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.CLASS) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    public Integer getMissedMethods() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.METHOD) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredMethods() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.METHOD) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    public Integer getMissedLines() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.LINE) {
                    return counter.getMissed();
                }
            }
        }
        return null;
    }

    public Integer getCoveredLines() {
        if(counters != null) {
            for (Counter counter : counters) {
                if(counter.getType() == CounterType.LINE) {
                    return counter.getCovered();
                }
            }
        }
        return null;
    }

    @Data
    public static class PackageCoverageResult {
        private String name;
        private List<Counter> counters;
        private List<ClassCoverageResult> classCoverageResults;

        public PackageCoverageResult(String name) {
            this.name = name;
        }
    }

    @Data
    public static class ClassCoverageResult {
        private String name;
        private String sourceFileName;
        private List<Counter> counters;
        private List<MethodCoverageResult> methodCoverageResults;
        private List<LineCoverageResult> lineCoverageResults;
    }

    @Data
    public static class MethodCoverageResult {
        private String name;
        private String desc;
        private String line;
        private List<Counter> counters;
    }

    @Data
    public static class LineCoverageResult {
        private int nr;
        private int mi;
        private int ci;
        private int mb;
        private int cb;

        public LineCoverageResult(int nr, int mi, int ci, int mb, int cb) {
            this.nr = nr;
            this.mi = mi;
            this.ci = ci;
            this.mb = mb;
            this.cb = cb;
        }
    }

    @Data
    public static class Counter {
        private CounterType type;
        private int missed;
        private int covered;

        public Counter(CounterType type, int missed, int covered) {
            this.type = type;
            this.missed = missed;
            this.covered = covered;
        }
    }

    public enum CounterType {
        INSTRUCTION("INSTRUCTION"), BRANCH("BRANCH"), LINE("LINE"),
        COMPLEXITY("COMPLEXITY"), METHOD("METHOD"), CLASS("CLASS");
        String value;
        CounterType(String value) {this.value = value;}
        CounterType() {}
        public String value() {return value;}
        public static CounterType get(String value) {
            if (Objects.equals(value, "INSTRUCTION")) {
                return CounterType.INSTRUCTION;
            }
            if (Objects.equals(value, "BRANCH")) {
                return CounterType.BRANCH;
            }
            if (Objects.equals(value, "LINE")) {
                return CounterType.LINE;
            }
            if (Objects.equals(value, "COMPLEXITY")) {
                return CounterType.COMPLEXITY;
            }
            if (Objects.equals(value, "METHOD")) {
                return CounterType.METHOD;
            }
            if (Objects.equals(value, "CLASS")) {
                return CounterType.CLASS;
            }
            return null;
        }
    }
}
