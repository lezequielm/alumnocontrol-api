import { IClassMeeting } from 'app/shared/model/class-meeting.model';

export interface IComment {
  id?: number;
  title?: string;
  text?: any;
  classMeeting?: IClassMeeting;
}

export const defaultValue: Readonly<IComment> = {};
