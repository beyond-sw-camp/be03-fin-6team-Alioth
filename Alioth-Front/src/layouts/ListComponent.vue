<template>
  <v-data-table
    :headers="columns"
    item-key="id"
    :items="rows"
    :items-length="rows.length"
    item-value="id"
  ></v-data-table>
</template>

<script>
export default {
  props: {
    columns: {
      type: Array,
      required: true
    },
    rows: {
      type: Array,
      required: true
    },
  },
};
</script>
