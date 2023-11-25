const applicationUserRoles = ['TOURNAMENT:CREATE', 'USER:CHANGE', 'ROLE:CHANGE'] as const;

export type ApplicationUserRole = (typeof applicationUserRoles)[number];
export type ApplicationUser = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  lastLogin: null;
  createdDateTime: string;
  updatedDateTime: string;
  roles: string[];
  permissions: string[];
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