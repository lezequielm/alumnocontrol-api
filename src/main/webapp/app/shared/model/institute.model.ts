import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { IStudent } from 'app/shared/model/student.model';
import { IGroup } from 'app/shared/model/group.model';
import { IAssistance } from 'app/shared/model/assistance.model';

export interface IInstitute {
  id?: number;
  name?: string;
  enabled?: boolean;
  users?: IExtendedUser[];
  students?: IStudent[];
  groups?: IGroup[];
  assistances?: IAssistance[];
}

export const defaultValue: Readonly<IInstitute> = {
  enabled: false,
};
