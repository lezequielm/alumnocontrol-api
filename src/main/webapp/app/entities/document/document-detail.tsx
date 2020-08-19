import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './document.reducer';
import { IDocument } from 'app/shared/model/document.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDocumentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DocumentDetail = (props: IDocumentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { documentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Document [<b>{documentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{documentEntity.name}</dd>
          <dt>
            <span id="requestDate">Request Date</span>
          </dt>
          <dd>
            {documentEntity.requestDate ? <TextFormat value={documentEntity.requestDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="uploadDate">Upload Date</span>
          </dt>
          <dd>
            {documentEntity.uploadDate ? <TextFormat value={documentEntity.uploadDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="fileUrl">File Url</span>
          </dt>
          <dd>{documentEntity.fileUrl}</dd>
          <dt>
            <span id="sent">Sent</span>
          </dt>
          <dd>{documentEntity.sent ? 'true' : 'false'}</dd>
          <dt>Group</dt>
          <dd>{documentEntity.group ? documentEntity.group.name : ''}</dd>
          <dt>Student</dt>
          <dd>{documentEntity.student ? documentEntity.student.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ document }: IRootState) => ({
  documentEntity: document.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DocumentDetail);
