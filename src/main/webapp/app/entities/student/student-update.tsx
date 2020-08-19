import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IInstitute } from 'app/shared/model/institute.model';
import { getEntities as getInstitutes } from 'app/entities/institute/institute.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './student.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IStudentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StudentUpdate = (props: IStudentUpdateProps) => {
  const [instituteId, setInstituteId] = useState('0');
  const [groupId, setGroupId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { studentEntity, institutes, groups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/student' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getInstitutes();
    props.getGroups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.birthDate = convertDateTimeToServer(values.birthDate);

    if (errors.length === 0) {
      const entity = {
        ...studentEntity,
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
          <h2 id="alumnocontrolApp.student.home.createOrEditLabel">Create or edit a Student</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : studentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="student-id">ID</Label>
                  <AvInput id="student-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="student-firstName">
                  First Name
                </Label>
                <AvField
                  id="student-firstName"
                  type="text"
                  name="firstName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    minLength: { value: 2, errorMessage: 'This field is required to be at least 2 characters.' },
                    maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="student-lastName">
                  Last Name
                </Label>
                <AvField
                  id="student-lastName"
                  type="text"
                  name="lastName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    minLength: { value: 2, errorMessage: 'This field is required to be at least 2 characters.' },
                    maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="idNumberLabel" for="student-idNumber">
                  Id Number
                </Label>
                <AvField
                  id="student-idNumber"
                  type="text"
                  name="idNumber"
                  validate={{
                    minLength: { value: 6, errorMessage: 'This field is required to be at least 6 characters.' },
                    maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="birthDateLabel" for="student-birthDate">
                  Birth Date
                </Label>
                <AvInput
                  id="student-birthDate"
                  type="datetime-local"
                  className="form-control"
                  name="birthDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.studentEntity.birthDate)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="student-enabled" type="checkbox" className="form-check-input" name="enabled" />
                  Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="photoUrlLabel" for="student-photoUrl">
                  Photo Url
                </Label>
                <AvField id="student-photoUrl" type="text" name="photoUrl" />
              </AvGroup>
              <AvGroup>
                <Label for="student-institute">Institute</Label>
                <AvInput id="student-institute" type="select" className="form-control" name="institute.id">
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
                <Label for="student-group">Group</Label>
                <AvInput id="student-group" type="select" className="form-control" name="group.id">
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
              <Button tag={Link} id="cancel-save" to="/student" replace color="info">
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
  institutes: storeState.institute.entities,
  groups: storeState.group.entities,
  studentEntity: storeState.student.entity,
  loading: storeState.student.loading,
  updating: storeState.student.updating,
  updateSuccess: storeState.student.updateSuccess,
});

const mapDispatchToProps = {
  getInstitutes,
  getGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StudentUpdate);
