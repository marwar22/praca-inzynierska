export default defineNuxtRouteMiddleware(async (to, from) => {
  const auth = useAuthStatus();
  if (!auth.value.loggedIn) return navigateTo('/logowanie');
});
