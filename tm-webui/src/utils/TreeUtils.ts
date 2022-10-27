export class TreeUtils {
    public static getAllNonLeafKeys(steps: any[]): string[] {
        const keys: string[] = [];

        const loop = (step: any) => {
            if(step.children && step.children.length > 0) {
                keys.push(step.key);
                for (let i = 0; i < step.children.length; i++) {
                    loop(step.children[i]);
                }
            }
        }

        loop(steps[0]);

        return keys;
    }
}
