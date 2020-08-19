import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { getEntities as getExtendedUsers } from 'app/entities/extended-user/extended-user.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { getEntity, updateEntity, createEntity, reset } from './address.reducer';
import { IAddress } from 'app/shared/model/address.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAddressUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AddressUpdate = (props: IAddressUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [studentId, setStudentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { addressEntity, extendedUsers, students, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/address' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getExtendedUsers();
    props.getStudents();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...addressEntity,
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
          <h2 id="alumnocontrolApp.address.home.createOrEditLabel">Create or edit a Address</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : addressEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="address-id">ID</Label>
                  <AvInput id="address-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="streetLabel" for="address-street">
                  Street
                </Label>
                <AvField
                  id="address-street"
                  type="text"
                  name="street"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="numberLabel" for="address-number">
                  Number
                </Label>
                <AvField
                  id="address-number"
                  type="text"
                  name="number"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="flatLabel" for="address-flat">
                  Flat
                </Label>
                <AvField id="address-flat" type="string" className="form-control" name="flat" />
              </AvGroup>
              <AvGroup>
                <Label id="departmentLabel" for="address-department">
                  Department
                </Label>
                <AvField id="address-department" type="text" name="department" />
              </AvGroup>
              <AvGroup>
                <Label id="postalCodeLabel" for="address-postalCode">
                  Postal Code
                </Label>
                <AvField id="address-postalCode" type="text" name="postalCode" />
              </AvGroup>
              <AvGroup>
                <Label id="orderLabel" for="address-order">
                  Order
                </Label>
                <AvField
                  id="address-order"
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
                <Label for="address-user">User</Label>
                <AvInput id="address-user" type="select" className="form-control" name="user.id">
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
              <AvGroup>
                <Label for="address-student">Student</Label>
                <AvInput id="address-student" type="select" className="form-control" name="student.id">
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
              <Button tag={Link} id="cancel-save" to="/address" replace color="info">
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
  extendedUsers: storeState.extendedUser.entities,
  students: storeState.student.entities,
  addressEntity: storeState.address.entity,
  loading: storeState.address.loading,
  updating: storeState.address.updating,
  updateSuccess: storeState.address.updateSuccess,
});

const mapDispatchToProps = {
  getExtendedUsers,
  getStudents,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AddressUpdate);
