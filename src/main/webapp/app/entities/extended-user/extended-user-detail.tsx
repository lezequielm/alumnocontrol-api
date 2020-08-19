import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './extended-user.reducer';
import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExtendedUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExtendedUserDetail = (props: IExtendedUserDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { extendedUserEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ExtendedUser [<b>{extendedUserEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="photoUrl">Photo Url</span>
          </dt>
          <dd>{extendedUserEntity.photoUrl}</dd>
          <dt>User</dt>
          <dd>{extendedUserEntity.user ? extendedUserEntity.user.id : ''}</dd>
          <dt>Institute</dt>
          <dd>{extendedUserEntity.institute ? extendedUserEntity.institute.name : ''}</dd>
          <dt>Group</dt>
          <dd>{extendedUserEntity.group ? extendedUserEntity.group.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/extended-user" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/extended-user/${extendedUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ extendedUser }: IRootState) => ({
  extendedUserEntity: extendedUser.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExtendedUserDetail);
