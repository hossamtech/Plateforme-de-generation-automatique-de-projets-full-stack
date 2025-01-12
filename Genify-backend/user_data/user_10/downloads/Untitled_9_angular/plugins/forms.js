const plugin = require('tailwindcss/plugin');

module.exports = plugin(function ({ addComponents, theme }) {
    addComponents({
        '[type="checkbox"]:checked': {
            '@apply bg-no-repeat': {},
            backgroundImage: 'url("/src/assets/images/check-arrow.svg")',
            backgroundSize: '.9rem .9rem',
            backgroundPosition: '50%',
        },
        '[type="radio"]:checked': {
            '@apply bg-no-repeat': {},
            backgroundImage: 'url("/src/assets/images/radio-arrow.svg")',
            backgroundSize: '.75rem .75rem',
            backgroundPosition: '58%',
        },
        '[type="checkbox"].arrow-none:checked, [type="radio"].arrow-none:checked': {
            '@apply bg-no-repeat': {},
        },

        '.form-input': {
            '@apply border rounded-md block text-base py-2 px-4 w-full': {},
        },
        '.form-select': {
            '@apply border rounded-md block py-2 pl-3 pr-10 px-4 text-base w-full bg-no-repeat appearance-none': {},
            backgroundImage: `url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%231f242e' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='m2 5 6 6 6-6'/%3e%3c/svg%3e")`,
            backgroundPosition: 'right 0.70rem center',
            backgroundRepeat: 'no-repeat',
            backgroundSize: '0.70em 0.70em',
        },

        'input[type="range"]': {
            '&::-webkit-slider-thumb': {
                '@apply bg-custom-500 h-4 w-4 rounded-full cursor-pointer appearance-none': {},
            },
            '&:focus::-webkit-slider-thumb': {
                boxShadow: `0 0 3px ${theme("colors.custom.700")}`
            }
        },
        // Add other styles here...
    });
});
