import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './student.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStudentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StudentDetail = (props: IStudentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { studentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Student [<b>{studentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{studentEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <span id="idNumber">Id Number</span>
          </dt>
          <dd>{studentEntity.idNumber}</dd>
          <dt>
            <span id="birthDate">Birth Date</span>
          </dt>
          <dd>{studentEntity.birthDate ? <TextFormat value={studentEntity.birthDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="enabled">Enabled</span>
          </dt>
          <dd>{studentEntity.enabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="photoUrl">Photo Url</span>
          </dt>
          <dd>{studentEntity.photoUrl}</dd>
          <dt>Institute</dt>
          <dd>{studentEntity.institute ? studentEntity.institute.name : ''}</dd>
          <dt>Group</dt>
          <dd>{studentEntity.group ? studentEntity.group.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ student }: IRootState) => ({
  studentEntity: student.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StudentDetail);
