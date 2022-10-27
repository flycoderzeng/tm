import moment from "moment";

export class DateUtils {
    public static format(time: any, format?: string): string {
        if(!time) {
            return '';
        }
        if(!format) {
            format = 'YYYY-MM-DD HH:mm:ss';
        }
        return moment(new Date(time)).format(format);
    }
}
