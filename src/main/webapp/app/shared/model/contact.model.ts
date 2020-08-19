import { IStudent } from 'app/shared/model/student.model';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { ContactType } from 'app/shared/model/enumerations/contact-type.model';

export interface IContact {
  id?: number;
  contactType?: ContactType;
  data?: string;
  order?: number;
  student?: IStudent;
  user?: IExtendedUser;
}

export const defaultValue: Readonly<IContact> = {};
