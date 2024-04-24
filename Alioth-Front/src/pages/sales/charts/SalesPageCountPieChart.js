import { defineComponent, h } from 'vue';

import { GChart } from 'vue-google-charts';
import axios from 'axios';

const type = 'PieChart';

// export const data = [
//   ['Task', 'Hours per Day'],
//   ['Work', 11],
//   ['Eat', 2],
//   ['Commute', 2],
//   ['Watch TV', 2],
//   ['Sleep', 7],
// ];


let data = [ 
  ['보험 분류', '보험별 건수']
];

function getProductCountData() {
  axios.get("http://localhost:8081/api/batch/contract-rank/count")
        .then(response => {
          console.log("getProductCountData 응답결과 : ");
          console.log(response.data.result);
          const valuesOnly = response.data.result.map(obj => {
            obj.count = parseInt(obj.count);
            return Object.values(obj)
          });
          data.push(...valuesOnly);
          //console.log(data);
          console.log(data);
        })
        .catch(error => {
          console.log("요청할 수 없습니다.1s : ", error);
        });
}




const options = {
  title: '보험 계약 건수 비율',
  pieHole: 0.4,

  width: 700,
  height: 600,
};

export default defineComponent({
  name: 'SalesPagePieChart',
  components: {
    GChart,
  },
  setup() {
    getProductCountData();

    return () =>
      h(GChart, {
        data,
        options,
        type,
      });
  },
});
