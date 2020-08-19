import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/student">
      Student
    </MenuItem>
    <MenuItem icon="asterisk" to="/contact">
      Contact
    </MenuItem>
    <MenuItem icon="asterisk" to="/address">
      Address
    </MenuItem>
    <MenuItem icon="asterisk" to="/institute">
      Institute
    </MenuItem>
    <MenuItem icon="asterisk" to="/group">
      Group
    </MenuItem>
    <MenuItem icon="asterisk" to="/assistance">
      Assistance
    </MenuItem>
    <MenuItem icon="asterisk" to="/class-meeting">
      Class Meeting
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment">
      Comment
    </MenuItem>
    <MenuItem icon="asterisk" to="/document">
      Document
    </MenuItem>
    <MenuItem icon="asterisk" to="/extended-user">
      Extended User
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
