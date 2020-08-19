import { IDocument } from 'app/shared/model/document.model';
import { IStudent } from 'app/shared/model/student.model';
import { IAssistance } from 'app/shared/model/assistance.model';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { IInstitute } from 'app/shared/model/institute.model';

export interface IGroup {
  id?: number;
  name?: string;
  enabled?: boolean;
  requestedDocuments?: IDocument[];
  students?: IStudent[];
  assistances?: IAssistance[];
  users?: IExtendedUser[];
  institute?: IInstitute;
}

export const defaultValue: Readonly<IGroup> = {
  enabled: false,
};
