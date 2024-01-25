import { library, config } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faArrowDown,
  faArrowUp,
  faArrowUp19,
  faArrowsRotate,
  faBars,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faEquals,
  faFilePen,
  faFlag,
  faFloppyDisk,
  faHouse,
  faHouseMedicalFlag,
  faListOl,
  faMinus,
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
  faArrowDown,
  faArrowUp,
  faArrowUp19,
  faArrowsRotate,
  faBars,
  faCalendarDays,
  faCircleInfo,
  faCirclePlus,
  faCircleUser,
  faClock,
  faDice,
  faEquals,
  faFilePen,
  faFlag,
  faFlagWhite,
  faFloppyDisk,  
  faHouse,
  faHouseMedicalFlag,
  faListOl,
  faMinus,
  faNetworkWired,
  faPlus,
  faUser,
  faUsers,
  faXmark
);

config.autoAddCss = false;

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('font-awesome-icon', FontAwesomeIcon);
});
