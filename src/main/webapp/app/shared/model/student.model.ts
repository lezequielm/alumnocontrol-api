import { Moment } from 'moment';
import { IContact } from 'app/shared/model/contact.model';
import { IDocument } from 'app/shared/model/document.model';
import { IAssistance } from 'app/shared/model/assistance.model';
import { IAddress } from 'app/shared/model/address.model';
import { IInstitute } from 'app/shared/model/institute.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IStudent {
  id?: number;
  firstName?: string;
  lastName?: string;
  idNumber?: string;
  birthDate?: string;
  enabled?: boolean;
  photoUrl?: string;
  contacts?: IContact[];
  documents?: IDocument[];
  assistances?: IAssistance[];
  addresses?: IAddress[];
  institute?: IInstitute;
  group?: IGroup;
}

export const defaultValue: Readonly<IStudent> = {
  enabled: false,
};
