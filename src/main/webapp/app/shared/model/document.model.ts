import { Moment } from 'moment';
import { IGroup } from 'app/shared/model/group.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IDocument {
  id?: number;
  name?: string;
  requestDate?: string;
  uploadDate?: string;
  fileUrl?: string;
  sent?: boolean;
  group?: IGroup;
  student?: IStudent;
}

export const defaultValue: Readonly<IDocument> = {
  sent: false,
};
