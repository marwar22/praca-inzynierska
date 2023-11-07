/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './components/**/*.{js,vue,ts}',
    './layouts/**/*.vue',
    './pages/**/*.vue',
    './plugins/**/*.{js,ts}',
    './app.vue'
  ],
  theme: {
    extend: {
      colors: {
        'atlantis': {
            '50': '#e5f1c6',
            '100': '#dbebad',
            '200': '#c9e08a',
            '300': '#aed058',
            '400': '#a6c856',
            '500': '#95bf3b',
            '600': '#799f2d',
            '700': '#5e7b28',
            '800': '#506827',
            '900': '#465926',
            '950': '#2a3b12',
        },
      },
      fontFamily: {
        montserrat: ['Montserrat', 'sans-serif']
      }
    }
  },
  plugins: []
};
