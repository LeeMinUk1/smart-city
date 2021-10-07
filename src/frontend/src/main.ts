import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import mitt from "mitt";
import PrimeVue from 'primevue/config';
import {createPinia} from "pinia";

import 'primevue/resources/themes/saga-blue/theme.css';
// import './assets/style/theme-mira.css';
// import './assets/style/theme-viva-light.css';
// import './assets/style/font-face.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import 'line-awesome/dist/line-awesome/css/line-awesome.min.css';

import Badge from 'primevue/badge';
import BadgeDirective from 'primevue/badgedirective';
import Breadcrumb from 'primevue/breadcrumb';
import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import Checkbox from 'primevue/checkbox';
import Column from 'primevue/column';
import ConfirmationService from 'primevue/confirmationservice';
import ConfirmDialog from 'primevue/confirmdialog';
import DataTable from 'primevue/datatable';
import Dialog from 'primevue/dialog';
import Dropdown from 'primevue/dropdown';
import Editor from 'primevue/editor';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import ProgressBar from 'primevue/progressbar';
import RadioButton from 'primevue/radiobutton';
import TabPanel from 'primevue/tabpanel';
import TabView from 'primevue/tabview';
import TieredMenu from 'primevue/tieredmenu';

const emitter= mitt();

const app = createApp(App);
app.provide('emitter', emitter);
app.use(createPinia());
app.use(router);
app.use(PrimeVue, { ripple: true });
app.use(ConfirmationService);
app.component('Badge', Badge);
app.directive('badge', BadgeDirective);
app.component('Breadcrumb', Breadcrumb);
app.component('Button', Button);
app.component('Calendar', Calendar);
app.component('Checkbox', Checkbox);
app.component('Column', Column);
app.component('ConfirmDialog', ConfirmDialog);
app.component('DataTable', DataTable);
app.component('Dialog', Dialog);
app.component('Dropdown', Dropdown);
app.component('Editor', Editor);
app.component('InputText', InputText);
app.component('Password', Password);
app.component('ProgressBar', ProgressBar);
app.component('RadioButton', RadioButton);
app.component('TabPanel', TabPanel);
app.component('TabView', TabView);
app.component('TieredMenu', TieredMenu);
app.mount('#app')
