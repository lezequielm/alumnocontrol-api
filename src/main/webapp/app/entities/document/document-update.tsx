import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { getEntity, updateEntity, createEntity, reset } from './document.reducer';
import { IDocument } from 'app/shared/model/document.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDocumentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DocumentUpdate = (props: IDocumentUpdateProps) => {
  const [groupId, setGroupId] = useState('0');
  const [studentId, setStudentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { documentEntity, groups, students, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/document' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getGroups();
    props.getStudents();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.requestDate = convertDateTimeToServer(values.requestDate);
    values.uploadDate = convertDateTimeToServer(values.uploadDate);

    if (errors.length === 0) {
      const entity = {
        ...documentEntity,
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
          <h2 id="alumnocontrolApp.document.home.createOrEditLabel">Create or edit a Document</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : documentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="document-id">ID</Label>
                  <AvInput id="document-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="document-name">
                  Name
                </Label>
                <AvField
                  id="document-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="requestDateLabel" for="document-requestDate">
                  Request Date
                </Label>
                <AvInput
                  id="document-requestDate"
                  type="datetime-local"
                  className="form-control"
                  name="requestDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.documentEntity.requestDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="uploadDateLabel" for="document-uploadDate">
                  Upload Date
                </Label>
                <AvInput
                  id="document-uploadDate"
                  type="datetime-local"
                  className="form-control"
                  name="uploadDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.documentEntity.uploadDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fileUrlLabel" for="document-fileUrl">
                  File Url
                </Label>
                <AvField
                  id="document-fileUrl"
                  type="text"
                  name="fileUrl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="sentLabel">
                  <AvInput id="document-sent" type="checkbox" className="form-check-input" name="sent" />
                  Sent
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="document-group">Group</Label>
                <AvInput id="document-group" type="select" className="form-control" name="group.id">
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
              <AvGroup>
                <Label for="document-student">Student</Label>
                <AvInput id="document-student" type="select" className="form-control" name="student.id">
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
              <Button tag={Link} id="cancel-save" to="/document" replace color="info">
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
  groups: storeState.group.entities,
  students: storeState.student.entities,
  documentEntity: storeState.document.entity,
  loading: storeState.document.loading,
  updating: storeState.document.updating,
  updateSuccess: storeState.document.updateSuccess,
});

const mapDispatchToProps = {
  getGroups,
  getStudents,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DocumentUpdate);
