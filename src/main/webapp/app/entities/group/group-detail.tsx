import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './group.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GroupDetail = (props: IGroupDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { groupEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Group [<b>{groupEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{groupEntity.name}</dd>
          <dt>
            <span id="enabled">Enabled</span>
          </dt>
          <dd>{groupEntity.enabled ? 'true' : 'false'}</dd>
          <dt>Institute</dt>
          <dd>{groupEntity.institute ? groupEntity.institute.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/group" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ group }: IRootState) => ({
  groupEntity: group.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GroupDetail);
