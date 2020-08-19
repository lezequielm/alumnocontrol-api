import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './class-meeting.reducer';
import { IClassMeeting } from 'app/shared/model/class-meeting.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IClassMeetingUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClassMeetingUpdate = (props: IClassMeetingUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { classMeetingEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/class-meeting' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const entity = {
        ...classMeetingEntity,
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
          <h2 id="alumnocontrolApp.classMeeting.home.createOrEditLabel">Create or edit a ClassMeeting</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : classMeetingEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="class-meeting-id">ID</Label>
                  <AvInput id="class-meeting-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="class-meeting-name">
                  Name
                </Label>
                <AvField
                  id="class-meeting-name"
                  type="text"
                  name="name"
                  validate={{
                    minLength: { value: 2, errorMessage: 'This field is required to be at least 2 characters.' },
                    maxLength: { value: 60, errorMessage: 'This field cannot be longer than 60 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="classTypeLabel" for="class-meeting-classType">
                  Class Type
                </Label>
                <AvInput
                  id="class-meeting-classType"
                  type="select"
                  className="form-control"
                  name="classType"
                  value={(!isNew && classMeetingEntity.classType) || 'NORMAL'}
                >
                  <option value="NORMAL">NORMAL</option>
                  <option value="SPECIAL">SPECIAL</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="dateLabel" for="class-meeting-date">
                  Date
                </Label>
                <AvInput
                  id="class-meeting-date"
                  type="datetime-local"
                  className="form-control"
                  name="date"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.classMeetingEntity.date)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/class-meeting" replace color="info">
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
  classMeetingEntity: storeState.classMeeting.entity,
  loading: storeState.classMeeting.loading,
  updating: storeState.classMeeting.updating,
  updateSuccess: storeState.classMeeting.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClassMeetingUpdate);
