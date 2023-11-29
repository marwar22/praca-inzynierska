import type { ApplicationUserRole } from "./applicationuser";

export type AuthStatus = {
    applicationUserId:number;
    username: string;
    loggedIn: boolean;
    roles: ApplicationUserRole[];
    permissions: ApplicationUserPermission[];
}
