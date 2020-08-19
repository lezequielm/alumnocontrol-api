import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Student from './student';
import Contact from './contact';
import Address from './address';
import Institute from './institute';
import Group from './group';
import Assistance from './assistance';
import ClassMeeting from './class-meeting';
import Comment from './comment';
import Document from './document';
import ExtendedUser from './extended-user';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}student`} component={Student} />
      <ErrorBoundaryRoute path={`${match.url}contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}institute`} component={Institute} />
      <ErrorBoundaryRoute path={`${match.url}group`} component={Group} />
      <ErrorBoundaryRoute path={`${match.url}assistance`} component={Assistance} />
      <ErrorBoundaryRoute path={`${match.url}class-meeting`} component={ClassMeeting} />
      <ErrorBoundaryRoute path={`${match.url}comment`} component={Comment} />
      <ErrorBoundaryRoute path={`${match.url}document`} component={Document} />
      <ErrorBoundaryRoute path={`${match.url}extended-user`} component={ExtendedUser} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
