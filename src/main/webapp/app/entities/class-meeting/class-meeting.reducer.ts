import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IClassMeeting, defaultValue } from 'app/shared/model/class-meeting.model';

export const ACTION_TYPES = {
  FETCH_CLASSMEETING_LIST: 'classMeeting/FETCH_CLASSMEETING_LIST',
  FETCH_CLASSMEETING: 'classMeeting/FETCH_CLASSMEETING',
  CREATE_CLASSMEETING: 'classMeeting/CREATE_CLASSMEETING',
  UPDATE_CLASSMEETING: 'classMeeting/UPDATE_CLASSMEETING',
  DELETE_CLASSMEETING: 'classMeeting/DELETE_CLASSMEETING',
  RESET: 'classMeeting/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IClassMeeting>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ClassMeetingState = Readonly<typeof initialState>;

// Reducer

export default (state: ClassMeetingState = initialState, action): ClassMeetingState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CLASSMEETING_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CLASSMEETING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CLASSMEETING):
    case REQUEST(ACTION_TYPES.UPDATE_CLASSMEETING):
    case REQUEST(ACTION_TYPES.DELETE_CLASSMEETING):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CLASSMEETING_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CLASSMEETING):
    case FAILURE(ACTION_TYPES.CREATE_CLASSMEETING):
    case FAILURE(ACTION_TYPES.UPDATE_CLASSMEETING):
    case FAILURE(ACTION_TYPES.DELETE_CLASSMEETING):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLASSMEETING_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CLASSMEETING):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CLASSMEETING):
    case SUCCESS(ACTION_TYPES.UPDATE_CLASSMEETING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CLASSMEETING):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/class-meetings';

// Actions

export const getEntities: ICrudGetAllAction<IClassMeeting> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CLASSMEETING_LIST,
    payload: axios.get<IClassMeeting>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IClassMeeting> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CLASSMEETING,
    payload: axios.get<IClassMeeting>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IClassMeeting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CLASSMEETING,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IClassMeeting> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CLASSMEETING,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IClassMeeting> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CLASSMEETING,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
