<script setup>
const statistics = [
  {
    title: '총매출',
    stats: '245k',
    color: 'primary',
  },
  {
    title: '매출목표',
    stats: '12.5k',
    color: 'success',
  },
  {
    title: '계약건수',
    stats: '1.54k',
    color: 'warning',
  },
  {
    title: '신규회원수',
    stats: '$88k',
    color: 'info',
  },
]
</script>

<template>
  <VCard title="판매 실적">
    <template #subtitle>
      <p class="text-body-1 mb-0">
        <!-- <span class="d-inline-block font-weight-medium text-high-emphasis">Total 48.5% Growth</span> <span class="text-high-emphasis">😎</span> this month -->
      </p>
    </template>

    <template #append>
      <IconBtn class="mt-n5">
        <VIcon
          color="high-emphasis"
          icon="ri-more-2-line"
        />
      </IconBtn>
    </template>

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
