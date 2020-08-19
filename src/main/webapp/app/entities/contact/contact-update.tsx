import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { getEntities as getExtendedUsers } from 'app/entities/extended-user/extended-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactUpdate = (props: IContactUpdateProps) => {
  const [studentId, setStudentId] = useState('0');
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { contactEntity, students, extendedUsers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/contact' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getStudents();
    props.getExtendedUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...contactEntity,
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
          <h2 id="alumnocontrolApp.contact.home.createOrEditLabel">Create or edit a Contact</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : contactEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="contact-id">ID</Label>
                  <AvInput id="contact-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contactTypeLabel" for="contact-contactType">
                  Contact Type
                </Label>
                <AvInput
                  id="contact-contactType"
                  type="select"
                  className="form-control"
                  name="contactType"
                  value={(!isNew && contactEntity.contactType) || 'EMAIL'}
                >
                  <option value="EMAIL">EMAIL</option>
                  <option value="PHONE">PHONE</option>
                  <option value="MOBILE">MOBILE</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="dataLabel" for="contact-data">
                  Data
                </Label>
                <AvField
                  id="contact-data"
                  type="text"
                  name="data"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    minLength: { value: 2, errorMessage: 'This field is required to be at least 2 characters.' },
                    maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="orderLabel" for="contact-order">
                  Order
                </Label>
                <AvField
                  id="contact-order"
                  type="string"
                  className="form-control"
                  name="order"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="contact-student">Student</Label>
                <AvInput id="contact-student" type="select" className="form-control" name="student.id">
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
                <Label for="contact-user">User</Label>
                <AvInput id="contact-user" type="select" className="form-control" name="user.id">
                  <option value="" key="0" />
                  {extendedUsers
                    ? extendedUsers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/contact" replace color="info">
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
  extendedUsers: storeState.extendedUser.entities,
  contactEntity: storeState.contact.entity,
  loading: storeState.contact.loading,
  updating: storeState.contact.updating,
  updateSuccess: storeState.contact.updateSuccess,
});

const mapDispatchToProps = {
  getStudents,
  getExtendedUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactUpdate);
