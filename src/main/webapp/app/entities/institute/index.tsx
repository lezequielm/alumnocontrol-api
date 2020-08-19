import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Institute from './institute';
import InstituteDetail from './institute-detail';
import InstituteUpdate from './institute-update';
import InstituteDeleteDialog from './institute-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InstituteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InstituteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InstituteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Institute} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InstituteDeleteDialog} />
  </>
);

export default Routes;
