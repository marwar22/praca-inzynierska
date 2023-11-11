export type ApiError = {
  status: string;
  message: string;
  errors: { [key: string]: string };
};
