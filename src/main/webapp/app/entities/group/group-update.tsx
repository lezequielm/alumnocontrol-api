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
import { getEntity, updateEntity, createEntity, reset } from './group.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGroupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GroupUpdate = (props: IGroupUpdateProps) => {
  const [instituteId, setInstituteId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { groupEntity, institutes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/group' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getInstitutes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...groupEntity,
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
          <h2 id="alumnocontrolApp.group.home.createOrEditLabel">Create or edit a Group</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : groupEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="group-id">ID</Label>
                  <AvInput id="group-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="group-name">
                  Name
                </Label>
                <AvField
                  id="group-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    minLength: { value: 2, errorMessage: 'This field is required to be at least 2 characters.' },
                    maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="group-enabled" type="checkbox" className="form-check-input" name="enabled" />
                  Enabled
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="group-institute">Institute</Label>
                <AvInput id="group-institute" type="select" className="form-control" name="institute.id">
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
              <Button tag={Link} id="cancel-save" to="/group" replace color="info">
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
  groupEntity: storeState.group.entity,
  loading: storeState.group.loading,
  updating: storeState.group.updating,
  updateSuccess: storeState.group.updateSuccess,
});

const mapDispatchToProps = {
  getInstitutes,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GroupUpdate);
