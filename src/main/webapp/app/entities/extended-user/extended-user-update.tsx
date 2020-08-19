import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IInstitute } from 'app/shared/model/institute.model';
import { getEntities as getInstitutes } from 'app/entities/institute/institute.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './extended-user.reducer';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExtendedUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExtendedUserUpdate = (props: IExtendedUserUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [instituteId, setInstituteId] = useState('0');
  const [groupId, setGroupId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { extendedUserEntity, users, institutes, groups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/extended-user' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getInstitutes();
    props.getGroups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...extendedUserEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="alumnocontrolApp.extendedUser.home.createOrEditLabel">Create or edit a ExtendedUser</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : extendedUserEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="extended-user-id">ID</Label>
                  <AvInput id="extended-user-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="photoUrlLabel" for="extended-user-photoUrl">
                  Photo Url
                </Label>
                <AvField id="extended-user-photoUrl" type="text" name="photoUrl" />
              </AvGroup>
              <AvGroup>
                <Label for="extended-user-user">User</Label>
                <AvInput id="extended-user-user" type="select" className="form-control" name="user.id">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="extended-user-institute">Institute</Label>
                <AvInput id="extended-user-institute" type="select" className="form-control" name="institute.id">
                  <option value="" key="0" />
                  {institutes
                    ? institutes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="extended-user-group">Group</Label>
                <AvInput id="extended-user-group" type="select" className="form-control" name="group.id">
                  <option value="" key="0" />
                  {groups
                    ? groups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/extended-user" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  institutes: storeState.institute.entities,
  groups: storeState.group.entities,
  extendedUserEntity: storeState.extendedUser.entity,
  loading: storeState.extendedUser.loading,
  updating: storeState.extendedUser.updating,
  updateSuccess: storeState.extendedUser.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getInstitutes,
  getGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExtendedUserUpdate);
