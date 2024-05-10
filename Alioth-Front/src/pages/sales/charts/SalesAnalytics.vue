<script setup>


const statistics = [
  {
    title: '매출',
    stats: '245k',
    color: 'primary',
  },
  {
    title: '계약건수',
    stats: '12.5k',
    color: 'success',
  },
  {
    title: '해약 매출',
    stats: '1.54k',
    color: 'warning',
  },
  {
    title: '해약 건수',
    stats: '$88k',
    color: 'info',
  },
]


</script>

<template>
  <VCard title="판매 실적">
    <VCardText class="pt-10">
      <VRow>
        <VCol cols="12" sm="6" md="6" class="mb-5">
          <VCard color="primary" class="text-center">
            <div class="d-flex align-center justify-center gap-x-3 py-4">
              <div class="d-flex flex-column">
                <div class="text-body-1">
                  {{ statistics[0].title }}
                </div>
                <h5 class="text-h5">
                  {{ statistics[0].stats }}
                </h5>
              </div>
            </div>
          </VCard>
        </VCol>
        <VCol cols="12" sm="6" md="6" class="mb-5">
          <VCard color="success" class="text-center">
            <div class="d-flex align-center justify-center gap-x-3 py-4">
              <div class="d-flex flex-column">
                <div class="text-body-1">
                  {{ statistics[1].title }}
                </div>
                <h5 class="text-h5">
                  {{ statistics[1].stats }}
                </h5>
              </div>
            </div>
          </VCard>
        </VCol>
        <VCol cols="12" sm="6" md="6" class="mb-8">
          <VCard color="warning" class="text-center">
            <div class="d-flex align-center justify-center gap-x-3 py-4">
              <div class="d-flex flex-column">
                <div class="text-body-1">
                  {{ statistics[2].title }}
                </div>
                <h5 class="text-h5">
                  {{ statistics[2].stats }}
                </h5>
              </div>
            </div>
          </VCard>
        </VCol>
        <VCol cols="12" sm="6" md="6">
          <VCard color="info" class="text-center">
            <div class="d-flex align-center justify-center gap-x-3 py-4">
              <div class="d-flex flex-column">
                <div class="text-body-1">
                  {{ statistics[3].title }}
                </div>
                <h5 class="text-h5">
                  {{ statistics[3].stats }}
                </h5>
              </div>
            </div>
          </VCard>
        </VCol>
      </VRow>
    </VCardText>
  </VCard>
</template>
