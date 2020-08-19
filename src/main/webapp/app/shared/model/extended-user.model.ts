import { IUser } from 'app/shared/model/user.model';
import { IContact } from 'app/shared/model/contact.model';
import { IAddress } from 'app/shared/model/address.model';
import { IInstitute } from 'app/shared/model/institute.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IExtendedUser {
  id?: number;
  photoUrl?: string;
  user?: IUser;
  contacts?: IContact[];
  addresses?: IAddress[];
  institute?: IInstitute;
  group?: IGroup;
}

export const defaultValue: Readonly<IExtendedUser> = {};
