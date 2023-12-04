import type { ApplicationUserPermission, ApplicationUserRole } from '~/types/applicationuser';

const permissionsLabels: { [key in ApplicationUserPermission]: string } = {
  'ROLE:CHANGE': 'Zmiana ról użytkowników',
  'TOURNAMENT:CREATE': 'Tworzenie rozgrywek i edycja utworzonych przez siebie rozgrywek',
  'TOURNAMENT:UPDATE_ANY': 'Edycja dowolnych rozgrywek'
};

export function permissionToLabel(permission: ApplicationUserPermission) {
  return permissionsLabels[permission] ?? permission;
}

const rolesLabels: { [key in ApplicationUserRole]: string } = {
  ADMIN: 'Administrator',
  SUPER_ADMIN: 'Główny administrator'
};

export function roleToLabel(role: ApplicationUserRole) {
  return rolesLabels[role] ?? role;
}
