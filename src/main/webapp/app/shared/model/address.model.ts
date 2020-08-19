import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IAddress {
  id?: number;
  street?: string;
  number?: string;
  flat?: number;
  department?: string;
  postalCode?: string;
  order?: number;
  user?: IExtendedUser;
  student?: IStudent;
}

export const defaultValue: Readonly<IAddress> = {};
