import { Moment } from 'moment';
import { IComment } from 'app/shared/model/comment.model';
import { IAssistance } from 'app/shared/model/assistance.model';
import { ClassType } from 'app/shared/model/enumerations/class-type.model';

export interface IClassMeeting {
  id?: number;
  name?: string;
  classType?: ClassType;
  date?: string;
  comments?: IComment[];
  assistances?: IAssistance[];
}

export const defaultValue: Readonly<IClassMeeting> = {};
