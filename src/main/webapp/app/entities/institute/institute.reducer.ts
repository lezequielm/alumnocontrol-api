import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInstitute, defaultValue } from 'app/shared/model/institute.model';

export const ACTION_TYPES = {
  FETCH_INSTITUTE_LIST: 'institute/FETCH_INSTITUTE_LIST',
  FETCH_INSTITUTE: 'institute/FETCH_INSTITUTE',
  CREATE_INSTITUTE: 'institute/CREATE_INSTITUTE',
  UPDATE_INSTITUTE: 'institute/UPDATE_INSTITUTE',
  DELETE_INSTITUTE: 'institute/DELETE_INSTITUTE',
  RESET: 'institute/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInstitute>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type InstituteState = Readonly<typeof initialState>;

// Reducer

export default (state: InstituteState = initialState, action): InstituteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INSTITUTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INSTITUTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_INSTITUTE):
    case REQUEST(ACTION_TYPES.UPDATE_INSTITUTE):
    case REQUEST(ACTION_TYPES.DELETE_INSTITUTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_INSTITUTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INSTITUTE):
    case FAILURE(ACTION_TYPES.CREATE_INSTITUTE):
    case FAILURE(ACTION_TYPES.UPDATE_INSTITUTE):
    case FAILURE(ACTION_TYPES.DELETE_INSTITUTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTITUTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_INSTITUTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_INSTITUTE):
    case SUCCESS(ACTION_TYPES.UPDATE_INSTITUTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_INSTITUTE):
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

const apiUrl = 'api/institutes';

// Actions

export const getEntities: ICrudGetAllAction<IInstitute> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INSTITUTE_LIST,
    payload: axios.get<IInstitute>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IInstitute> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INSTITUTE,
    payload: axios.get<IInstitute>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IInstitute> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INSTITUTE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInstitute> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INSTITUTE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInstitute> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INSTITUTE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
