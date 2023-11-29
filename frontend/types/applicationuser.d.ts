const applicationUserRoles = ['ADMIN', 'SUPER_ADMIN'] as const;
const applicationUserPermissions = ['TOURNAMENT:CREATE', 'USER:CHANGE', 'ROLE:CHANGE'] as const;

export type ApplicationUserRole = (typeof applicationUserRoles)[number];
export type ApplicationUserPermission = (typeof applicationUserPermissions)[number];
export type ApplicationUser = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  lastLogin: null;
  createdDateTime: string;
  updatedDateTime: string;
  roles: ApplicationUserRole[];
  permissions: ApplicationUserPermission[];
};

export type ApplicationUserBasic = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
};

export type ApplicationUserContact = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
};
