const applicationUserRoles = ['TOURNAMENT_ORGANIZER', 'ADMIN', 'SUPER_ADMIN'] as const;
const applicationUserPermissions = [
  'TOURNAMENT:CREATE',
  'TOURNAMENT:UPDATE_ANY',
  'ROLE:CHANGE',
  'USER:GET_ANY'
] as const;

export type ApplicationUserRole = (typeof applicationUserRoles)[number];
export type ApplicationUserPermission = (typeof applicationUserPermissions)[number];
export type ApplicationUser = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  rating: number | null;
  prvRating: number | null;
  ranking: number | null;
  prvRanking: number | null;
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
  rating: number | null;
  prvRating: number | null;
  ranking: number | null;
  prvRanking: number | null;
};

export type ApplicationUserContact = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  rating: number | number;
};
