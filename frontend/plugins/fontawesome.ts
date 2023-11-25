import { library, config } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faArrowsRotate,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faFilePen,
  faNetworkWired,
  faUser,
  faUsers,
  faXmark
} from '@fortawesome/free-solid-svg-icons';

library.add(
  faArrowsRotate,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faFilePen,
  faNetworkWired,
  faUser,
  faUsers,
  faXmark
);

// This is important, we are going to let Nuxt worry about the CSS
config.autoAddCss = false;

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('font-awesome-icon', FontAwesomeIcon);
});
