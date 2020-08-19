import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ClassMeeting from './class-meeting';
import ClassMeetingDetail from './class-meeting-detail';
import ClassMeetingUpdate from './class-meeting-update';
import ClassMeetingDeleteDialog from './class-meeting-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ClassMeetingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ClassMeetingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ClassMeetingDetail} />
      <ErrorBoundaryRoute path={match.url} component={ClassMeeting} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ClassMeetingDeleteDialog} />
  </>
);

export default Routes;
