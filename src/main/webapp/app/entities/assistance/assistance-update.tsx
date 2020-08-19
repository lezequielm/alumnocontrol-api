import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { IInstitute } from 'app/shared/model/institute.model';
import { getEntities as getInstitutes } from 'app/entities/institute/institute.reducer';
import { IClassMeeting } from 'app/shared/model/class-meeting.model';
import { getEntities as getClassMeetings } from 'app/entities/class-meeting/class-meeting.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './assistance.reducer';
import { IAssistance } from 'app/shared/model/assistance.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAssistanceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AssistanceUpdate = (props: IAssistanceUpdateProps) => {
  const [studentId, setStudentId] = useState('0');
  const [instituteId, setInstituteId] = useState('0');
  const [classMeetingId, setClassMeetingId] = useState('0');
  const [groupId, setGroupId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { assistanceEntity, students, institutes, classMeetings, groups, loading, updating } = props;

  const { justification } = assistanceEntity;

  const handleClose = () => {
    props.history.push('/assistance' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getStudents();
    props.getInstitutes();
    props.getClassMeetings();
    props.getGroups();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...assistanceEntity,
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
          <h2 id="alumnocontrolApp.assistance.home.createOrEditLabel">Create or edit a Assistance</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : assistanceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="assistance-id">ID</Label>
                  <AvInput id="assistance-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup check>
                <Label id="presentLabel">
                  <AvInput id="assistance-present" type="checkbox" className="form-check-input" name="present" />
                  Present
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="delayedLabel">
                  <AvInput id="assistance-delayed" type="checkbox" className="form-check-input" name="delayed" />
                  Delayed
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="justifiedLabel">
                  <AvInput id="assistance-justified" type="checkbox" className="form-check-input" name="justified" />
                  Justified
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="justificationLabel" for="assistance-justification">
                  Justification
                </Label>
                <AvInput id="assistance-justification" type="textarea" name="justification" />
              </AvGroup>
              <AvGroup>
                <Label for="assistance-student">Student</Label>
                <AvInput id="assistance-student" type="select" className="form-control" name="student.id">
                  <option value="" key="0" />
                  {students
                    ? students.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="assistance-institute">Institute</Label>
                <AvInput id="assistance-institute" type="select" className="form-control" name="institute.id">
                  <option value="" key="0" />
                  {institutes
                    ? institutes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="assistance-classMeeting">Class Meeting</Label>
                <AvInput id="assistance-classMeeting" type="select" className="form-control" name="classMeeting.id">
                  <option value="" key="0" />
                  {classMeetings
                    ? classMeetings.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="assistance-group">Group</Label>
                <AvInput id="assistance-group" type="select" className="form-control" name="group.id">
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
              <Button tag={Link} id="cancel-save" to="/assistance" replace color="info">
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
  students: storeState.student.entities,
  institutes: storeState.institute.entities,
  classMeetings: storeState.classMeeting.entities,
  groups: storeState.group.entities,
  assistanceEntity: storeState.assistance.entity,
  loading: storeState.assistance.loading,
  updating: storeState.assistance.updating,
  updateSuccess: storeState.assistance.updateSuccess,
});

const mapDispatchToProps = {
  getStudents,
  getInstitutes,
  getClassMeetings,
  getGroups,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AssistanceUpdate);
