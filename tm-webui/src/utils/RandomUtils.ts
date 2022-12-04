export class RandomUtils {
    public static getKey(): string {
        let key = new Date().getTime() + '';
        key += Math.random().toString().substring(2);
        key += Math.random().toString().substring(2);
        return key;
    }
}

