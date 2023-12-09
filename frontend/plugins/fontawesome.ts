import { library, config } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faArrowUp19,
  faArrowsRotate,
  faBars,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faFilePen,
  faFlag,
  faFloppyDisk,
  faHouse,
  faHouseMedicalFlag,
  faListOl,
  faNetworkWired,
  faPlus,
  faUser,
  faUsers,
  faXmark
} from '@fortawesome/free-solid-svg-icons';

import {
  faFlag as faFlagWhite,
} from '@fortawesome/free-regular-svg-icons';


library.add(
  faArrowsRotate,
  faArrowUp19,
  faBars,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faFilePen,
  faFlag,
  faFlagWhite,
  faFloppyDisk,
  faHouse,
  faHouseMedicalFlag,
  faListOl,
  faNetworkWired,
  faPlus,
  faUser,
  faUsers,
  faXmark
);

// This is important, we are going to let Nuxt worry about the CSS
config.autoAddCss = false;

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('font-awesome-icon', FontAwesomeIcon);
});
