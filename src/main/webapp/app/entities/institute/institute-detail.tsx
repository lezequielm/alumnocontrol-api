import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './institute.reducer';
import { IInstitute } from 'app/shared/model/institute.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInstituteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const InstituteDetail = (props: IInstituteDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { instituteEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Institute [<b>{instituteEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{instituteEntity.name}</dd>
          <dt>
            <span id="enabled">Enabled</span>
          </dt>
          <dd>{instituteEntity.enabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/institute" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/institute/${instituteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ institute }: IRootState) => ({
  instituteEntity: institute.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(InstituteDetail);
