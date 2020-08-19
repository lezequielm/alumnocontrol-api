import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGroup, defaultValue } from 'app/shared/model/group.model';

export const ACTION_TYPES = {
  FETCH_GROUP_LIST: 'group/FETCH_GROUP_LIST',
  FETCH_GROUP: 'group/FETCH_GROUP',
  CREATE_GROUP: 'group/CREATE_GROUP',
  UPDATE_GROUP: 'group/UPDATE_GROUP',
  DELETE_GROUP: 'group/DELETE_GROUP',
  RESET: 'group/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGroup>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type GroupState = Readonly<typeof initialState>;

// Reducer

export default (state: GroupState = initialState, action): GroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GROUP):
    case REQUEST(ACTION_TYPES.UPDATE_GROUP):
    case REQUEST(ACTION_TYPES.DELETE_GROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GROUP):
    case FAILURE(ACTION_TYPES.CREATE_GROUP):
    case FAILURE(ACTION_TYPES.UPDATE_GROUP):
    case FAILURE(ACTION_TYPES.DELETE_GROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_GROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_GROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GROUP):
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

const apiUrl = 'api/groups';

// Actions

export const getEntities: ICrudGetAllAction<IGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GROUP_LIST,
    payload: axios.get<IGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GROUP,
    payload: axios.get<IGroup>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GROUP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GROUP,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GROUP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
