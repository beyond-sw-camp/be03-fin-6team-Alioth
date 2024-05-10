import { defineComponent, h, ref, watch } from 'vue';
import axios from 'axios';
import { useSalesStore } from '@/stores/SalesStore';
import { useLoginInfoStore } from '@/stores/loginInfo';
import { GChart } from 'vue-google-charts';

export const type = 'BarChart';

export default defineComponent({
  name: 'GoogleChart',
  components: {
    GChart,
  },
  setup() {
    const code = useLoginInfoStore().memberCode;
    const date = useSalesStore().startDate;
    const chartData = ref([
      ['', '매출 목표', '매출 금액'],
      ['', 0, 0],
    ]);

    async function fetchMemberTarget() {
      try {
        const teamCode = useLoginInfoStore().memberTeamCode;
        const teamDate = useSalesStore().startTeamDate;
        const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL || 'http://localhost:8081/statistics';
        const url = `${baseUrl}/api/sales/${teamCode}/${teamDate}/target`;
        // const url = `http://localhost:8081/statistics/api/sales/${teamCode}/${teamDate}/target`;
        console.log(url);
        const response = await axios.get(url);
        const result = response.data.result || {};
        // 데이터를 업데이트합니다.
        console.log(result);
        chartData.value.splice(1);
        chartData.value.push(['', result.targetPrice || 0, result.price || 0]);
      } catch (error) {
        console.log("요청할 수 없습니다. : ", error);
      }
    }

    // startDate 값이 변경될 때마다 fetchMemberTarget 함수 호출하여 차트 다시 그리기
    watch(() => useSalesStore().startTeamDate, () => {
      fetchMemberTarget();
    });

    // 컴포넌트가 처음 렌더링될 때 데이터를 가져와 차트를 그립니다.
    fetchMemberTarget();

    return () =>
      h(GChart, {
        data: chartData.value,
        options: {
          title: '매출 목표',
          chartArea: { width: '50%' },
          hAxis: { title: '단위 (원)', minValue: 0 },
          width: 500,
          height: 364,
        },
        type,
      });
  },
});
