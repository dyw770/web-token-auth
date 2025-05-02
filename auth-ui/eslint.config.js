import antfu from '@antfu/eslint-config'

export default antfu(
  {
    unocss: true,
    ignores: [
      'public',
      'dist*',
      'src/ui/shadcn/ui',
    ],
  },
  {
    rules: {
      'eslint-comments/no-unlimited-disable': 'off',
      'curly': ['error', 'all'],
      'unused-imports/no-unused-vars': 'off',
      'ts/no-unused-expressions': ['error', {
        allowShortCircuit: true,
        allowTernary: true,
      }],
    },
  },
  {
    files: [
      'src/**/*.vue',
    ],
    rules: {
      'vue/block-order': ['error', {
        order: ['route', 'script', 'template', 'style'],
      }],
    },
  },
)
