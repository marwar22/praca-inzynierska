import { AxiosError } from 'axios';
import type { ApiError } from '~/types/apierrror';

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
