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
