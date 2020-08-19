import { IStudent } from 'app/shared/model/student.model';
import { IInstitute } from 'app/shared/model/institute.model';
import { IClassMeeting } from 'app/shared/model/class-meeting.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IAssistance {
  id?: number;
  present?: boolean;
  delayed?: boolean;
  justified?: boolean;
  justification?: any;
  student?: IStudent;
  institute?: IInstitute;
  classMeeting?: IClassMeeting;
  group?: IGroup;
}

export const defaultValue: Readonly<IAssistance> = {
  present: false,
  delayed: false,
  justified: false,
};
