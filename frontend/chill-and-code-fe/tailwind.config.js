/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 톤앤톤 팔레트 (#C9D7DE 기반)
        brand: {
          50: '#EEF3F5',   // 가장 밝음 (배경)
          100: '#E0E9ED',  // 밝음 (카드)
          200: '#C9D7DE',  // 메인 ⭐
          300: '#A8BCC6',  // 중간톤
          400: '#8AA2AE',  // 어두운 (호버)
          500: '#6B8896',  // 더 어두운 (강조)
          600: '#4D6D7E',  // 가장 어두운 (텍스트)
          700: '#3A5464',  // 매우 어두운
          800: '#283B4A',  // 거의 검정
          900: '#1A2830',  // 검정에 가까움
        },
      },
    },
  },
  plugins: [],
}

