export class MathUtils {
    public static isNumberSequence(str: string): boolean {
        const exp = /^\d{1,}$/;
        return !exp.test(str) ? false : true;
    }
}
