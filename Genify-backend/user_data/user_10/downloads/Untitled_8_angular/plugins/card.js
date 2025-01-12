const plugin = require('tailwindcss/plugin');

module.exports = plugin(function ({ addComponents }) {
    addComponents({
        '.card': {
            '@apply shadow-md rounded-md shadow-slate-200 border-0 mb-5 border-transparent bg-white': {},

        '.card-body': {
            '@apply p-5 pb-0': {},
        }
    });
});
