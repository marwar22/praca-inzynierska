import { AxiosError } from 'axios';
import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser } from '~/types/applicationuser';

export async function errorToApiError(error: any): Promise<ApiError> {
  if (error instanceof AxiosError) {
    if (error.response) {
      return (await error.response.data) as ApiError;
    } else {
      return {
        status: error.status?.toString() ?? '',
        errors: {},
        message: 'Unknown error'
      };
    }
  }
  return {
    status: error.status?.toString() ?? '',
    errors: {},
    message: 'Unknown error'
  };
}

export function fetchErrorToApiError(error: any) {
  if (error?.data) {
    const { status, errors, message } = error.data;
    return { status, errors, message };
  } else {
    return { status: error?.status?.toString() ?? '', errors: {}, message: 'Unknown fetch error' };
  }
}

export function nameFromApplicationUser(applicationUser: ApplicationUser | null | undefined) {
  if (!applicationUser) return '';
  return `${applicationUser.firstName} ${applicationUser.lastName}`;
}
