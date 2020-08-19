import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExtendedUser from './extended-user';
import ExtendedUserDetail from './extended-user-detail';
import ExtendedUserUpdate from './extended-user-update';
import ExtendedUserDeleteDialog from './extended-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExtendedUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExtendedUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExtendedUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExtendedUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExtendedUserDeleteDialog} />
  </>
);

export default Routes;
