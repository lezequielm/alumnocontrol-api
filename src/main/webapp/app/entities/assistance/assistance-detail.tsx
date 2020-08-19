import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './assistance.reducer';
import { IAssistance } from 'app/shared/model/assistance.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAssistanceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AssistanceDetail = (props: IAssistanceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { assistanceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Assistance [<b>{assistanceEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="present">Present</span>
          </dt>
          <dd>{assistanceEntity.present ? 'true' : 'false'}</dd>
          <dt>
            <span id="delayed">Delayed</span>
          </dt>
          <dd>{assistanceEntity.delayed ? 'true' : 'false'}</dd>
          <dt>
            <span id="justified">Justified</span>
          </dt>
          <dd>{assistanceEntity.justified ? 'true' : 'false'}</dd>
          <dt>
            <span id="justification">Justification</span>
          </dt>
          <dd>{assistanceEntity.justification}</dd>
          <dt>Student</dt>
          <dd>{assistanceEntity.student ? assistanceEntity.student.id : ''}</dd>
          <dt>Institute</dt>
          <dd>{assistanceEntity.institute ? assistanceEntity.institute.id : ''}</dd>
          <dt>Class Meeting</dt>
          <dd>{assistanceEntity.classMeeting ? assistanceEntity.classMeeting.id : ''}</dd>
          <dt>Group</dt>
          <dd>{assistanceEntity.group ? assistanceEntity.group.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/assistance" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assistance/${assistanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ assistance }: IRootState) => ({
  assistanceEntity: assistance.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AssistanceDetail);
