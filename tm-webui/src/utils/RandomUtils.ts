export class RandomUtils {
    public static getKey(): string {
        let key = new Date().getTime() + '';
        key += Math.ceil(Math.random()*100000000);
        key += Math.ceil(Math.random()*100000000);
        return key;
    }
}

