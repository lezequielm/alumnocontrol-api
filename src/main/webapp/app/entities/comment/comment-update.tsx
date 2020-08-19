import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IClassMeeting } from 'app/shared/model/class-meeting.model';
import { getEntities as getClassMeetings } from 'app/entities/class-meeting/class-meeting.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './comment.reducer';
import { IComment } from 'app/shared/model/comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICommentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CommentUpdate = (props: ICommentUpdateProps) => {
  const [classMeetingId, setClassMeetingId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { commentEntity, classMeetings, loading, updating } = props;

  const { text } = commentEntity;

  const handleClose = () => {
    props.history.push('/comment' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getClassMeetings();
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
        ...commentEntity,
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
          <h2 id="alumnocontrolApp.comment.home.createOrEditLabel">Create or edit a Comment</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : commentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="comment-id">ID</Label>
                  <AvInput id="comment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="comment-title">
                  Title
                </Label>
                <AvField id="comment-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="textLabel" for="comment-text">
                  Text
                </Label>
                <AvInput id="comment-text" type="textarea" name="text" />
              </AvGroup>
              <AvGroup>
                <Label for="comment-classMeeting">Class Meeting</Label>
                <AvInput id="comment-classMeeting" type="select" className="form-control" name="classMeeting.id">
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
              <Button tag={Link} id="cancel-save" to="/comment" replace color="info">
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
  classMeetings: storeState.classMeeting.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating,
  updateSuccess: storeState.comment.updateSuccess,
});

const mapDispatchToProps = {
  getClassMeetings,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentUpdate);
