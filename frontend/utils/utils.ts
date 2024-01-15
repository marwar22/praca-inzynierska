import type { ApiError } from '~/types/apierrror';
import type { ApplicationUser, ApplicationUserBasic, ApplicationUserContact } from '~/types/applicationuser';

export function isBetween(min: number, n: number, max: number) {
  return min <= n && n <= max;
}

export function clamp(min: number, n: number, max: number) {
  return Math.max(min, n, Math.min(n, max));
}

export function fetchErrorToApiError(error: any): ApiError {
  if (error?.data) {
    const { status, errors, message } = error.data;
    return { status: `${error.status} ${status}` ?? '', errors: errors ?? {}, message: message ?? '' };
  } else {
    return { status: error?.status?.toString() ?? '', errors: {}, message: 'Unknown fetch error' };
  }
}

export function nameFromApplicationUser(
  applicationUser: ApplicationUser | ApplicationUserBasic | ApplicationUserContact | null | undefined
) {
  if (!applicationUser) return '';
  return `${applicationUser.firstName} ${applicationUser.lastName}`;
}

interface Entity {
  id: number;
}
export function entityArrayToMap<T extends Entity>(arr: T[] | null | undefined) {
  const map = new Map<number, T>();
  for (const obj of arr ?? []) {
    map.set(obj.id, obj);
  }
  return map;
}

export function nextPowerOf10(num: number) {
  return Math.pow(10, Math.ceil(Math.log10(num)));
}
