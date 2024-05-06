import { defineComponent, h } from 'vue';

import { GChart } from 'vue-google-charts';

export const type = 'BarChart';

export const data = [
  ['전사', '매출 목표', '매출 금액'],
  ['한화 생명', 2300000, 2000000],
];

export const options = {
  title: '전사 매출 목표',
  chartArea: { width: '50%' },
  hAxis: {
    title: '단위 (원)',
    minValue: 0,
  },
  // vAxis: {
  //   title: '전사',
  // },
  width: 500,
  height: 364,
};

export default defineComponent({
  name: 'GoogleChart',
  components: {
    GChart,
  },
  setup() {
    return () =>
      h(GChart, {
        data,
        options,
        type,
      });
  },
});
