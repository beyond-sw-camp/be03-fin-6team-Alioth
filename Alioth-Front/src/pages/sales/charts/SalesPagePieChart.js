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


// export const data = [
//   ['가나다', "kjkads"],
//   ['Work', 11],
//   ['Eat', 3],
//   ['Commute', 3],
//   ['Watch TV', 3],
//   ['Sleep', 7],
//   ['ffff', 33],
//   ['qqqq', 7],
// // ];


let data = [ 
  ['보험 분류', '보험별 가격']
];

function getProductPriceData() {
  axios.get("http://localhost:8081/api/batch/contract-rank/price")
        .then(response => {
          console.log("getProductPriceData 응답결과 : ");
          console.log(response.data.result);
          const valuesOnly = response.data.result.map(obj => {
            obj.price = parseInt(obj.price);
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
  title: '보험 계약 금액별 비율',
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
    getProductPriceData();

    return () =>
      h(GChart, {
        data,
        options,
        type,
      });
  },
});
