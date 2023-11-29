import type { AuthStatus } from '~/types/auth';

export const useAuthStatus = () =>
  useState<AuthStatus>('AuthStatus', () => {
    return {
      applicationUserId: -1,
      username: '',
      loggedIn: false,
      roles: [],
      permissions: []
    };
  });
