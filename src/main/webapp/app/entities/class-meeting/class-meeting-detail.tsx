import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './class-meeting.reducer';
import { IClassMeeting } from 'app/shared/model/class-meeting.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IClassMeetingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClassMeetingDetail = (props: IClassMeetingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { classMeetingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ClassMeeting [<b>{classMeetingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{classMeetingEntity.name}</dd>
          <dt>
            <span id="classType">Class Type</span>
          </dt>
          <dd>{classMeetingEntity.classType}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{classMeetingEntity.date ? <TextFormat value={classMeetingEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/class-meeting" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/class-meeting/${classMeetingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ classMeeting }: IRootState) => ({
  classMeetingEntity: classMeeting.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClassMeetingDetail);
