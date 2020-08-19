import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ContactDetail = (props: IContactDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { contactEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Contact [<b>{contactEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="contactType">Contact Type</span>
          </dt>
          <dd>{contactEntity.contactType}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>{contactEntity.data}</dd>
          <dt>
            <span id="order">Order</span>
          </dt>
          <dd>{contactEntity.order}</dd>
          <dt>Student</dt>
          <dd>{contactEntity.student ? contactEntity.student.id : ''}</dd>
          <dt>User</dt>
          <dd>{contactEntity.user ? contactEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/contact" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contact/${contactEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ contact }: IRootState) => ({
  contactEntity: contact.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ContactDetail);
