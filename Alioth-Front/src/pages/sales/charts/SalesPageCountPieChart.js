import { defineComponent, h, ref, watchEffect } from 'vue';
import { GChart } from 'vue-google-charts';
import { useSalesRankingStore } from '@/stores/SalesRankingStore';
import axios from 'axios';

const type = 'PieChart';

let data = [['보험 분류', '보험별 건수']];

async function getProductCountData() {
  try {
    const baseUrl = import.meta.env.VITE_API_STATISTICS_BASE_URL
    const response = await axios.get(`${baseUrl}/api/batch/contract-rank/count`);
    const valuesOnly = response.data.result.map(obj => {
      obj.count = parseInt(obj.count);
      return Object.values(obj);
    });
    data.splice(1);
    data.push(...valuesOnly);

    //console.log("보험건수 ", data);

  } catch (error) {
    console.log("요청할 수 없습니다. : ", error);
  }
}


async function getProductCount() {
  try {
    let url = `http://localhost:8081/api/batch/contract-rank/count/${useSalesRankingStore().startDate}`;
    console.log(url);
    const response = await axios.get(url);
    const valuesOnly = response.data.result.map(obj => {
      obj.count = parseInt(obj.count);
      return Object.values(obj);
    });
    data.splice(1);
    data.push(...valuesOnly);

    console.log("보험건수 ", data);
    loaded_CountPie = true;
  } catch (error) {
    console.log("요청할 수 없습니다. : ", error);
  }
}

const options = {
  title: '보험 계약 건수 비율',
  pieHole: 0.4,
  width: 800,
  height: 500,
};

export default defineComponent({
  name: 'SalesPageCountChart',
  components: {
    GChart,
  },
  setup() {
    const chartData = ref(data);
    const loaded_CountPie = ref(false);
    const salesRankingStore = useSalesRankingStore(); // store 가져오기

    watch(() => salesRankingStore.startDate, async (newStartDate, oldStartDate) => {
      if (newStartDate !== oldStartDate) {
        await getProductCount();
        loaded_CountPie.value = true;
        console.log("정상동작하는지를 확인하는 디버그", loaded_CountPie.value);
        setTimeout(() => {
          loaded_CountPie.value = false;
        }, 10);
        setTimeout(() => {
          loaded_CountPie.value = true;
        }, 100);
      }
    });

    // 의존성 배열에 startDate 추가하여 변경을 감지하도록 설정
    watch(() => salesRankingStore.startDate, () => {}, { deep: true });

    getProductCountData();

    return () =>
      // loaded가 true일 때만 차트를 렌더링
      loaded_CountPie.value
        ? h(GChart, {
            data: chartData.value,
            options,
            type,
          })
        : null;
  },
});
